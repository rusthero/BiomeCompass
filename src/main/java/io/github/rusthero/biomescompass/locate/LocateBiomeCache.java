package io.github.rusthero.biomescompass.locate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LocateBiomeCache {
    final LoadingCache<LocateBiomeQuery, LocateBiomeQuery.Result> cache;

    public LocateBiomeCache() {
        cache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<>() {
            @Override
            public LocateBiomeQuery.Result load(LocateBiomeQuery query) throws Exception {
                return query.fetch();
            }
        });
    }

    public LocateBiomeQuery.Result get(LocateBiomeQuery query) {
        return cache.getUnchecked(query);
    }

    public LocateBiomeQuery.Result fetch(LocateBiomeQuery query) {
        cache.invalidate(query);

        return get(query);
    }
}
