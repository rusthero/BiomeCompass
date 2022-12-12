package io.github.rusthero.biomescompass.finder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BiomeFinderCache {
    private static BiomeFinderCache singleton;

    public static BiomeFinderCache singleton() {
        if (singleton == null) singleton = new BiomeFinderCache();

        return singleton;
    }

    final LoadingCache<BiomeFinderQuery, BiomeFinderQuery.Result> cache;

    private BiomeFinderCache() {
        cache = CacheBuilder.newBuilder().maximumSize(100).build(new BiomeFinderCache.Loader());
    }

    public BiomeFinderQuery.Result get(BiomeFinderQuery query) {
        return cache.getUnchecked(query);
    }

    public void invalidate(BiomeFinderQuery query) {
        cache.invalidate(query);
    }

    private class Loader extends CacheLoader<BiomeFinderQuery, BiomeFinderQuery.Result> {
        @Override
        public BiomeFinderQuery.Result load(BiomeFinderQuery query) {
            return query.fetch();
        }
    }
}
