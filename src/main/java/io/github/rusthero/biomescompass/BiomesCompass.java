package io.github.rusthero.biomescompass;

import io.github.rusthero.biomescompass.gui.BiomesMenu;
import io.github.rusthero.biomescompass.items.BiomesCompassItem;
import io.github.rusthero.biomescompass.listeners.ItemUseListener;
import io.github.rusthero.biomescompass.listeners.MenuClickListener;
import io.github.rusthero.biomescompass.listeners.MenuDragListener;
import io.github.rusthero.biomescompass.locate.LocateBiomeCache;
import io.github.rusthero.biomescompass.locate.PlayerBiomeLocatorRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompass extends JavaPlugin {
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

        getServer().addRecipe(BiomesCompassItem.getRecipe(this));

        getLogger().info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomesCompass is disabled");
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

    public BiomesMenu getLocateBiomeMenu() {
        return biomesMenu;
    }
}