package dev.rusthero.biomecompass.locate;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.rusthero.biomecompass.BiomeCompass;

public class LocateBiomeCache {
    final LoadingCache<LocateBiomeQuery, LocateBiomeQueryResult> cache;

    public LocateBiomeCache(BiomeCompass biomeCompass) {
        cache = CacheBuilder.newBuilder().maximumSize(biomeCompass.getSettings().cacheSize).build(new CacheLoader<>() {
            @Override
            public LocateBiomeQueryResult load(LocateBiomeQuery query) {
                return query.fetch(biomeCompass);
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
