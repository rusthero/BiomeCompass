package io.github.rusthero.biomecompass.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.bukkit.ChatColor.*;
import static org.bukkit.World.Environment.NETHER;
import static org.bukkit.World.Environment.NORMAL;

public enum BiomeElement {
    OCEAN(NORMAL, false, "WATER", DARK_BLUE),
    PLAINS(NORMAL, false, "GRASS_BLOCK", GREEN),
    DESERT(NORMAL, false, "SAND", YELLOW),
    WINDSWEPT_HILLS(NORMAL, false, "EMERALD_ORE", GRAY),
    FOREST(NORMAL, false, "OAK_LOG", GREEN),
    TAIGA(NORMAL, false, "SPRUCE_LOG", DARK_GREEN),
    SWAMP(NORMAL, false, "LILY_PAD", DARK_GREEN),
    MANGROVE_SWAMP(NORMAL, false, "MANGROVE_LOG", GREEN),
    RIVER(NORMAL, false, "WATER", BLUE),
    FROZEN_OCEAN(NORMAL, false, "BLUE_ICE", AQUA),
    FROZEN_RIVER(NORMAL, false, "ICE", AQUA),
    SNOWY_PLAINS(NORMAL, false, "SNOW", WHITE),
    MUSHROOM_FIELDS(NORMAL, false, "MYCELIUM", LIGHT_PURPLE),
    BEACH(NORMAL, false, "SAND", YELLOW),
    JUNGLE(NORMAL, false, "JUNGLE_LOG", DARK_GREEN),
    SPARSE_JUNGLE(NORMAL, false, "JUNGLE_LOG", DARK_GREEN),
    DEEP_OCEAN(NORMAL, false, "WATER", DARK_BLUE),
    STONY_SHORE(NORMAL, false, "STONE", DARK_GRAY),
    SNOWY_BEACH(NORMAL, false, "SNOW", WHITE),
    BIRCH_FOREST(NORMAL, false, "BIRCH_LOG", GRAY),
    DARK_FOREST(NORMAL, false, "DARK_OAK_LOG", DARK_RED),
    SNOWY_TAIGA(NORMAL, false, "SNOW", WHITE),
    OLD_GROWTH_PINE_TAIGA(NORMAL, false, "PODZOL", DARK_RED),
    WINDSWEPT_FOREST(NORMAL, false, "EMERALD_ORE", GREEN),
    SAVANNA(NORMAL, false, "ACACIA_LOG", YELLOW),
    SAVANNA_PLATEAU(NORMAL, false, "ACACIA_LOG", YELLOW),
    BADLANDS(NORMAL, false, "RED_SAND", RED),
    WOODED_BADLANDS(NORMAL, false, "COARSE_DIRT", RED),
    WARM_OCEAN(NORMAL, false, "WATER", AQUA),
    LUKEWARM_OCEAN(NORMAL, false, "WATER", DARK_AQUA),
    COLD_OCEAN(NORMAL, false, "WATER", BLUE),
    DEEP_LUKEWARM_OCEAN(NORMAL, false, "WATER", BLUE),
    DEEP_COLD_OCEAN(NORMAL, false, "WATER", DARK_BLUE),
    DEEP_FROZEN_OCEAN(NORMAL, false, "PACKED_ICE", AQUA),
    SUNFLOWER_PLAINS(NORMAL, false, "SUNFLOWER", YELLOW),
    WINDSWEPT_GRAVELLY_HILLS(NORMAL, false, "GRAVEL", GRAY),
    FLOWER_FOREST(NORMAL, false, "ROSE_BUSH", GREEN),
    ICE_SPIKES(NORMAL, false, "PACKED_ICE", BLUE),
    OLD_GROWTH_BIRCH_FOREST(NORMAL, false, "BIRCH_LOG", GRAY),
    OLD_GROWTH_SPRUCE_TAIGA(NORMAL, false, "SPRUCE_LOG", DARK_GREEN),
    WINDSWEPT_SAVANNA(NORMAL, false, "COARSE_DIRT", DARK_GRAY),
    ERODED_BADLANDS(NORMAL, false, "TERRACOTTA", RED),
    BAMBOO_JUNGLE(NORMAL, false, "BAMBOO", GREEN),
    MEADOW(NORMAL, false, "BEE_NEST", GREEN),
    GROVE(NORMAL, false, "POWDER_SNOW", WHITE),
    SNOWY_SLOPES(NORMAL, false, "POWDER_SNOW", WHITE),
    FROZEN_PEAKS(NORMAL, false, "ICE", BLUE),
    JAGGED_PEAKS(NORMAL, false, "SNOW", WHITE),
    STONY_PEAKS(NORMAL, false, "STONE", DARK_GRAY),

    DRIPSTONE_CAVES(NORMAL, true, "POINTED_DRIPSTONE", RED),
    LUSH_CAVES(NORMAL, true, "MOSS_BLOCK", GREEN),
    DEEP_DARK(NORMAL, true, "SCULK", BLACK),

    NETHER_WASTES(NETHER, false, "NETHERRACK", RED),
    SOUL_SAND_VALLEY(NETHER, false, "SOUL_SAND", DARK_PURPLE),
    CRIMSON_FOREST(NETHER, false, "CRIMSON_NYLIUM", DARK_RED),
    WARPED_FOREST(NETHER, false, "WARPED_NYLIUM", DARK_AQUA),
    BASALT_DELTAS(NETHER, false, "BASALT", DARK_GRAY),

    THE_END(World.Environment.THE_END, false, "OBSIDIAN", DARK_PURPLE),
    SMALL_END_ISLANDS(World.Environment.THE_END, false, "END_STONE", DARK_PURPLE),
    END_MIDLANDS(World.Environment.THE_END, false, "END_STONE_BRICKS", DARK_PURPLE),
    END_HIGHLANDS(World.Environment.THE_END, false, "CHORUS_PLANT", DARK_PURPLE),
    END_BARRENS(World.Environment.THE_END, false, "END_STONE", DARK_PURPLE),

    THE_VOID(NORMAL, false, "STONE", BLACK);

    public static Optional<BiomeElement> getByItemStack(ItemStack item) {
        return Arrays.stream(values()).filter(element -> element.item.equals(item)).findFirst();
    }

    public final String displayName;

    final World.Environment environment;
    final boolean isUnderground;
    final ItemStack item;

    BiomeElement(final World.Environment environment, final boolean isUnderground, final String material,
                 final ChatColor color) {
        this.environment = environment;
        this.isUnderground = isUnderground;
        item = new ItemStack(Material.valueOf(material.toUpperCase()), 1);

        final ItemMeta meta = item.getItemMeta();
        displayName = color + Arrays.stream(this.name().split("_"))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
    }
}
