package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.gui.BiomeElement;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuClickListener implements Listener {
    private final BiomeCompass biomeCompass;

    public MenuClickListener(final BiomeCompass biomeCompass) {
        this.biomeCompass = biomeCompass;
    }

    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null || !biomeCompass.getBiomesMenu().contains(event.getClickedInventory())) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player player = (Player) event.getWhoClicked();
        if (!player.hasPermission("biomecompass.use")) return;

        player.closeInventory();
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "Locating"));
        player.playSound(player.getLocation(), Sound.BLOCK_CONDUIT_ACTIVATE, 1.0f, 1.0f);

        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        BiomeCompassItem.ifInstance(itemInHand, compass -> {
            try {
                BiomeElement.getByItemStack(clickedItem)
                        .ifPresent(element -> biomeCompass.getPlayerBiomeLocators().get(player)
                                .asyncLocateBiome(Biome.valueOf(element.name()), biomeCompass, optLocation -> {
                                    optLocation.ifPresentOrElse(location -> {
                                        if (!player.getInventory().contains(itemInHand)) {
                                            player.spigot()
                                                    .sendMessage(ChatMessageType.ACTION_BAR,
                                                                 new TextComponent(
                                                                         ChatColor.RED + "Hold the " +
                                                                                 "compass"));
                                            player.playSound(player.getLocation(),
                                                             Sound.BLOCK_CONDUIT_AMBIENT,
                                                             1.0f, 4.0f);
                                            return;
                                        }

                                        player.spigot()
                                                .sendMessage(ChatMessageType.ACTION_BAR,
                                                             new TextComponent(
                                                                     ChatColor.GREEN + "Located"));
                                        player.playSound(player.getLocation(),
                                                         Sound.BLOCK_CONDUIT_ACTIVATE,
                                                         1.0f, 1.0f);

                                        Bukkit.getScheduler().runTaskLater(biomeCompass, () ->
                                                compass.bindLocation(element.displayName, location), 3L);
                                    }, () -> {
                                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                                                    new TextComponent(ChatColor.RED +
                                                                                              "Not " +
                                                                                              "found " +
                                                                                              "within " +
                                                                                              "search " +
                                                                                              "range"));
                                        player.playSound(player.getLocation(),
                                                         Sound.BLOCK_CONDUIT_AMBIENT,
                                                         1.0f, 4.0f);
                                    });
                                }));
            } catch (
                    IllegalArgumentException exception) {
                player.spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLACK + "Unknown biome"));
            }
        });
    }
}
