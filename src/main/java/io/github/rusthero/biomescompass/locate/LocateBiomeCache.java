package io.github.rusthero.biomescompass.locate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.rusthero.biomescompass.BiomesCompass;

public class LocateBiomeCache {
    final LoadingCache<LocateBiomeQuery, LocateBiomeQueryResult> cache;

    public LocateBiomeCache(BiomesCompass biomesCompass) {
        cache = CacheBuilder.newBuilder().maximumSize(biomesCompass.getSettings().cacheSize).build(new CacheLoader<>() {
            @Override
            public LocateBiomeQueryResult load(LocateBiomeQuery query) {
                return query.fetch(biomesCompass);
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
