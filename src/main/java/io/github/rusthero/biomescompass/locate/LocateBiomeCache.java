package io.github.rusthero.biomescompass.locate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LocateBiomeCache {
    private static LocateBiomeCache singleton;

    public static LocateBiomeCache singleton() {
        if (singleton == null) singleton = new LocateBiomeCache();

        return singleton;
    }

    final LoadingCache<LocateBiomeQuery, LocateBiomeQuery.Result> cache;

    private LocateBiomeCache() {
        cache = CacheBuilder.newBuilder().maximumSize(100).build(new LocateBiomeCache.Loader());
    }

    public LocateBiomeQuery.Result get(LocateBiomeQuery query) {
        return cache.getUnchecked(query);
    }

    public LocateBiomeQuery.Result fetch(LocateBiomeQuery query) {
        cache.invalidate(query);

        return get(query);
    }

    private class Loader extends CacheLoader<LocateBiomeQuery, LocateBiomeQuery.Result> {
        @Override
        public LocateBiomeQuery.Result load(LocateBiomeQuery query) {
            return query.fetch();
        }
    }
}
