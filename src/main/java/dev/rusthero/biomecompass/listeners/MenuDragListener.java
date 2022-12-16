package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.gui.BiomesMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

/**
 * A listener class that prevents players from dragging items from the menu.
 */
public class MenuDragListener implements Listener {
    /**
     * The instance of the {@link BiomesMenu}.
     */
    private final BiomesMenu menu;

    /**
     * Constructs a new instance of the {@link MenuDragListener} class.
     *
     * @param menu The instance of the {@link BiomesMenu}.
     */
    public MenuDragListener(final BiomesMenu menu) {
        this.menu = menu;
    }

    /**
     * Handles the {@link InventoryDragEvent} that is triggered when a player tries to drag an item in the menu.
     * If the dragged item is in the menu, the event is cancelled to prevent the item from being dragged.
     *
     * @param event The {@link InventoryDragEvent} that was triggered.
     */
    @EventHandler
    private void onMenuDrag(final InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        boolean isMenu = menu.contains(inventory);
        event.setCancelled(isMenu);
    }
}
