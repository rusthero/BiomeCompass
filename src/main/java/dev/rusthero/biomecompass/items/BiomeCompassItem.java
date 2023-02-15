package dev.rusthero.biomecompass.items;

import dev.rusthero.biomecompass.BiomeCompass;
import dev.rusthero.biomecompass.lang.Field;
import dev.rusthero.biomecompass.lang.Language;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Consumer;

import static java.lang.String.format;
import static org.bukkit.Material.*;

/**
 * This class represents a Biome Compass item. It includes the recipe for creating the compass and a default
 * ItemStack for comparison and obtaining. It does not extend the ItemStack class, but it can hold one to call
 * methods on it.
 */
public class BiomeCompassItem {
    /**
     * The constant display name for the Biome Compass item. This is useful for comparing and creating new compasses.
     *
     * @Deprecated This value is used for backwards compatibility and should not be referred.
     */
    public static final String DISPLAY_NAME = "§3Biome Compass";

    /**
     * The default ItemStack form of the Biome Compass.
     */
    private static ItemStack defaultItem;

    /**
     * Returns the default ItemStack form of the Biome Compass. This method is useful for generating new instances of
     * the Biome Compass, and should not be used for comparing existing ItemStacks. Instead, use the
     * {@link BiomeCompassItem#isInstance(ItemStack, Language, BiomeCompass)} and
     * {@link BiomeCompassItem#ifInstance(ItemStack, Language, BiomeCompass, Consumer)}
     * methods for this purpose.
     *
     * @return The default ItemStack form of the Biome Compass.
     */
    public static ItemStack getDefault(BiomeCompass plugin, Language language) {
        if (defaultItem != null) return defaultItem;
        // If it is the first time being called, register it. We do this to avoid generating the ItemStack every time.
        defaultItem = new ItemStack(COMPASS, 1);

        ItemMeta meta = defaultItem.getItemMeta();

        // This for suppressing IDE warning, it cannot be null.
        if (meta == null) return defaultItem;

        meta.setDisplayName("§3" + language.getString(Field.ITEM_BIOME_COMPASS_DISPLAY_NAME));
        meta.setCustomModelData(plugin.getSettings().customModelData);
        NamespacedKey isBiomeCompass = new NamespacedKey(plugin, "is_biome_compass");
        meta.getPersistentDataContainer().set(isBiomeCompass, PersistentDataType.BYTE, (byte) 1);
        defaultItem.setItemMeta(meta);

        return defaultItem;
    }

    /**
     * Returns a shaped recipe for crafting the Biome Compass.
     *
     * @param key the namespaced key to uniquely identify the recipe and avoid collisions
     * @return the shaped recipe for crafting the Biome Compass
     */
    public static ShapedRecipe getRecipe(NamespacedKey key, Language language, BiomeCompass plugin) {
        ShapedRecipe recipe = new ShapedRecipe(key, BiomeCompassItem.getDefault(plugin, language));
        recipe.shape("WEB", "DCS", "PNL");
        recipe.setIngredient('W', WATER_BUCKET);
        recipe.setIngredient('E', END_STONE);
        recipe.setIngredient('B', SNOWBALL);
        recipe.setIngredient('D', DIRT);
        recipe.setIngredient('C', COMPASS);
        recipe.setIngredient('S', STONE);
        recipe.setIngredient('P', POPPY);
        recipe.setIngredient('N', NETHERRACK);
        recipe.setIngredient('L', LAVA_BUCKET);
        return recipe;
    }

    /**
     * Accepts a consumer if the given item is a valid instance of a Biome Compass.
     *
     * @param item     The item to check.
     * @param consumer The consumer to accept the Biome Compass item if it is an instance.
     * @see #isInstance(ItemStack, Language, BiomeCompass)
     */
    public static void ifInstance(ItemStack item, Language language,
                                  BiomeCompass plugin, Consumer<BiomeCompassItem> consumer) {
        if (isInstance(item, language, plugin)) consumer.accept(new BiomeCompassItem(item));
    }

    /**
     * Determines if a given item is a valid instance of a Biome Compass. This is determined by checking the item's
     * type and display name.
     *
     * @param item The item to check.
     * @return {@code true} if the item is a valid Biome Compass, {@code false} otherwise.
     */
    public static boolean isInstance(ItemStack item, Language language, BiomeCompass plugin) {
        if (item.getType() != getDefault(plugin, language).getType()) return false;
        if (!item.hasItemMeta()) return false;

        // These checks are used to suppress any potential null value warnings from the IDE.
        if (getDefault(plugin, language).getItemMeta() == null || item.getItemMeta() == null) return false;

        String name = item.getItemMeta().getDisplayName();

        return name.startsWith(DISPLAY_NAME) ||
                name.endsWith(language.getString(Field.ITEM_BIOME_COMPASS_DISPLAY_NAME)) ||
                hasBiomeCompassData(item.getItemMeta(), plugin);
    }

    private static boolean hasBiomeCompassData(ItemMeta meta, BiomeCompass plugin) {
        NamespacedKey isBiomeCompass = new NamespacedKey(plugin, "is_biome_compass");
        byte isUpdated = meta
                .getPersistentDataContainer()
                .getOrDefault(isBiomeCompass, PersistentDataType.BYTE, (byte) 0);
        return isUpdated == (byte) 1;
    }

    /**
     * ItemStack instance of the Biome Compass. This instance can be used to update the location and name of the
     * compass.
     *
     * @see #bindLocation
     */
    final ItemStack item;

    /**
     * Constructs a new instance of the Biome Compass item. The access level is private because it is important to
     * ensure that the provided item is a valid Biome Compass before constructing the object. It is recommended to use
     * {@link #ifInstance(ItemStack, Language, BiomeCompass, Consumer)} to construct this object.
     *
     * @param item the ItemStack representation of the Biome Compass
     */
    private BiomeCompassItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Binds a location to the Biome Compass with a biome name. Updates compass pointing and display name.
     *
     * @param biomeName Name of the biome, can be colored. It will be added to the display name of the compass.
     * @param location  Location which the compass will point to, y level does not matter.
     */
    public void bindLocation(String biomeName, Language language, Location location, BiomeCompass plugin) {
        CompassMeta meta = (CompassMeta) item.getItemMeta();

        // This for suppressing IDE warnings, they cannot be null.
        if (meta == null || defaultItem.getItemMeta() == null) return;

        // Write name of the located biome on the compass in this format: Biome Compass (Plains)
        String name = "§3" + language.getString(Field.ITEM_BIOME_COMPASS_DISPLAY_NAME);
        name += format(" §6(%s§6)", biomeName);
        meta.setDisplayName(name);

        // We want compass to point a location however do not want to put an actual lodestone so tracked set to false.
        meta.setLodestoneTracked(false);
        meta.setLodestone(location);

        // Update the custom model data if it is not set, older items created before v1.2.0 may not have the data set.
        // This also is going to update the resource pack if changed from configuration
        if (!meta.hasCustomModelData() || meta.getCustomModelData() != plugin.getSettings().customModelData)
            meta.setCustomModelData(plugin.getSettings().customModelData);

        // Update the NBT tag to check if the item is Biome Compass. Older items created before v1.3.0 may not have it.
        if (!hasBiomeCompassData(meta, plugin)) {
            NamespacedKey isBiomeCompass = new NamespacedKey(plugin, "is_biome_compass");
            meta.getPersistentDataContainer().set(isBiomeCompass, PersistentDataType.BYTE, (byte) 1);
        }

        item.setItemMeta(meta);
    }
}
