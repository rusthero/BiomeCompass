package io.github.rusthero.biomescompass.items;

import io.github.rusthero.biomescompass.BiomesCompass;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.function.Consumer;

public class BiomesCompassItem {
    public static ShapedRecipe getRecipe(BiomesCompass biomesCompass) {
        ShapedRecipe recipe =
                new ShapedRecipe(new NamespacedKey(biomesCompass, "biomes_compass"),
                                 BiomesCompassItem.getDefault());

        recipe.shape("SLS", "LCL", "SLS");

        recipe.setIngredient('S', Material.OAK_SAPLING);
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('C', Material.COMPASS);

        return recipe;
    }

    private static ItemStack biomesCompassItem;

    private static ItemStack getDefault() {
        if (biomesCompassItem == null) {
            biomesCompassItem = new ItemStack(Material.COMPASS, 1);

            ItemMeta meta = biomesCompassItem.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GREEN + "Biomes Compass");
            biomesCompassItem.setItemMeta(meta);
        }

        return biomesCompassItem;
    }

    public static void ifInstance(ItemStack item, Consumer<BiomesCompassItem> consumer) {
        if (!isInstance(item)) return;

        consumer.accept(new BiomesCompassItem(item));
    }

    public static boolean isInstance(ItemStack item) {
        if (!item.getType().equals(getDefault().getType())) return false;
        if (!item.hasItemMeta() || item.getItemMeta() == null) return false;

        return item.getItemMeta().getDisplayName().startsWith(getDefault().getItemMeta().getDisplayName());
    }

    final ItemStack item;

    private BiomesCompassItem(ItemStack item) {
        this.item = item;
    }

    public void bindLocation(String biomeName, Location location) {
        CompassMeta meta = (CompassMeta) item.getItemMeta();

        meta.setDisplayName(
                ChatColor.DARK_GREEN + "Biomes Compass (" + biomeName + ChatColor.DARK_GREEN + ")");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_PURPLE + "Location: " + location.getBlockX() + ", " + location.getBlockY() + ", " +
                         location.getBlockZ());
        meta.setLore(lore);

        meta.setLodestoneTracked(false);
        meta.setLodestone(location);

        item.setItemMeta(meta);
    }
}
