package io.github.rusthero.biomescompass.finder;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerBiomeFinder implements BiomeFinder {
    private final Player player;

    public PlayerBiomeFinder(Player player) {
        this.player = player;
    }

    @Override
    public Optional<Location> locateBiome(Biome biome) {
        BiomeFinderQuery query = new BiomeFinderQuery(player.getLocation(), biome);
        BiomeFinderQuery.Result result = BiomeFinderCache.singleton().get(query);

        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            if (!result.didBreakEarly()) return Optional.empty();

            BiomeFinderCache.singleton().invalidate(query);
            result = BiomeFinderCache.singleton().get(query);
            location = result.getLocation(biome);
        }
        return location;
    }
}
