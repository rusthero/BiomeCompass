package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import dev.rusthero.biomecompass.locate.PlayerBiomeLocator;
import dev.rusthero.biomecompass.util.Experience;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import static net.md_5.bungee.api.ChatMessageType.ACTION_BAR;
import static org.bukkit.Sound.*;

/**
 * Represents a listener to handle using biome compass. Users need to have permission "biomecompass.use" to use.
 */
public class ItemUseListener implements Listener {
    /**
     * Instance of the Biome Compass plugin.
     */
    private final BiomeCompass plugin;

    /**
     * Constructor for the ItemUseListener.
     *
     * @param plugin Instance of the Biome Compass plugin.
     */
    public ItemUseListener(BiomeCompass plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the PlayerInteractEvent and checks if the player is using a Biome Compass. If so, opens the biomes
     * menu if the player has the necessary permission and the compass is not on cooldown or already locating a biome.
     *
     * @param event PlayerInteractEvent to handle.
     */
    @EventHandler
    private void onItemUse(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if (event.getItem() == null || !BiomeCompassItem.isInstance(event.getItem())) return;

        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (!player.hasPermission("biomecompass.use")) return;

        PlayerBiomeLocator locator = plugin.getPlayerBiomeLocators().get(player);
        if (locator.isRunning()) {
            player.spigot().sendMessage(ACTION_BAR, new TextComponent("§eLocating"));
            player.playSound(location, BLOCK_CONDUIT_AMBIENT_SHORT, 1.0f, 1.0f);
            return;
        }
        if (locator.isOnCooldown()) {
            player.spigot().sendMessage(ACTION_BAR, new TextComponent("§9Cooling down"));
            player.playSound(location, BLOCK_CONDUIT_ATTACK_TARGET, 1.0f, 1.0f);
            return;
        }

        // Check if player has enough experience, if so withdraw.
        final int experienceCost = plugin.getSettings().experienceCost;
        if (experienceCost > 0) {
            final int playerExperience = Experience.getExp(player);
            if (playerExperience < experienceCost) {
                player.spigot().sendMessage(ACTION_BAR, new TextComponent("§cInsufficient experience"));
                player.playSound(location, BLOCK_CONDUIT_ATTACK_TARGET, 1.0f, 1.0f);
                return;
            }
            Experience.giveExp(player, -experienceCost);
        }

        // Check if player has sufficient funds and withdraw.
        final Economy economy = plugin.getEconomy();
        final int moneyCost = plugin.getSettings().moneyCost;
        if (moneyCost > 0 && economy != null) {
            if (!economy.has(player, moneyCost)) {
                player.spigot().sendMessage(ACTION_BAR, new TextComponent("§cInsufficient funds"));
                player.playSound(location, BLOCK_CONDUIT_ATTACK_TARGET, 1.0f, 1.0f);
                return;
            }
            economy.withdrawPlayer(player, moneyCost);
        }

        player.playSound(location, BLOCK_ENDER_CHEST_OPEN, 1.0f, 4.0f);
        plugin.getBiomesMenu().open(player);
    }
}
