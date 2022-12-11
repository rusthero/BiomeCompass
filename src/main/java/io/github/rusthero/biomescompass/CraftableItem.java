package io.github.rusthero.biomescompass;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public interface CraftableItem {
    ItemStack getItem();

    ShapedRecipe getRecipe();
}
