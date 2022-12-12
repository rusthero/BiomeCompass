package io.github.rusthero.biomescompass;

import io.github.rusthero.biomescompass.gui.BiomeSelectMenu;
import io.github.rusthero.biomescompass.item.BiomesCompassItem;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompass extends JavaPlugin {
    @Override
    public void onEnable() {
        // TODO Configuration

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new BiomesCompassItem.Listener(), this);
        pluginManager.registerEvents(new BiomeSelectMenu.Listener(this), this);

        getServer().addRecipe(BiomesCompassItem.getRecipe(this));

        getLogger().info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomesCompass is disabled");
    }
}