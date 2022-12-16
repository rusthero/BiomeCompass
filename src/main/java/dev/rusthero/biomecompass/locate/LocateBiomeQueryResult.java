package dev.rusthero.biomecompass.locate;

import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.Optional;

/**
 * Represents the result of a query made using the {@link LocateBiomeQuery#fetch} method. The nearest locations of
 * biomes are stored in the {@link #biomeLocations} field. Note: Locate biome query results are cached in the
 * {@link LocateBiomeCache} for performance reasons.
 */
public class LocateBiomeQueryResult {
    /**
     * * A mapping of the found biomes to their nearest locations. This field only contains biomes that were found
     * while searching for a specific target biome. It is possible that not all biomes in the game are mapped, but
     * this is useful for instant locating of biomes that have already been found.
     */
    private final HashMap<Biome, Location> biomeLocations;

    /**
     * A flag indicating whether the target biome was found before the search reached the maximum range set in
     * {@link dev.rusthero.biomecompass.Settings#radius}. This is useful to know when a cache result has an empty
     * target biome. If the search did not break early but the target biome is empty, it means that the target biome
     * does not exist within the radius.
     *
     * @see LocateBiomeQuery#fetch
     * @see PlayerBiomeLocator#locateBiome
     */
    private final boolean earlyBreak;

    /**
     * Constructs a new `LocateBiomeQueryResult` object with the given parameters.
     *
     * @param biomeLocations A mapping of biomes to their nearest locations.
     * @param earlyBreak     A flag indicating whether the search for the target biome broke early due to reaching
     *                       the maximum search range.
     */
    LocateBiomeQueryResult(HashMap<Biome, Location> biomeLocations, boolean earlyBreak) {
        this.biomeLocations = biomeLocations;
        this.earlyBreak = earlyBreak;
    }

    /**
     * Gets the nearest location of the specified biome, if it was found during the query.
     *
     * @param biome The biome whose location is requested.
     * @return An Optional containing the nearest location of the specified biome, or an empty Optional if the
     * biome was not found during the query.
     */
    public Optional<Location> getLocation(Biome biome) {
        return Optional.ofNullable(biomeLocations.get(biome));
    }

    public boolean isEarlyBreak() {
        return earlyBreak;
    }
}
