package dev.rusthero.biomecompass.locate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.Settings;

/**
 * Represents a cache for queries made to locate biomes. This cache is useful for avoiding unnecessary re-locating of
 * biomes that have already been searched. Locate biome queries are not cached by player, but by location. This means
 * that the search of a player can also be useful for other players in the same area. Queries are defined by a
 * 256x256 grid, as specified in the {@link LocateBiomeQuery#hashCode()} and {@link LocateBiomeQuery#} methods.
 *
 * @see LocateBiomeQuery
 */
public class LocateBiomeCache {
    /**
     * A cache that stores the results of queries made to locate biomes. This cache is implemented using Google's
     * Guava library and its `LoadingCache` interface. Queries are mapped to their results when they are fetched
     *
     * @see LocateBiomeQuery
     * @see LocateBiomeQueryResult
     */
    final LoadingCache<LocateBiomeQuery, LocateBiomeQueryResult> cache;

    /**
     * Constructs a new `LocateBiomeCache` object using the given `BiomeCompass` plugin. A new loading cache is built
     * using the cache size specified in the plugin's settings.
     *
     * @param plugin The `BiomeCompass` plugin to use for getting the cache size and fetching locate biome queries.
     * @see LocateBiomeQuery#fetch
     */
    public LocateBiomeCache(BiomeCompass plugin) {
        Settings settings = plugin.getSettings();
        cache = CacheBuilder.newBuilder().maximumSize(settings.cacheSize).build(new CacheLoader<>() {
            @Override
            public LocateBiomeQueryResult load(LocateBiomeQuery query) {
                // If the query is not already cached, it will be fetched (calculated).
                // Queries are cached in 256x256 areas. If you go beyond an area that is cached, a fetch will occur.
                return query.fetch(settings.radius, settings.resolution);
            }
        });
    }

    /**
     * Forces the cache to invalidate and re-fetch the specified query. The result of the query is then stored in the
     * cache.
     *
     * @param query The `LocateBiomeQuery` whose result is going to be re-fetched and cached.
     * @return The result of the query, which is now stored in the cache.
     */
    public LocateBiomeQueryResult load(LocateBiomeQuery query) {
        // Removes the query from the cache so that it will be re-calculated when it is next used.
        cache.invalidate(query);
        return get(query); // And used here so re-calculated.
    }

    /**
     * Attempts to get the result of a `LocateBiomeQuery` from the cache. If the result is not already cached, it
     * will be fetched and cached.
     *
     * @param query The `LocateBiomeQuery` whose result is being requested.
     * @return The result of the query, which may be from the cache or may have been fetched.
     */
    public LocateBiomeQueryResult get(LocateBiomeQuery query) {
        return cache.getUnchecked(query); // It is unchecked because checked exceptions are not thrown by cache loader.
    }
}
