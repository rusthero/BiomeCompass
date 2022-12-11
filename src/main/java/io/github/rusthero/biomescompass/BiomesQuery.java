package io.github.rusthero.biomescompass;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Optional;

public class BiomesQuery {
    static final LoadingCache<BiomesQuery, Result> cache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<>() {
        @Override
        public BiomesQuery.Result load(BiomesQuery query) {
            return query.fetch();
        }
    });

    private final Location origin;
    private final Biome target;

    public BiomesQuery(Location origin, Biome target) {
        this.origin = origin;
        this.target = target;
    }

    public Result fetch() {
        final HashMap<Biome, Location> biomeLocations = new HashMap<>();
        final int radius = 6400, resolution = 32;

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
                biomeLocations.put(biome, new Location(origin.getWorld(), x, 128.0, z));

            if (biome.equals(target)) {
                earlyBreak = true;
                break;
            }
        }

        return new Result(biomeLocations, earlyBreak);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BiomesQuery query)) return false;
        // Normally this is considered bad, but fine for cache purposes
        return (obj.hashCode() == query.hashCode());
    }

    @Override
    public int hashCode() {
        // Results of queries are going to be cached for 512x512 size divided areas
        return new Vector(Math.floor(origin.getX() / 512), 128, Math.floor(origin.getZ() / 512)).hashCode();
    }

    public static class Result {
        private final HashMap<Biome, Location> biomeLocations;
        private boolean earlyBreak = false;

        Result(HashMap<Biome, Location> biomeLocations, boolean earlyBreak) {
            this.biomeLocations = biomeLocations;
            this.earlyBreak = earlyBreak;
        }

        public Optional<Location> getLocation(Biome biome) {
            return Optional.ofNullable(biomeLocations.get(biome));
        }

        public boolean didBreakEarly() {
            return earlyBreak;
        }
    }
}