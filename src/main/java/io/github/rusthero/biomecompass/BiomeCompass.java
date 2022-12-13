package io.github.rusthero.biomecompass;

import io.github.rusthero.biomecompass.gui.BiomesMenu;
import io.github.rusthero.biomecompass.items.BiomeCompassItem;
import io.github.rusthero.biomecompass.listeners.ItemUseListener;
import io.github.rusthero.biomecompass.listeners.MenuClickListener;
import io.github.rusthero.biomecompass.listeners.MenuDragListener;
import io.github.rusthero.biomecompass.locate.LocateBiomeCache;
import io.github.rusthero.biomecompass.locate.PlayerBiomeLocatorRegistry;
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