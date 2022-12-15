package dev.rusthero.biomecompass.locate;

import dev.rusthero.biomecompass.BiomeCompass;
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

    public Optional<Location> locateBiome(Biome biome, BiomeCompass biomeCompass) {
        LocateBiomeQuery query = new LocateBiomeQuery(player.getLocation(), biome);
        LocateBiomeQueryResult result = biomeCompass.getLocateBiomeCache().get(query);

        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            if (!result.isEarlyBreak()) return Optional.empty();
            result = biomeCompass.getLocateBiomeCache()
                    .fetch(query); // TODO: Continue from early break to improve performance
            location = result.getLocation(biome);
        }
        return location;
    }

    private boolean running = false;
    private boolean onCooldown = false;

    public void asyncLocateBiome(Biome biome, BiomeCompass biomeCompass, LocateBiomeCallback callback) {
        if (running || onCooldown) return;

        running = true;
        Bukkit.getScheduler().runTaskAsynchronously(biomeCompass, () -> {
            callback.onQueryDone(locateBiome(biome, biomeCompass));
            running = false;
        });


        if (biomeCompass.getSettings().cooldown > 0) {
            onCooldown = true;
            Bukkit.getScheduler()
                    .runTaskLater(biomeCompass, () -> onCooldown = false, biomeCompass.getSettings().cooldown * 20L);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }
}
