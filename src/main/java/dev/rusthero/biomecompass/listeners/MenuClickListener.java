package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.BiomeElement;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import dev.rusthero.biomecompass.lang.Language;
import dev.rusthero.biomecompass.locate.PlayerBiomeLocator;
import dev.rusthero.biomecompass.util.Experience;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
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

import java.util.HashSet;
import java.util.Optional;

import static dev.rusthero.biomecompass.lang.Field.*;
import static java.lang.String.format;
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

        final Language lang = plugin.getSettings().language;

        final HashSet<String> costMessages = new HashSet<>();
        // Withdraw experience from the player.
        final int experienceCost = plugin.getSettings().experienceCost;
        if (experienceCost > 0) {
            if (Experience.getExp(player) < experienceCost) return;
            Experience.giveExp(player, -experienceCost);
            costMessages.add(format("§a%d %s", experienceCost, lang.getString(ITEM_BIOME_COMPASS_COST_XP)));
        }
        // Withdraw money from the player.
        final Economy economy = plugin.getEconomy();
        final double moneyCost = plugin.getSettings().moneyCost;
        if (moneyCost > 0 && economy != null) {
            if (!economy.has(player, moneyCost)) return;
            economy.withdrawPlayer(player, moneyCost);
            String currencyName = moneyCost == 1 ? economy.currencyNameSingular() : economy.currencyNamePlural();
            costMessages.add(format("§6%.2f %s", moneyCost, currencyName));
        }

        String locatingMessage = format("§e%s", lang.getString(ITEM_BIOME_COMPASS_LOCATING));
        if (costMessages.size() > 0) {
            locatingMessage += format(": §c%s ", lang.getString(ITEM_BIOME_COMPASS_COST));
            locatingMessage += String.join("§c, ", costMessages);
        }
        player.spigot().sendMessage(ACTION_BAR, new TextComponent(locatingMessage));
        player.playSound(origin, BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        BiomeCompassItem.ifInstance(itemInHand, lang, plugin, compass -> {
            // Get target biome from the clicked item, so we can locate it.
            Optional<BiomeElement> element = BiomeElement.getByItemStack(clickedItem, lang);
            if (element.isEmpty()) return;

            Biome target;
            try {
                target = Biome.valueOf(element.get().name());
            } catch (IllegalArgumentException ignored) {
                player.spigot().sendMessage(
                        ACTION_BAR,
                        new TextComponent("§0" + lang.getString(ITEM_BIOME_COMPASS_UNKNOWN_BIOME))
                );
                return;
            }

            PlayerBiomeLocator locator = plugin.getPlayerBiomeLocators().get(player);
            locator.asyncLocateBiome(target, plugin, location -> {
                if (location.isEmpty()) {
                    player.spigot().sendMessage(
                            ACTION_BAR,
                            new TextComponent("§c" + lang.getString(ITEM_BIOME_COMPASS_NOT_FOUND))
                    );
                    player.playSound(player.getLocation(), BLOCK_CONDUIT_AMBIENT, 1.0f, 4.0f);
                    return;
                }

                // Return if the compass is not in the player's inventory because we cannot modify the compass.
                if (!player.getInventory().contains(itemInHand)) {
                    player.spigot().sendMessage(
                            ACTION_BAR,
                            new TextComponent("§c" + lang.getString(ITEM_BIOME_COMPASS_NOT_HOLDING))
                    );
                    player.playSound(player.getLocation(), BLOCK_CONDUIT_AMBIENT, 1.0f, 4.0f);
                    return;
                }

                String locatedMessage = "§a" + lang.getString(ITEM_BIOME_COMPASS_LOCATED);
                // If player teleports to another dimension while locating, we cannot calculate distance.
                if (plugin.getSettings().showDistance && player.getWorld().equals(location.get().getWorld())) {
                    int distance = (int) Math.round(player.getLocation().distance(location.get()));
                    locatedMessage += format(
                            format(" (§e%s§a)", lang.getString(ITEM_BIOME_COMPASS_LOCATED_DISTANCE)),
                            distance
                    );
                }
                player.spigot().sendMessage(ACTION_BAR, new TextComponent(locatedMessage));
                player.playSound(player.getLocation(), BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);

                String biomeName = element.get().getDisplayName(lang);
                // Modifying ItemMeta from an asynchronous task is not guaranteed to be successful, so we schedule a
                // synchronous task to run in the main thread with a delay to ensure that the bind is successful.
                Bukkit.getScheduler().runTaskLater(plugin, () ->
                        compass.bindLocation(biomeName, lang, location.get(), plugin), 3L);
            });
        });
    }
}
