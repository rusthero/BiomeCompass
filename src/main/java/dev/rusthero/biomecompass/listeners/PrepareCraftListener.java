package dev.rusthero.biomecompass.listeners;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.items.BiomeCompassItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

public class PrepareCraftListener implements Listener {
    @EventHandler
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        Recipe recipe = event.getRecipe();
        if (!(recipe instanceof ShapedRecipe)) return;
        if (!BiomeCompassItem.isInstance(recipe.getResult())) return;

        if (!event.getView().getPlayer().hasPermission("biomecompass.craft"))
            event.getInventory().setResult(new ItemStack(Material.AIR));
    }
}
