package io.github.rusthero.biomescompass.listener;

import io.github.rusthero.biomescompass.BiomesCompass;
import io.github.rusthero.biomescompass.task.LocateBiomeTask;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MenuClickListener implements Listener {
    private final BiomesCompass biomesCompass;

    public MenuClickListener(final BiomesCompass biomesCompass) {
        this.biomesCompass = biomesCompass;
    }

    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        if (biomesCompass.getLocateBiomeMenu().contains(event.getClickedInventory())) return;
        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player player = (Player) event.getWhoClicked();
        player.closeInventory();
        biomesCompass.getLocateBiomeMenu().itemToBiome(clickedItem).ifPresent(biome -> new LocateBiomeTask(player, biome, biomesCompass));
    }
}
