package io.github.rusthero.biomescompass.locate;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class BiomeLocator {
    final Player player;

    BiomeLocator(Player player) {
        this.player = player;
    }

    public Optional<Location> locateBiome(Biome biome) {
        LocateBiomeQuery query = new LocateBiomeQuery(player.getLocation(), biome);
        LocateBiomeQuery.Result result = LocateBiomeCache.singleton().get(query);

        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            if (!result.didBreakEarly()) return Optional.empty();

            result = LocateBiomeCache.singleton().fetch(query); // TODO: Continue from early break to improve performance
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
}
