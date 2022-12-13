package io.github.rusthero.biomescompass.listeners;

import io.github.rusthero.biomescompass.BiomesCompass;
import io.github.rusthero.biomescompass.items.BiomesCompassItem;
import io.github.rusthero.biomescompass.locate.PlayerBiomeLocator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemUseListener implements Listener {
    private final BiomesCompass biomesCompass;

    public ItemUseListener(final BiomesCompass biomesCompass) {
        this.biomesCompass = biomesCompass;
    }

    @EventHandler
    private void onItemUse(final PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if (event.getItem() == null || !new BiomesCompassItem().equals(event.getItem())) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("biomescompass.use")) return;

        PlayerBiomeLocator locator = biomesCompass.getPlayerBiomeLocators().get(player);
        if (locator.isRunning()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Locating"));
            player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT_SHORT, 1.0f, 1.0f);
            return;
        }
        if (locator.isOnCooldown()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "Cooling Down"));
            player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ATTACK_TARGET, 1.0f, 1.0f);
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1.0f, 4.0f);
        biomesCompass.getLocateBiomeMenu().open(player);
    }
}
