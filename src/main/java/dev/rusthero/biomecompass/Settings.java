package dev.rusthero.biomecompass;

import dev.rusthero.biomecompass.lang.Language;
import dev.rusthero.biomecompass.lang.LanguageRegistry;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;

/**
 * This class represents the settings read from the configuration file of the plugin.
 */
public final class Settings {
    /**
     * The language that is going to be used for in-game messages and text for biome compass.
     */
    public final Language language;

    /**
     * The cooldown duration in seconds for using the biome compass. The default value is 5 seconds.
     *
     * @see dev.rusthero.biomecompass.listeners.ItemUseListener
     */
    public final long cooldown;

    /**
     * A flag indicating whether the distance to the target biome should be displayed when the compass is used.
     */
    public final boolean showDistance;

    /**
     * The number of experience points required to use the Biome Compass.
     */
    public final int experienceCost;

    /**
     * The amount of money required to use the Biome Compass.
     */
    public final double moneyCost;

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
     * Determines which biomes are displayed in the Biome Compass GUI.
     */
    public final HashMap<Biome, Boolean> biomes;

    /**
     * Constructs a new Settings instance and reads in the setting values from the specified configuration file.
     *
     * @param config The file configuration from which the setting values will be read.
     */
    Settings(FileConfiguration config, LanguageRegistry languages) {
        this.language = languages.get(config.getString("language"));
        this.cooldown = config.getLong("cooldown");
        this.showDistance = config.getBoolean("show_distance");
        this.resolution = config.getInt("resolution");
        this.radius = config.getInt("radius");
        this.cacheSize = config.getInt("cache_size");
        this.experienceCost = config.getInt("experience_cost");
        this.moneyCost = config.getDouble("money_cost");

        biomes = new HashMap<>();
        ConfigurationSection section = config.getConfigurationSection("biomes");
        if (section != null)
            section.getKeys(true).forEach(biomeStr -> {
                try {
                    biomes.put(Biome.valueOf(biomeStr.toUpperCase()), section.getBoolean(biomeStr));
                } catch (IllegalArgumentException ignored) {
                    // We can safely ignore if a biome name is invalid because we cannot locate a biome which does
                    // not exist within this version anyway.
                }
            });
    }
}
