package io.github.rusthero.biomescompass;

import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompassPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        info("Plugin is enabled.");
    }

    @Override
    public void onDisable() {
        info("Plugin is disabled.");
    }

    public void info(String msg) {
        getLogger().info(msg);
    }
}