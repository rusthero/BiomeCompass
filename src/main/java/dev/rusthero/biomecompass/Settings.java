package dev.rusthero.biomecompass;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * This class represents the settings read from the configuration file of the plugin.
 */
public final class Settings {
    /**
     * The cooldown duration in seconds for using the biome compass. The default value is 5 seconds.
     *
     * @see dev.rusthero.biomecompass.listeners.ItemUseListener
     */
    public final long cooldown;

    /**
     * The resolution used when locating biomes in blocks. The default value is 32 blocks, so biomes narrower than 32
     * blocks may not be detected.
     *
     * @see dev.rusthero.biomecompass.locate.LocateBiomeQuery
     */
    public final int resolution;

    /**
     * The maximum range when locating biomes in blocks. The default value is 6400, which means that a square area of
     * 12801x12801 blocks will be searched.
     *
     * @see dev.rusthero.biomecompass.locate.LocateBiomeQuery
     */
    public final int radius;

    /**
     * The size of the cache for holding biome locate queries. The default value is 100, which means that a maximum
     * of 100 queries will be cached.
     *
     * @see dev.rusthero.biomecompass.locate.LocateBiomeCache
     */
    public final int cacheSize;

    /**
     * Constructs a new Settings instance and reads in the setting values from the specified configuration file.
     *
     * @param config The file configuration from which the setting values will be read.
     */
    Settings(FileConfiguration config) {
        this.cooldown = config.getLong("cooldown");
        this.resolution = config.getInt("resolution");
        this.radius = config.getInt("radius");
        this.cacheSize = config.getInt("cache_size");
    }
}
