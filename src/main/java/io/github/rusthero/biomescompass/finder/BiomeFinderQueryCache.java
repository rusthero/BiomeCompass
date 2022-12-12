package io.github.rusthero.biomescompass.finder;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BiomeFinderQueryCache {
    private static BiomeFinderQueryCache singleton;

    public static BiomeFinderQueryCache singleton() {
        if (singleton == null) singleton = new BiomeFinderQueryCache();

        return singleton;
    }

    final LoadingCache<BiomeFinderQuery, BiomeFinderQuery.Result> cache;

    private BiomeFinderQueryCache() {
        cache = CacheBuilder.newBuilder().maximumSize(100).build(new BiomeFinderQueryCache.Loader());
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
