package io.github.rusthero.biomescompass.listeners;

import io.github.rusthero.biomescompass.gui.BiomesMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuDragListener implements Listener {
    private final BiomesMenu menu;

    public MenuDragListener(final BiomesMenu menu) {
        this.menu = menu;
    }

    @EventHandler
    private void onMenuDrag(final InventoryDragEvent event) {
        event.setCancelled(menu.contains(event.getInventory()));
    }
}
