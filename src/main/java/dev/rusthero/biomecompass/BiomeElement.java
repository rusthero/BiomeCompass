package dev.rusthero.biomecompass;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;
import static org.bukkit.World.Environment;

/**
 * This enum represents biomes with an item version, colored name, dimension, and flags. Instead of using the default
 * Biome class from the Bukkit API, this enum is used to add biomes that do not exist in the current API version but
 * will be accessed when constructing the BiomesMenu.
 * <p>
 * Note that the Material class is not used here to support future materials. Instead, the names of the materials are
 * used to generate materials at runtime.
 *
 * @see dev.rusthero.biomecompass.gui.BiomesMenu
 * @see dev.rusthero.biomecompass.items.BiomeCompassItem
 * @see dev.rusthero.biomecompass.locate.LocateBiomeQuery
 */
public enum BiomeElement {
    OCEAN(Environment.NORMAL, false, "SEAGRASS", DARK_BLUE),
    PLAINS(Environment.NORMAL, false, "GRASS_BLOCK", GREEN),
    DESERT(Environment.NORMAL, false, "SAND", YELLOW),
    WINDSWEPT_HILLS(Environment.NORMAL, false, "EMERALD_ORE", GRAY),
    FOREST(Environment.NORMAL, false, "OAK_LOG", GREEN),
    TAIGA(Environment.NORMAL, false, "SPRUCE_LOG", DARK_GREEN),
    SWAMP(Environment.NORMAL, false, "LILY_PAD", DARK_GREEN),
    MANGROVE_SWAMP(Environment.NORMAL, false, "MANGROVE_LOG", GREEN),
    RIVER(Environment.NORMAL, false, "SUGAR_CANE", BLUE),
    FROZEN_OCEAN(Environment.NORMAL, false, "BLUE_ICE", AQUA),
    FROZEN_RIVER(Environment.NORMAL, false, "ICE", AQUA),
    SNOWY_PLAINS(Environment.NORMAL, false, "SNOW", WHITE),
    MUSHROOM_FIELDS(Environment.NORMAL, false, "MYCELIUM", LIGHT_PURPLE),
    BEACH(Environment.NORMAL, false, "SAND", YELLOW),
    JUNGLE(Environment.NORMAL, false, "JUNGLE_LOG", DARK_GREEN),
    SPARSE_JUNGLE(Environment.NORMAL, false, "JUNGLE_LOG", DARK_GREEN),
    DEEP_OCEAN(Environment.NORMAL, false, "KELP", DARK_BLUE),
    STONY_SHORE(Environment.NORMAL, false, "STONE", DARK_GRAY),
    SNOWY_BEACH(Environment.NORMAL, false, "SNOW", WHITE),
    BIRCH_FOREST(Environment.NORMAL, false, "BIRCH_LOG", GRAY),
    DARK_FOREST(Environment.NORMAL, false, "DARK_OAK_LOG", DARK_RED),
    SNOWY_TAIGA(Environment.NORMAL, false, "SNOW", WHITE),
    OLD_GROWTH_PINE_TAIGA(Environment.NORMAL, false, "PODZOL", DARK_RED),
    WINDSWEPT_FOREST(Environment.NORMAL, false, "EMERALD_ORE", GREEN),
    SAVANNA(Environment.NORMAL, false, "ACACIA_LOG", YELLOW),
    SAVANNA_PLATEAU(Environment.NORMAL, false, "ACACIA_LOG", YELLOW),
    BADLANDS(Environment.NORMAL, false, "RED_SAND", RED),
    WOODED_BADLANDS(Environment.NORMAL, false, "COARSE_DIRT", RED),
    WARM_OCEAN(Environment.NORMAL, false, "FIRE_CORAL_BLOCK", AQUA),
    LUKEWARM_OCEAN(Environment.NORMAL, false, "HORN_CORAL_BLOCK", DARK_AQUA),
    COLD_OCEAN(Environment.NORMAL, false, "BUBBLE_CORAL_BLOCK", BLUE),
    DEEP_LUKEWARM_OCEAN(Environment.NORMAL, false, "BRAIN_CORAL_BLOCK", BLUE),
    DEEP_COLD_OCEAN(Environment.NORMAL, false, "TUBE_CORAL_BLOCK", DARK_BLUE),
    DEEP_FROZEN_OCEAN(Environment.NORMAL, false, "PACKED_ICE", AQUA),
    SUNFLOWER_PLAINS(Environment.NORMAL, false, "SUNFLOWER", YELLOW),
    WINDSWEPT_GRAVELLY_HILLS(Environment.NORMAL, false, "GRAVEL", GRAY),
    FLOWER_FOREST(Environment.NORMAL, false, "ROSE_BUSH", GREEN),
    ICE_SPIKES(Environment.NORMAL, false, "PACKED_ICE", BLUE),
    OLD_GROWTH_BIRCH_FOREST(Environment.NORMAL, false, "BIRCH_LOG", GRAY),
    OLD_GROWTH_SPRUCE_TAIGA(Environment.NORMAL, false, "SPRUCE_LOG", DARK_GREEN),
    WINDSWEPT_SAVANNA(Environment.NORMAL, false, "COARSE_DIRT", DARK_GRAY),
    ERODED_BADLANDS(Environment.NORMAL, false, "TERRACOTTA", RED),
    BAMBOO_JUNGLE(Environment.NORMAL, false, "BAMBOO", GREEN),
    MEADOW(Environment.NORMAL, false, "BEE_NEST", GREEN),
    GROVE(Environment.NORMAL, false, "SNOW_BLOCK", WHITE),
    SNOWY_SLOPES(Environment.NORMAL, false, "SNOW_BLOCK", WHITE),
    FROZEN_PEAKS(Environment.NORMAL, false, "ICE", BLUE),
    JAGGED_PEAKS(Environment.NORMAL, false, "SNOW_BLOCK", WHITE),
    STONY_PEAKS(Environment.NORMAL, false, "STONE", DARK_GRAY),

