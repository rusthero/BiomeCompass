package dev.rusthero.biomecompass;

import dev.rusthero.biomecompass.gui.BiomesMenu;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import dev.rusthero.biomecompass.listeners.ItemUseListener;
import dev.rusthero.biomecompass.listeners.MenuClickListener;
import dev.rusthero.biomecompass.listeners.MenuDragListener;
import dev.rusthero.biomecompass.listeners.PrepareCraftListener;
import dev.rusthero.biomecompass.locate.LocateBiomeCache;
import dev.rusthero.biomecompass.locate.PlayerBiomeLocatorRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class represents the BiomeCompass plugin. It prepares the configuration, settings, cache for locate queries,
 * biome locators, biomes menu, listeners, and craft recipe.
 *
 * @see org.bukkit.plugin.java.JavaPlugin
 */
public class BiomeCompass extends JavaPlugin {
    /**
     * The Settings instance of the BiomeCompass plugin, which is read from the "config.yml" file in the data folder.
     */
    private Settings settings;

    /**
     * A cache that holds locate biome queries for faster access to already searched biomes in an area.
     *
     * @see dev.rusthero.biomecompass.locate.LocateBiomeQuery
     */
    private LocateBiomeCache locateBiomeCache;

    /**
     * A container for all player biome locators, which is used to check if a player is on cooldown or if a query is
     * already running.
     *
     * @see dev.rusthero.biomecompass.locate.PlayerBiomeLocator
     */
    private PlayerBiomeLocatorRegistry playerBiomeLocators;

    /**
     * A menu used to select biomes. Players can use this menu to locate desired biomes by using the compass item.
     *
     * @see ItemUseListener
     * @see MenuClickListener
     */
    private BiomesMenu biomesMenu;

    @Override
    public void onEnable() {
        // Prepare the configuration and settings for easier access to constants.
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        settings = new Settings(getConfig());

        // Create cache to store search queries for faster access to already searched biomes in an area.
        locateBiomeCache = new LocateBiomeCache(this);
        // Create the player biome locators registry to track player cooldowns and active queries.
        playerBiomeLocators = new PlayerBiomeLocatorRegistry();
        // Create the biomes menu, which will be used by players to select a biome and initiate a search.
        biomesMenu = new BiomesMenu();

        // Register the listeners required for the plugin to function properly.
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ItemUseListener(this), this);
        pluginManager.registerEvents(new MenuClickListener(this), this);
        pluginManager.registerEvents(new MenuDragListener(biomesMenu), this);
        pluginManager.registerEvents(new PrepareCraftListener(), this);

        // Add a shaped recipe for the Biome Compass.
        NamespacedKey biomeCompassKey = new NamespacedKey(this, "biome_compass");
        ShapedRecipe biomeCompassRecipe = BiomeCompassItem.getRecipe(biomeCompassKey);
        getServer().addRecipe(biomeCompassRecipe);

        getLogger().info("BiomeCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomeCompass is disabled");
    }

    public Settings getSettings() {
        return settings;
    }

    public LocateBiomeCache getLocateBiomeCache() {
        return locateBiomeCache;
    }

    public PlayerBiomeLocatorRegistry getPlayerBiomeLocators() {
        return playerBiomeLocators;
    }

    public BiomesMenu getBiomesMenu() {
        return biomesMenu;
    }
}