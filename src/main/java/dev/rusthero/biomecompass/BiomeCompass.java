package dev.rusthero.biomecompass;

import dev.rusthero.biomecompass.gui.BiomesMenu;
import dev.rusthero.biomecompass.listeners.ItemUseListener;
import dev.rusthero.biomecompass.listeners.MenuClickListener;
import dev.rusthero.biomecompass.listeners.MenuDragListener;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import dev.rusthero.biomecompass.locate.LocateBiomeCache;
import dev.rusthero.biomecompass.locate.PlayerBiomeLocatorRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomeCompass extends JavaPlugin {
    private Settings settings;
    private LocateBiomeCache locateBiomeCache;
    private PlayerBiomeLocatorRegistry playerBiomeLocators;
    private BiomesMenu biomesMenu;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        settings = new Settings(getConfig());

        locateBiomeCache = new LocateBiomeCache(this);
        playerBiomeLocators = new PlayerBiomeLocatorRegistry();
        biomesMenu = new BiomesMenu();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ItemUseListener(this), this);
        pluginManager.registerEvents(new MenuClickListener(this), this);
        pluginManager.registerEvents(new MenuDragListener(biomesMenu), this);

        getServer().addRecipe(BiomeCompassItem.getRecipe(this));

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