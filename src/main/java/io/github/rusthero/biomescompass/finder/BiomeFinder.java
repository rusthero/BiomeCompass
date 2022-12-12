package io.github.rusthero.biomescompass.finder;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public interface BiomeFinder {
    Optional<Location> locateBiome(Biome biome);

    void asyncLocateBiome(Biome biome, JavaPlugin plugin, PlayerBiomeFinder.LocateBiomeCallback callback);
}
