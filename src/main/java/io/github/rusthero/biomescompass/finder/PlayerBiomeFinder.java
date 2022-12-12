package io.github.rusthero.biomescompass.finder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Optional;

public class PlayerBiomeFinder implements BiomeFinder {
    final Player player;

    PlayerBiomeFinder(Player player) {
        this.player = player;
    }

    @Override
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

    private boolean locked = false;

    @Override
    public void asyncLocateBiome(Biome biome, JavaPlugin plugin, LocateBiomeCallback callback) {
        if (locked) {
            callback.isLocked();

            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            locked = true;
            callback.onQueryDone(locateBiome(biome));
            locked = false;
        });
    }

    public boolean isLocked() {
        return locked;
    }

    public interface LocateBiomeCallback {
        void onQueryDone(Optional<Location> location);

        void isLocked();
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
