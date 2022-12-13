package io.github.rusthero.biomescompass.locate;

import io.github.rusthero.biomescompass.BiomesCompass;
import org.bukkit.Location;
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

    public LocateBiomeQueryResult fetch(BiomesCompass biomesCompass) {
        final HashMap<Biome, Location> biomeLocations = new HashMap<>();
        final int radius = biomesCompass.getSettings().radius, resolution = biomesCompass.getSettings().resolution;

        int x = origin.getBlockX(), z = origin.getBlockZ();
        int dx = 32, dz = 0;
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

            Biome biome = origin.getWorld().getBiome(x, z);
            if (!biomeLocations.containsKey(biome))
                biomeLocations.put(biome, new Location(origin.getWorld(), x, 64.0, z));

            if (biome.equals(target)) {
                earlyBreak = true;
                break;
            }
        }

        return new LocateBiomeQueryResult(biomeLocations, earlyBreak);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocateBiomeQuery query)) return false;
        // Normally this is considered bad, but fine for cache purposes
        return (obj.hashCode() == query.hashCode());
    }

    @Override
    public int hashCode() {
        // Results of queries are going to be cached for 512x512 size divided areas
        return new Vector(Math.floor(origin.getX() / 512), 64, Math.floor(origin.getZ() / 512)).hashCode();
    }
}