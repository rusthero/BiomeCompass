package io.github.rusthero.biomescompass.listener;

import io.github.rusthero.biomescompass.gui.LocateBiomeMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

public class MenuDragListener implements Listener {
    private final LocateBiomeMenu menu;

    public MenuDragListener(final LocateBiomeMenu menu) {
        this.menu = menu;
    }

    @EventHandler
    private void onMenuDrag(final InventoryDragEvent event) {
        event.setCancelled(menu.contains(event.getInventory()));
    }
}
