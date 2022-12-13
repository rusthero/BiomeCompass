package io.github.rusthero.biomescompass.locate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class LocateBiomeCache {
    final LoadingCache<LocateBiomeQuery, LocateBiomeQueryResult> cache;

    public LocateBiomeCache() {
        cache = CacheBuilder.newBuilder().maximumSize(100).build(new CacheLoader<>() {
            @Override
            public LocateBiomeQueryResult load(LocateBiomeQuery query) {
                return query.fetch();
            }
        });
    }

    public LocateBiomeQueryResult get(LocateBiomeQuery query) {
        return cache.getUnchecked(query);
    }

    public LocateBiomeQueryResult fetch(LocateBiomeQuery query) {
        cache.invalidate(query);
        return get(query);
    }
}
