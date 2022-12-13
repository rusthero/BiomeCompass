package io.github.rusthero.biomescompass.util;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public final class SoundPlayer {
    public static void playRunningSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT_SHORT, 1.0f, 1.0f);
    }

    public static void playCooldownSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ATTACK_TARGET, 1.0f, 1.0f);
    }

    public static void playOpenMenuSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 4.0f);
    }

    public static void playStartLocatingSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);
    }

    public static void playLocatedSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_DEACTIVATE, 1.0f, 4.0f);
    }

    public static void playNotLocatedSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 1.0f, 4.0f);
    }
}
