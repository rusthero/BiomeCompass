package io.github.rusthero.biomescompass.task;

import io.github.rusthero.biomescompass.BiomesCompass;
import io.github.rusthero.biomescompass.locate.BiomeLocator;
import org.bukkit.entity.Player;

public class OpenLocateBiomeMenuTask {
    public OpenLocateBiomeMenuTask(Player player, BiomesCompass biomesCompass) {
        BiomeLocator locator = biomesCompass.getBiomeLocators().get(player);

        if (locator.isRunning()) {
            player.sendMessage("Please wait, searching.");
            return;
        }

        if (locator.isOnCooldown()) {
            player.sendMessage("Please wait, you are on cooldown.");
            return;
        }

        biomesCompass.getLocateBiomeMenu().open(player);
    }
}
