package io.github.rusthero.biomecompass.listeners;

import io.github.rusthero.biomecompass.BiomeCompass;
import io.github.rusthero.biomecompass.items.BiomeCompassItem;
import io.github.rusthero.biomecompass.locate.PlayerBiomeLocator;
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
    private final BiomeCompass biomeCompass;

    public ItemUseListener(final BiomeCompass biomeCompass) {
        this.biomeCompass = biomeCompass;
    }

    @EventHandler
    private void onItemUse(final PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if (event.getItem() == null || !BiomeCompassItem.isInstance(event.getItem())) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("biomecompass.use")) return;

        PlayerBiomeLocator locator = biomeCompass.getPlayerBiomeLocators().get(player);
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
        biomeCompass.getBiomesMenu().open(player);
    }
}
