package io.github.rusthero.biomescompass.task;

import io.github.rusthero.biomescompass.BiomesCompass;
import io.github.rusthero.biomescompass.locate.BiomeLocator;
import io.github.rusthero.biomescompass.locate.LocateBiomeCallback;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class LocateBiomeTask implements LocateBiomeCallback {
    private final BiomeLocator biomeLocator;

    public LocateBiomeTask(Player player, Biome biome, BiomesCompass biomesCompass) {
        biomeLocator = biomesCompass.getBiomeLocators().get(player);

        biomeLocator.asyncLocateBiome(biome, biomesCompass, this);
    }

    @Override
    public void onQueryDone(Optional<Location> optLocation) {
        optLocation.ifPresentOrElse(location -> {
            biomeLocator.player.sendMessage("Closest biome you selected is at: " + location.toVector());
            biomeLocator.player.setCompassTarget(location);
        }, () -> biomeLocator.player.sendMessage("Could not find the biome in max search area"));
    }

    @Override
    public void onRunning() {
        biomeLocator.player.sendMessage("You are already searching a biome!");
    }

    @Override
    public void onCooldown() {
        biomeLocator.player.sendMessage("Please wait, you are on cooldown!");
    }
}
