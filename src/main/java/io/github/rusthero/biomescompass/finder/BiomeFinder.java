package io.github.rusthero.biomescompass.finder;

import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.Optional;

public interface BiomeFinder {
    Optional<Location> locateBiome(Biome biome);
}
