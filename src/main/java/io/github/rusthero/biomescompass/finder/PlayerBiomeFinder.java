package io.github.rusthero.biomescompass.finder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class PlayerBiomeFinder implements BiomeFinder {
    private final Player player;

    public PlayerBiomeFinder(Player player) {
        this.player = player;
    }

    @Override
    public Optional<Location> locateBiome(Biome biome) {
        BiomeFinderQuery query = new BiomeFinderQuery(player.getLocation(), biome);
        BiomeFinderQuery.Result result = BiomeFinderQueryCache.singleton().get(query);

        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            if (!result.didBreakEarly()) return Optional.empty();

            BiomeFinderQueryCache.singleton().invalidate(query);
            result = BiomeFinderQueryCache.singleton().get(query);
            location = result.getLocation(biome);
        }
        return location;
    }

    @Override
    public void asyncLocateBiome(Biome biome, JavaPlugin plugin, LocateBiomeCallback callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Optional<Location> location = locateBiome(biome);

            callback.onQueryDone(location);
        });
    }

    public interface LocateBiomeCallback {
        void onQueryDone(Optional<Location> location);
    }
}
