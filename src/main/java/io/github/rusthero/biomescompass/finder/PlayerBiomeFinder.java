package io.github.rusthero.biomescompass.finder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Optional;

public class PlayerBiomeFinder {
    final Player player;

    PlayerBiomeFinder(Player player) {
        this.player = player;
    }

    public Optional<Location> locateBiome(Biome biome) {
        BiomeFinderQuery query = new BiomeFinderQuery(player.getLocation(), biome);
        BiomeFinderQuery.Result result = BiomeFinderQueryCache.singleton().get(query);

        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            if (!result.didBreakEarly()) return Optional.empty();

            result = BiomeFinderQueryCache.singleton().fetch(query); // TODO: Continue from early break to improve performance
            location = result.getLocation(biome);
        }
        return location;
    }

    private boolean running = false;
    private boolean onCooldown = false;

    public void asyncLocateBiome(Biome biome, JavaPlugin plugin, LocateBiomeCallback callback) {
        if (running) {
            callback.onRunning();

            return;
        }

        running = true;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            callback.onQueryDone(locateBiome(biome));
            running = false;
        });

        onCooldown = true;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            onCooldown = false;
        }, 100L);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }

    public static class Container extends HashSet<PlayerBiomeFinder> {
        private static Container singleton;

        public static Container singleton() {
            if (singleton == null) singleton = new Container();

            return singleton;
        }

        private Container() {

        }

        public PlayerBiomeFinder get(Player player) {
            Optional<PlayerBiomeFinder> optFinder = this.stream().filter(finder -> finder.player.equals(player)).findFirst();

            if (optFinder.isEmpty()) {
                PlayerBiomeFinder newFinder = new PlayerBiomeFinder(player);
                add(newFinder);
                return newFinder;
            } else {
                return optFinder.get();
            }
        }
    }
}
