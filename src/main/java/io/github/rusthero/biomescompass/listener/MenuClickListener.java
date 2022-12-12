package io.github.rusthero.biomescompass.listener;

import io.github.rusthero.biomescompass.BiomesCompass;
import io.github.rusthero.biomescompass.gui.LocateBiomeMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuClickListener implements Listener {
    private final BiomesCompass biomesCompass;
    private final LocateBiomeMenu menu;

    public MenuClickListener(final BiomesCompass biomesCompass, final LocateBiomeMenu menu) {
        this.biomesCompass = biomesCompass;
        this.menu = menu;
    }

    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        if (menu.contains(event.getClickedInventory())) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player player = (Player) event.getWhoClicked();
        player.closeInventory();
        menu.itemToBiome(clickedItem).ifPresent(biome -> biomesCompass.searchBiome(player, biome));
    }
}
