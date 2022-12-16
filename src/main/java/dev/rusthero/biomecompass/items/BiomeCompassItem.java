package dev.rusthero.biomecompass.items;

import dev.rusthero.biomecompass.BiomeCompass;
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

public class BiomeCompassItem {
    public static ShapedRecipe getRecipe(BiomeCompass biomeCompass) {
        NamespacedKey key = new NamespacedKey(biomeCompass, "biome_compass");
        ShapedRecipe recipe = new ShapedRecipe(key, BiomeCompassItem.getDefault());

        recipe.shape("WEB", "DCS", "PNL");

        recipe.setIngredient('W', Material.WATER_BUCKET);
        recipe.setIngredient('E', Material.END_STONE);
        recipe.setIngredient('B', Material.SNOWBALL);

        recipe.setIngredient('D', Material.DIRT);
        recipe.setIngredient('C', Material.COMPASS);
        recipe.setIngredient('S', Material.SAND);

        recipe.setIngredient('P', Material.POPPY);
        recipe.setIngredient('N', Material.NETHERRACK);
        recipe.setIngredient('L', Material.LAVA_BUCKET);

        return recipe;
    }

    private static ItemStack biomeCompassItem;

    private static ItemStack getDefault() {
        if (biomeCompassItem == null) {
            biomeCompassItem = new ItemStack(Material.COMPASS, 1);

            ItemMeta meta = biomeCompassItem.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GREEN + "Biome Compass");
            biomeCompassItem.setItemMeta(meta);
        }

        return biomeCompassItem;
    }

    public static void ifInstance(ItemStack item, Consumer<BiomeCompassItem> consumer) {
        if (!isInstance(item)) return;

        consumer.accept(new BiomeCompassItem(item));
    }

    public static boolean isInstance(ItemStack item) {
        if (!item.getType().equals(getDefault().getType())) return false;
        if (!item.hasItemMeta() || item.getItemMeta() == null) return false;

        return item.getItemMeta().getDisplayName().startsWith(getDefault().getItemMeta().getDisplayName());
    }

    final ItemStack item;

    private BiomeCompassItem(ItemStack item) {
        this.item = item;
    }

    public void bindLocation(String biomeName, Location location) {
        CompassMeta meta = (CompassMeta) item.getItemMeta();

        meta.setDisplayName(
                ChatColor.DARK_GREEN + "Biome Compass (" + biomeName + ChatColor.DARK_GREEN + ")");

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_PURPLE + "Location: " + location.getBlockX() + ", " + location.getBlockY() + ", " +
                         location.getBlockZ());
        meta.setLore(lore);

        meta.setLodestoneTracked(false);
        meta.setLodestone(location);

        item.setItemMeta(meta);
    }
}
