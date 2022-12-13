package io.github.rusthero.biomescompass.locate;

import io.github.rusthero.biomescompass.BiomesCompass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PlayerBiomeLocator {
    public final Player player;

    PlayerBiomeLocator(Player player) {
        this.player = player;
    }

    public Optional<Location> locateBiome(Biome biome, BiomesCompass biomesCompass) {
        LocateBiomeQuery query = new LocateBiomeQuery(player.getLocation(), biome);
        LocateBiomeQueryResult result = biomesCompass.getLocateBiomeCache().get(query);

        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            if (!result.isEarlyBreak()) return Optional.empty();
            result = biomesCompass.getLocateBiomeCache().fetch(query); // TODO: Continue from early break to improve performance
            location = result.getLocation(biome);
        }
        return location;
    }

    private boolean running = false;
    private boolean onCooldown = false;

    public void asyncLocateBiome(Biome biome, BiomesCompass biomesCompass, LocateBiomeCallback callback) {
        if (running || onCooldown) return;

        running = true;
        Bukkit.getScheduler().runTaskAsynchronously(biomesCompass, () -> {
            callback.onQueryDone(locateBiome(biome, biomesCompass));
            running = false;
        });

        onCooldown = true;
        if (biomesCompass.getSettings().cooldown > 0)
            Bukkit.getScheduler().runTaskLater(biomesCompass, () -> onCooldown = false, biomesCompass.getSettings().cooldown * 20L);
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }
}
