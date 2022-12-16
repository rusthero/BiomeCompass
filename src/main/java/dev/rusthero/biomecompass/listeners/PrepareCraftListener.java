package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.items.BiomeCompassItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Represents a listener for the preparation of a crafting event for the Biome Compass. This listener checks if the
 * player has the required permission "biomecompass.craft" to craft the item. If the player does not have the
 * required permission, the result of the crafting event will be set to an empty item stack.
 */
public class PrepareCraftListener implements Listener {
    /**
     * Handles the preparation of a crafting event for the Biome Compass.
     *
     * @param event The {@link PrepareItemCraftEvent} that was fired.
     */
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        // Only proceed if the recipe is a shaped recipe.
        Recipe recipe = event.getRecipe();
        if (!(recipe instanceof ShapedRecipe)) return;
        ItemStack result = recipe.getResult();

        // Check if the recipe result is a Biome Compass.
        if (!BiomeCompassItem.isInstance(result)) return;

        // Set result empty if player does not have the permission.
        Player player = (Player) event.getView().getPlayer();
        if (!player.hasPermission("biomecompass.craft")) event.getInventory().setResult(null);
    }
}
