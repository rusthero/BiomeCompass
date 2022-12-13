package io.github.rusthero.biomecompass;

import org.bukkit.configuration.file.FileConfiguration;

public final class Settings {
    public final long cooldown;
    public final int resolution;
    public final int radius;
    public final int cacheSize;

    Settings(FileConfiguration config) {
        this.cooldown = (long) config.getDouble("cooldown");
        this.resolution = config.getInt("resolution");
        this.radius = config.getInt("radius");
        this.cacheSize = config.getInt("cacheSize");
    }
}
