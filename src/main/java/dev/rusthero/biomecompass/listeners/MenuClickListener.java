package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.BiomeElement;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import dev.rusthero.biomecompass.locate.PlayerBiomeLocator;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

import static net.md_5.bungee.api.ChatMessageType.ACTION_BAR;
import static org.bukkit.Sound.BLOCK_CONDUIT_ACTIVATE;
import static org.bukkit.Sound.BLOCK_CONDUIT_AMBIENT;

/**
 * Represents a listener for handling clicks on the biome menu. Players must have the "biomecompass.use" permission
 * to use the Biome Compass.
 */
public class MenuClickListener implements Listener {
    /**
     * Instance of the Biome Compass plugin.
     */
    private final BiomeCompass plugin;

    /**
     * Constructs a new `MenuClickListener` instance.
     *
     * @param plugin the BiomeCompass instance that this listener belongs to
     */
    public MenuClickListener(BiomeCompass plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles clicks on the biome menu.
     *
     * @param event the InventoryClickEvent that occurred
     */
    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !plugin.getBiomesMenu().contains(event.getClickedInventory())) return;

        // Do not allow stealing items from the menu or putting or adding items.
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();
        if (!player.hasPermission("biomecompass.use")) return;
        Location origin = player.getLocation();

        // Menu will be closed as player selected a biome.
        player.closeInventory();

        player.spigot().sendMessage(ACTION_BAR, new TextComponent("§eLocating"));
        player.playSound(origin, BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        BiomeCompassItem.ifInstance(itemInHand, compass -> {
            // Get target biome from the clicked item, so we can locate it.
            Optional<BiomeElement> element = BiomeElement.getByItemStack(clickedItem);
            if (element.isEmpty()) return;

            Biome target;
            try {
                target = Biome.valueOf(element.get().name());
            } catch (IllegalArgumentException ignored) {
                player.spigot().sendMessage(ACTION_BAR, new TextComponent("§0Unknown biome"));
                return;
            }

            PlayerBiomeLocator locator = plugin.getPlayerBiomeLocators().get(player);
            locator.asyncLocateBiome(target, plugin, location -> {
                if (location.isEmpty()) {
                    player.spigot().sendMessage(ACTION_BAR, new TextComponent("§cNot found within search range"));
                    player.playSound(player.getLocation(), BLOCK_CONDUIT_AMBIENT, 1.0f, 4.0f);
                    return;
                }

                // Return if the compass is not in the player's inventory because we cannot modify the compass.
                if (!player.getInventory().contains(itemInHand)) {
                    player.spigot().sendMessage(ACTION_BAR, new TextComponent("§cHold the compass"));
                    player.playSound(player.getLocation(), BLOCK_CONDUIT_AMBIENT, 1.0f, 4.0f);
                    return;
                }

                player.spigot().sendMessage(ACTION_BAR, new TextComponent("§aLocated"));
                player.playSound(player.getLocation(), BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);

                String biomeName = element.get().displayName;
                // Modifying ItemMeta from an asynchronous task is not guaranteed to be successful, so we schedule a
                // synchronous task to run in the main thread with a delay to ensure that the bind is successful.
                Bukkit.getScheduler().runTaskLater(plugin, () -> compass.bindLocation(biomeName, location.get()), 3L);
            });
        });
    }
}