    DRIPSTONE_CAVES(Environment.NORMAL, true, "POINTED_DRIPSTONE", RED),
    LUSH_CAVES(Environment.NORMAL, true, "MOSS_BLOCK", GREEN),
    DEEP_DARK(Environment.NORMAL, true, "SCULK", BLACK),

    NETHER_WASTES(Environment.NETHER, false, "NETHERRACK", RED),
    SOUL_SAND_VALLEY(Environment.NETHER, false, "SOUL_SAND", DARK_PURPLE),
    CRIMSON_FOREST(Environment.NETHER, false, "CRIMSON_NYLIUM", DARK_RED),
    WARPED_FOREST(Environment.NETHER, false, "WARPED_NYLIUM", DARK_AQUA),
    BASALT_DELTAS(Environment.NETHER, false, "BASALT", DARK_GRAY),

    THE_END(Environment.THE_END, false, "OBSIDIAN", DARK_PURPLE),
    SMALL_END_ISLANDS(Environment.THE_END, false, "END_STONE", DARK_PURPLE),
    END_MIDLANDS(Environment.THE_END, false, "END_STONE", DARK_PURPLE),
    END_HIGHLANDS(Environment.THE_END, false, "CHORUS_PLANT", DARK_PURPLE),
    END_BARRENS(Environment.THE_END, false, "END_STONE", DARK_PURPLE);

    // https://minecraft.fandom.com/wiki/The_Void - Not useful for normal dimensions so commented.
    // THE_VOID(Environment.NORMAL, false, "STONE", BLACK);

    /**
     * Returns the BiomeElement associated with the given ItemStack, if one exists.
     *
     * @param item The ItemStack to search for.
     * @return An Optional containing the BiomeElement whose item matches the given ItemStack, if one exists. An
     * empty Optional is returned if no matching biome is found.
     */
    public static Optional<BiomeElement> getByItemStack(ItemStack item) {
        return Arrays.stream(values()).filter(element -> element.item.equals(item)).findFirst();
    }

    /**
     * The display name for the element, which can be colored. This name is used for renaming the compass and item
     * names in the biomes menu.
     *
     * @see dev.rusthero.biomecompass.gui.BiomesMenu
     * @see dev.rusthero.biomecompass.items.BiomeCompassItem#bindLocation(String, Location)
     */
    public final String displayName;

    /**
     * A flag indicating whether the biome generates underground. This is useful for biome locating.
     *
     * @see dev.rusthero.biomecompass.locate.LocateBiomeQuery#fetch
     */
    public final boolean isUnderground;

    /**
     * The dimension in which the biome generates.
     */
    public final Environment environment;

    /**
     * The ItemStack representation of the biome. This is used in the BiomesMenu.
     */
    public final ItemStack item;

    /**
     * Constructs a new BiomeElement with the given environment, underground flag, material name, and chat color.
     * The material name is used to generate the corresponding Material at runtime, allowing the use of future
     * materials without changing the API version or using hacks. The enum name is formatted and set as the display
     * name, which is also used to set the display name of the ItemStack representation of the biome.
     *
     * @param environment   The dimension in which the biome exists.
     * @param isUnderground A flag indicating whether the biome generates underground.
     * @param materialStr   The name of the material to use for the ItemStack representation of
     *                      the biome.
     * @param color         The chat color to use for the display name of the biome.
     */
    BiomeElement(Environment environment, boolean isUnderground, String materialStr, ChatColor color) {
        this.environment = environment;
        this.isUnderground = isUnderground;

        // To support future versions, we use the names of materials as strings instead of the Material class. This
        // allows new biomes and materials to be used without changing the API version or using hacks.
        Material material;
        try {
            material = Material.valueOf(materialStr);
        } catch (IllegalArgumentException exception) {
            material = Material.END_GATEWAY;
        }

        // Format the enum name: NETHER_WASTES => Nether Wastes
        displayName = color + Arrays.stream(this.name().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // This for suppressing IDE warning, it cannot be null.
        if (meta == null) return;

        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
    }
}
