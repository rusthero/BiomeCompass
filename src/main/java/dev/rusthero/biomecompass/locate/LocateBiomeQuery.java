package dev.rusthero.biomecompass.locate;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.gui.BiomeElement;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class LocateBiomeQuery {
    private final Location origin;
    private final Biome target;

    public LocateBiomeQuery(Location origin, Biome target) {
        this.origin = origin;
        this.target = target;
    }

    public LocateBiomeQueryResult fetch(BiomeCompass biomeCompass) {
        final HashMap<Biome, Location> biomeLocations = new HashMap<>();
        final int radius = biomeCompass.getSettings().radius, resolution = biomeCompass.getSettings().resolution;
        final World world = origin.getWorld();

        int x = origin.getBlockX(), z = origin.getBlockZ();
        int dx = resolution, dz = 0;
        int segment_length = 1, segment_passed = 0;

        boolean earlyBreak = false;
        for (int k = 0; k < Math.pow(radius * 2.0 / resolution, 2); ++k) {
            x += dx;
            z += dz;
            ++segment_passed;

            if (segment_passed == segment_length) {
                segment_passed = 0;

                int buffer = dx;
                dx = -dz;
                dz = buffer;

                if (dz == 0) ++segment_length;
            }

            try {
                Biome biome = world.getBiome(x, z);
                BiomeElement element = BiomeElement.valueOf(biome.name());

                if (!biomeLocations.containsKey(biome)) biomeLocations.put(biome, new Location(world, x, 128, z));

                if (world.getEnvironment().equals(World.Environment.NORMAL)) {
                    Biome earthBiome = biome;
                    if (element.isUnderground) {
                        earthBiome = world.getBiome(x, 128, z);
                        if (!biomeLocations.containsKey(earthBiome))
                            biomeLocations.put(earthBiome, new Location(world, x, 128, z));
                    }

                    Biome deepBiome = world.getBiome(x, -52, z);
                    if (!biomeLocations.containsKey(deepBiome))
                        biomeLocations.put(deepBiome, new Location(world, x, 128, z));

                    if (biome.equals(target) || earthBiome.equals(target) || deepBiome.equals(target)) {
                        earlyBreak = true;
                        break;
                    }
                }
            } catch (IllegalArgumentException ignored) {

            }
        }

        return new LocateBiomeQueryResult(biomeLocations, earlyBreak);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocateBiomeQuery)) return false;
        LocateBiomeQuery query = (LocateBiomeQuery) obj;
        return (obj.hashCode() == query.hashCode()); // Normally this is considered bad, but fine for cache purposes
    }

    @Override
    public int hashCode() {
        // Results of queries are going to be cached for 512x512 size divided areas
        return new Vector(Math.floor(origin.getX() / 256), 64, Math.floor(origin.getZ() / 256)).hashCode();
    }
}