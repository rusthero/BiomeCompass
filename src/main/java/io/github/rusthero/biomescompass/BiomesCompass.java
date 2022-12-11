package io.github.rusthero.biomescompass;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompass extends JavaPlugin implements Logging, CraftableItem {
    @Override
    public void onEnable() {
        getServer().addRecipe(getRecipe());

        info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        info("BiomesCompass is disabled");
    }

    @Override
    public void info(String msg) {
        getLogger().info(msg);
    }

    @Override
    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Biomes Compass");
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ShapedRecipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "biomes_compass"), getItem());

        recipe.shape("SLS", "LCL", "SLS");

        recipe.setIngredient('S', Material.OAK_SAPLING);
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('C', Material.COMPASS);

        return recipe;
    }
}