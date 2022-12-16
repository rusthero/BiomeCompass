package dev.rusthero.biomecompass.locate;

import dev.rusthero.biomecompass.BiomeElement;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

import java.util.HashMap;

import static org.bukkit.World.Environment;

/**
 * Represents a query for finding the nearest biome to a given location.
 * It is recommended to use {@link PlayerBiomeLocator} for accessing this class, as it provides caching and additional
 * features.
 *
 * @see LocateBiomeQueryResult
 */
public class LocateBiomeQuery {
    /**
     * The location from which the search will start and expand outwards in a spiral pattern.
     */
    private final Location origin;

    /**
     * The target biome that is to be found. The search will end when this biome is found or the maximum range is
     * reached.
     */
    private final Biome target;

    /**
     * Constructs a locate biome query with the given origin location and target biome.
     *
     * @param origin The location from which the search will start and expand outwards in a spiral pattern.
     * @param target The target biome that is to be found. The search will end when this biome is found or the
     *               maximum range is reached.
     */
    public LocateBiomeQuery(Location origin, Biome target) {
        this.origin = origin;
        this.target = target;
    }

    /**
     * Makes a search for locating the target biome around the origin point using an outward spiral algorithm. Also
     * returns the other the nearest locations for biomes found on the way while searching.
     *
     * @param radius     Maximum range in blocks for the search. The search will cover an area of 12801x12801 blocks if
     *                   the radius is 6400.
     * @param resolution Resolution in blocks for the search. Biomes narrower than this value may not be found.
     * @return Result of the query which holds locations of the nearest biomes that are found while searching for the
     * target. The target biome may be empty if not found within the radius. The result also includes a flag
     * indicating whether a full search was made.
     * <p>
     * Returns null if the {@link #origin}'s world is null.
     * @see LocateBiomeQueryResult#getLocation(Biome)
     * @see LocateBiomeQueryResult#isEarlyBreak()
     */

    public LocateBiomeQueryResult fetch(final int radius, final int resolution) {
        // Initialize a map to store the nearest locations of biomes found while searching.
        final HashMap<Biome, Location> biomeLocations = new HashMap<>();
        final World world = origin.getWorld();
        if (world == null) return null;

        // Algorithm for iterating over an outward spiral on a discrete 2D grid from the origin.
        // Credits to Nikita Rybak on https://stackoverflow.com/a/3706260

        // (dx, dz) is a vector - direction in which we move right now.
        int dx = resolution, dz = 0; // Initial value of dx will be the resolution, 32 for default.
        // Length of current segment.
        int segment_length = 1;

        // Current position (x, z) and how much of current segment we passed.
        int x = origin.getBlockX(), z = origin.getBlockZ(), segment_passed = 0;

        // Calculate the length of the sides of the square to be searched.
        final double sideLength = radius * 2 + 1;
        // Calculate the total number of points in the square. (Not 100% accurate for a full square but good enough.)
        final int numPointsInSquare = (int) Math.ceil(Math.pow(sideLength / resolution, 2));

        // Initialize a flag to track if the search broke early due to reaching the target biome or reaching the
        // maximum search radius.
        boolean earlyBreak = false;

        for (int k = 0; k < numPointsInSquare; ++k) {
            // Make a step, add 'direction' vector (dx, dz) to current position (x, z).
            x += dx;
            z += dz;
            ++segment_passed;

            if (segment_passed == segment_length) {
                // Done with current segment.
                segment_passed = 0;

                // Rotate directions.
                int buffer = dx;
                dx = -dz;
                dz = buffer;

                // Increase segment length if necessary.
                if (dz == 0) ++segment_length;
            }

            try {
                // Prepare the location to be checked, disregard y level
                Location location = new Location(world, x, 128, z);

                // This getBiome method is deprecated but really useful. If there is a lush cave under a forest biome
                // lets say, this method will return the lush cave so no need to implement a 3D check. However, we
                // need to check the earth (forest above) biome as well if it is flagged as an underground biome.
                Biome biome = world.getBiome(x, z);
                boolean isUnderground = BiomeElement.valueOf(biome.name()).isUnderground;

                // Adds the location of biome if not already added. This way, the ones found first (nearest) will be
                // added. Remember, this biome does not have to be our target but will be useful for caching later.
                if (!biomeLocations.containsKey(biome)) biomeLocations.put(biome, location);

                // Check the biomes above and below the current location if the environment is the overworld
                if (!world.getEnvironment().equals(Environment.NORMAL)) continue;

                // If we find an underground biome, we need to check the biome above to ensure that we don't miss any
                // biomes. This is particularly common with bamboo jungle biomes, as narrow lush caves often generate
                // beneath them. This caused us to miss a lot of bamboo jungle biomes in previous builds.
                Biome earthBiome = biome;
                if (isUnderground) {
                    earthBiome = world.getBiome(x, 128, z);
                    if (!biomeLocations.containsKey(earthBiome)) biomeLocations.put(earthBiome, location);
                }

                // In the case of the deep dark biome, the deprecated getBiome method may not be able to find it. As a
                // result, we need to use a special check. It appears that deep dark biomes (not sure) tend to
                // generate at y level -52, and this seems to work well. However, further testing is definitely needed.
                Biome deepBiome = world.getBiome(x, -52, z);
                if (!biomeLocations.containsKey(deepBiome)) biomeLocations.put(deepBiome, location);

                // If we find the target biome, there is no reason to continue searching, so we can break the loop
                // and update the earlyBreak flag.
                if (biome.equals(target) || earthBiome.equals(target) || deepBiome.equals(target)) {
                    earlyBreak = true;
                    break;
                }
            } catch (IllegalArgumentException ignored) {
                // An IllegalArgumentException may occur if the biome name is not registered in the BiomeElement,
                // which can happen in newer versions that are not supported yet. This is not a problem because if a
                // new biome is not included in the BiomeElement, it is also not included in the menu. Therefore,
                // players cannot locate unsupported biomes, and it is safe to ignore the exception.
            }
        }

        return new LocateBiomeQueryResult(biomeLocations, earlyBreak);
    }

    /**
     * Determines whether this query is equal to the given object. This method should not be used to compare two
     * queries, as it is designed for caching purposes.
     *
     * @param obj The object to compare with this query.
     * @return {@code true} if the given object is a LocateBiomeQuery and has the same hash code as this query,
     * {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocateBiomeQuery)) return false;
        LocateBiomeQuery query = (LocateBiomeQuery) obj;
        // This implementation of equals is not ideal for general comparisons between objects, but it is acceptable
        // for caching purposes as the hash code of both objects will be used to determine equality. This is the
        // intended use of this class.
        return (obj.hashCode() == query.hashCode());
    }

    /**
     * Generates a hash code for this locate biome query, made for caching purposes.
     *
     * @return A hash code for this locate biome query.
     */
    @Override
    public int hashCode() {
        // The hash code is calculated based on the coordinates of the origin location, rounded down to the nearest
        // multiple of 256. The y coordinate is not used in the calculation because it does not matter for the
        // purposes of caching the results of the query.
        return new Vector(Math.floor(origin.getX() / 256), 64, Math.floor(origin.getZ() / 256)).hashCode();
    }
}