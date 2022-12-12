package io.github.rusthero.biomescompass;

import io.github.rusthero.biomescompass.gui.LocateBiomeMenu;
import io.github.rusthero.biomescompass.item.BiomesCompassItem;
import io.github.rusthero.biomescompass.listener.ItemUseListener;
import io.github.rusthero.biomescompass.listener.MenuClickListener;
import io.github.rusthero.biomescompass.listener.MenuDragListener;
import io.github.rusthero.biomescompass.locate.BiomeLocatorRegistry;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompass extends JavaPlugin {
    private BiomeLocatorRegistry biomeLocators;
    private LocateBiomeMenu locateBiomeMenu;

    @Override
    public void onEnable() {
        // TODO Configuration

        biomeLocators = new BiomeLocatorRegistry();
        locateBiomeMenu = new LocateBiomeMenu();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ItemUseListener(this), this);
        pluginManager.registerEvents(new MenuClickListener(this), this);
        pluginManager.registerEvents(new MenuDragListener(locateBiomeMenu), this);

        getServer().addRecipe(BiomesCompassItem.getRecipe(this));

        getLogger().info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomesCompass is disabled");
    }

    public BiomeLocatorRegistry getBiomeLocators() {
        return biomeLocators;
    }

    public LocateBiomeMenu getLocateBiomeMenu() {
        return locateBiomeMenu;
    }
}