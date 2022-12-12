package io.github.rusthero.biomescompass.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompassItem extends ItemStack {
    public BiomesCompassItem() {
        super(Material.COMPASS, 1);

        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Biomes Compass");
        setItemMeta(meta);
    }

    private boolean canCraft(CraftItemEvent event) {
        if (!event.getRecipe().getResult().equals(new BiomesCompassItem())) return false;
        if (!(event.getWhoClicked() instanceof Player player)) return false;

        return !player.hasPermission("biomescompass.craft");
    }

    public static ShapedRecipe getRecipe(JavaPlugin plugin) {
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin, "biomes_compass"), new BiomesCompassItem());

        recipe.shape("SLS", "LCL", "SLS");

        recipe.setIngredient('S', Material.OAK_SAPLING);
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('C', Material.COMPASS);

        return recipe;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ItemStack item)) return false;
        if (!item.hasItemMeta()) return false;

        return (getType().equals(item.getType()) && getItemMeta().equals(item.getItemMeta()));
    }
}
