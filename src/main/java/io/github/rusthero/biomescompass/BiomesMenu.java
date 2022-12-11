package io.github.rusthero.biomescompass;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Optional;

class BiomesMenu implements Listener {
    private final Inventory[] menu = new Inventory[2];
    private final HashMap<ItemStack, Biome> itemsToBiomes = new HashMap<>();

    BiomesMenu() {
        menu[0] = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "[1]" + ChatColor.DARK_GREEN + "Select a biome to lock your compass:");
        menu[1] = Bukkit.createInventory(null, 54, ChatColor.YELLOW + "[2]" + ChatColor.DARK_GREEN + "Select a biome to lock your compass:");

        // TODO Use configuration for adding biome items in a more dynamic way so the plugin supports future/newer versions
        itemsToBiomes.put(itemWithName(Material.KELP_PLANT, ChatColor.BLUE + "Ocean"), Biome.OCEAN);
        itemsToBiomes.put(itemWithName(Material.PRISMARINE, ChatColor.DARK_BLUE + "Deep Ocean"), Biome.DEEP_OCEAN);
        itemsToBiomes.put(itemWithName(Material.FIRE_CORAL, ChatColor.AQUA + "Warm Ocean"), Biome.WARM_OCEAN);
        itemsToBiomes.put(itemWithName(Material.BUBBLE_CORAL, ChatColor.BLUE + "Lukewarm Ocean"), Biome.LUKEWARM_OCEAN);
        itemsToBiomes.put(itemWithName(Material.TUBE_CORAL, ChatColor.DARK_AQUA + "Cold Ocean"), Biome.COLD_OCEAN);
        //itemsToBiomes.put(itemWithName(Material.FIRE_CORAL, ChatColor.AQUA + "Deep Warm Ocean"), Biome.DEEP_WARM_OCEAN);
        itemsToBiomes.put(itemWithName(Material.BUBBLE_CORAL, ChatColor.BLUE + "Deep Lukewarm Ocean"), Biome.DEEP_LUKEWARM_OCEAN);
        itemsToBiomes.put(itemWithName(Material.TUBE_CORAL, ChatColor.DARK_AQUA + "Deep Cold Ocean"), Biome.DEEP_COLD_OCEAN);
        itemsToBiomes.put(itemWithName(Material.BLUE_ICE, ChatColor.DARK_BLUE + "Deep Frozen Cold Ocean"), Biome.DEEP_FROZEN_OCEAN);
        itemsToBiomes.put(itemWithName(Material.WATER, ChatColor.DARK_AQUA + "River"), Biome.RIVER);
        itemsToBiomes.put(itemWithName(Material.PACKED_ICE, ChatColor.AQUA + "Frozen Ocean"), Biome.FROZEN_OCEAN);
        itemsToBiomes.put(itemWithName(Material.ICE, ChatColor.AQUA + "Frozen River"), Biome.FROZEN_RIVER);
        itemsToBiomes.put(itemWithName(Material.GRASS_BLOCK, ChatColor.GREEN + "Plains"), Biome.PLAINS);
        itemsToBiomes.put(itemWithName(Material.SUNFLOWER, ChatColor.YELLOW + "Sunflower Plains"), Biome.SUNFLOWER_PLAINS);
        itemsToBiomes.put(itemWithName(Material.OAK_LEAVES, ChatColor.DARK_GREEN + "Forest"), Biome.FOREST);
        //itemsToBiomes.put(itemWithName(Material.BIRCH_LEAVES, ChatColor.GREEN + "Tall Birch Forests"), Biome.TALL_BIRCH_FOREST);
        //itemsToBiomes.put(itemWithName(Material.BIRCH_LOG, ChatColor.GREEN + "Tall Birch Hills"), Biome.TALL_BIRCH_HILLS);
        itemsToBiomes.put(itemWithName(Material.BIRCH_LOG, ChatColor.GRAY + "Birch Forest"), Biome.BIRCH_FOREST);
        //itemsToBiomes.put(itemWithName(Material.BIRCH_LEAVES, ChatColor.GRAY + "Birch Forest Hills"), Biome.BIRCH_FOREST_HILLS);
        itemsToBiomes.put(itemWithName(Material.LILY_PAD, ChatColor.DARK_GREEN + "Swamp"), Biome.SWAMP);
        //itemsToBiomes.put(itemWithName(Material.VINE, ChatColor.DARK_GREEN + "Swamp Hills"), Biome.SWAMP_HILLS);
        itemsToBiomes.put(itemWithName(Material.JUNGLE_LOG, ChatColor.DARK_GREEN + "Jungle"), Biome.JUNGLE);
        //itemsToBiomes.put(itemWithName(Material.JUNGLE_LEAVES, ChatColor.DARK_GREEN + "Jungle Hills"), Biome.JUNGLE_HILLS);
        //itemsToBiomes.put(itemWithName(Material.COCOA_BEANS, ChatColor.DARK_GREEN + "Modified Jungle"), Biome.MODIFIED_JUNGLE);
        //itemsToBiomes.put(itemWithName(Material.JUNGLE_LEAVES, ChatColor.DARK_GREEN + "Modified Jungle Edge"), Biome.MODIFIED_JUNGLE_EDGE);
        //itemsToBiomes.put(itemWithName(Material.VINE, ChatColor.DARK_GREEN + "Jungle Edge"), Biome.JUNGLE_EDGE);
        itemsToBiomes.put(itemWithName(Material.ACACIA_LOG, ChatColor.YELLOW + "Savanna"), Biome.SAVANNA);
        itemsToBiomes.put(itemWithName(Material.TALL_GRASS, ChatColor.YELLOW + "Savanna Plateau"), Biome.SAVANNA_PLATEAU);
        //itemsToBiomes.put(itemWithName(Material.ACACIA_LEAVES, ChatColor.YELLOW + "Shattered Savanna"), Biome.SHATTERED_SAVANNA);
        //itemsToBiomes.put(itemWithName(Material.ACACIA_LEAVES, ChatColor.YELLOW + "Shattered Savanna Plateau"), Biome.SHATTERED_SAVANNA_PLATEAU);
        itemsToBiomes.put(itemWithName(Material.ROSE_BUSH, ChatColor.RED + "Flower Forest"), Biome.FLOWER_FOREST);
        itemsToBiomes.put(itemWithName(Material.DARK_OAK_LOG, ChatColor.DARK_RED + "Dark Forest"), Biome.DARK_FOREST);
        //itemsToBiomes.put(itemWithName(Material.RED_MUSHROOM_BLOCK, ChatColor.DARK_RED + "Dark Forest Hills"), Biome.DARK_FOREST_HILLS);
        //itemsToBiomes.put(itemWithName(Material.EMERALD_ORE, ChatColor.GRAY + "Mountains"), Biome.MOUNTAINS);
        //itemsToBiomes.put(itemWithName(Material.STONE, ChatColor.DARK_GRAY + "Mountain Edge"), Biome.MOUNTAIN_EDGE);
        //itemsToBiomes.put(itemWithName(Material.STONE, ChatColor.DARK_GRAY + "Stony Shore"), Biome.STONE_SHORE);
        //itemsToBiomes.put(itemWithName(Material.EMERALD_ORE, ChatColor.DARK_GRAY + "Gravelly Mountains"), Biome.GRAVELLY_MOUNTAINS);
        //itemsToBiomes.put(itemWithName(Material.EMERALD_ORE, ChatColor.DARK_GRAY + "Modified Gravelly Mountains"), Biome.MODIFIED_GRAVELLY_MOUNTAINS);
        itemsToBiomes.put(itemWithName(Material.CACTUS, ChatColor.GOLD + "Desert"), Biome.DESERT);
        //itemsToBiomes.put(itemWithName(Material.DEAD_BUSH, ChatColor.GOLD + "Desert Hills"), Biome.DESERT_HILLS);
        //itemsToBiomes.put(itemWithName(Material.SAND, ChatColor.YELLOW + "Desert Lakes"), Biome.DESERT_LAKES);
        itemsToBiomes.put(itemWithName(Material.SAND, ChatColor.YELLOW + "Beach"), Biome.BEACH);
        //itemsToBiomes.put(itemWithName(Material.SNOW, ChatColor.WHITE + "Snowy Tundra/Plains"), Biome.SNOWY_TUNDRA);
        //itemsToBiomes.put(itemWithName(Material.SNOW, ChatColor.WHITE + "Snowy Mountains"), Biome.SNOWY_MOUNTAINS);
        itemsToBiomes.put(itemWithName(Material.SNOWBALL, ChatColor.WHITE + "Snowy Beach"), Biome.SNOWY_BEACH);
        //itemsToBiomes.put(itemWithName(Material.GRASS, ChatColor.WHITE + "Snowy Taiga Mountains"), Biome.SNOWY_TAIGA_MOUNTAINS);
        itemsToBiomes.put(itemWithName(Material.FERN, ChatColor.WHITE + "Snowy Taiga"), Biome.SNOWY_TAIGA);
        //itemsToBiomes.put(itemWithName(Material.SNOW, ChatColor.WHITE + "Snowy Taiga Hills"), Biome.SNOWY_TAIGA_HILLS);
        itemsToBiomes.put(itemWithName(Material.PACKED_ICE, ChatColor.BLUE + "Ice Spikes"), Biome.ICE_SPIKES);
        //itemsToBiomes.put(itemWithName(Material.PEONY, ChatColor.GREEN + "Wooded Hills"), Biome.WOODED_HILLS);
        //itemsToBiomes.put(itemWithName(Material.SPRUCE_LEAVES, ChatColor.DARK_GREEN + "Wooded Mountains"), Biome.WOODED_MOUNTAINS);
        itemsToBiomes.put(itemWithName(Material.SPRUCE_LOG, ChatColor.DARK_GREEN + "Taiga"), Biome.TAIGA);
        //itemsToBiomes.put(itemWithName(Material.PODZOL, ChatColor.DARK_GREEN + "Taiga Hills"), Biome.TAIGA_HILLS);
        //itemsToBiomes.put(itemWithName(Material.COARSE_DIRT, ChatColor.DARK_GREEN + "Giant Tree Taiga"), Biome.GIANT_TREE_TAIGA);
        //itemsToBiomes.put(itemWithName(Material.LARGE_FERN, ChatColor.DARK_GREEN + "Giant Tree Taiga Hills"), Biome.GIANT_TREE_TAIGA_HILLS);
        //itemsToBiomes.put(itemWithName(Material.SPRUCE_LEAVES, ChatColor.DARK_GREEN + "Taiga Mountains"), Biome.TAIGA_MOUNTAINS);
        //itemsToBiomes.put(itemWithName(Material.SPRUCE_LOG, ChatColor.DARK_GREEN + "Giant Spruce Taiga"), Biome.GIANT_SPRUCE_TAIGA);
        //itemsToBiomes.put(itemWithName(Material.SPRUCE_LEAVES, ChatColor.DARK_GREEN + "Giant Spruce Taiga Hills"), Biome.GIANT_SPRUCE_TAIGA_HILLS);
        itemsToBiomes.put(itemWithName(Material.RED_SAND, ChatColor.RED + "Badlands"), Biome.BADLANDS);
        //itemsToBiomes.put(itemWithName(Material.TERRACOTTA, ChatColor.RED + "Wooded Badlands"), Biome.WOODED_BADLANDS_PLATEAU);
        //itemsToBiomes.put(itemWithName(Material.TERRACOTTA, ChatColor.RED + "Modified Wooded Badlands"), Biome.MODIFIED_WOODED_BADLANDS_PLATEAU);
        //itemsToBiomes.put(itemWithName(Material.GOLD_ORE, ChatColor.RED + "Badlands Plateau"), Biome.BADLANDS_PLATEAU);
        //itemsToBiomes.put(itemWithName(Material.RED_SAND, ChatColor.RED + "Modified Badlands Plateau"), Biome.MODIFIED_BADLANDS_PLATEAU);
        itemsToBiomes.put(itemWithName(Material.RED_SAND, ChatColor.RED + "Eroded Badlands"), Biome.ERODED_BADLANDS);
        itemsToBiomes.put(itemWithName(Material.MYCELIUM, ChatColor.LIGHT_PURPLE + "Mushroom Fields"), Biome.MUSHROOM_FIELDS);
        //itemsToBiomes.put(itemWithName(Material.MYCELIUM, ChatColor.LIGHT_PURPLE + "Mushroom Field Shore"), Biome.MUSHROOM_FIELD_SHORE);
        //itemsToBiomes.put(itemWithName(Material.NETHERRACK, ChatColor.DARK_RED + "Nether"), Biome.NETHER);
        itemsToBiomes.put(itemWithName(Material.END_STONE, ChatColor.DARK_PURPLE + "The End"), Biome.THE_END);
        itemsToBiomes.put(itemWithName(Material.VOID_AIR, ChatColor.BLACK + "The Void"), Biome.THE_VOID);
        itemsToBiomes.put(itemWithName(Material.PURPUR_BLOCK, ChatColor.YELLOW + "End Midlands"), Biome.END_MIDLANDS);
        itemsToBiomes.put(itemWithName(Material.END_STONE, ChatColor.YELLOW + "End Barrens"), Biome.END_BARRENS);
        itemsToBiomes.put(itemWithName(Material.END_STONE, ChatColor.DARK_PURPLE + "Small End Islands"), Biome.SMALL_END_ISLANDS);
        itemsToBiomes.put(itemWithName(Material.CHORUS_PLANT, ChatColor.DARK_PURPLE + "End Highlands"), Biome.END_HIGHLANDS);

        int i = 0;
        for (ItemStack item : itemsToBiomes.keySet()) {
            menu[i < 36 ? 0 : 1].addItem(item);
            i++;
        }

        // TODO Add next page and before page buttons
    }

    void open(final HumanEntity entity) {
        entity.openInventory(menu[0]);
    }

    @EventHandler
    private void onMenuClick(final InventoryClickEvent event) {
        if (!event.getInventory().equals(menu[0]) && !event.getInventory().equals(menu[1])) return;

        event.setCancelled(true);

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        final Player player = (Player) event.getWhoClicked();
        player.sendMessage("You clicked at slot " + event.getRawSlot());

        if (event.getRawSlot() == 53 && event.getInventory().equals(menu[0])) {
            player.closeInventory();
            player.openInventory(menu[1]);
        } else if (event.getRawSlot() == 45 && event.getInventory().equals(menu[1])) {
            player.closeInventory();
            player.openInventory(menu[0]);
        } else {
            // TODO Add cooldown
            player.closeInventory();
            Bukkit.getScheduler().runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("BiomesCompass"), () -> {
                // TODO This may cause null exception when user selects an item in his inventory that does not exist in itemsToBiomes
                Biome target = itemsToBiomes.get(clickedItem);

                Optional<Location> location = getClosestTargetBiome(player.getLocation(), target);

                location.ifPresentOrElse(location1 -> {
                    player.sendMessage("Closest biome you selected is at: " + location.get().toVector());
                    player.setCompassTarget(location.get());
                }, () -> {
                    player.sendMessage("Could not find the biome in max search area");
                });
            });
        }
    }

    private Optional<Location> getClosestTargetBiome(Location origin, Biome biome) {
        BiomesQuery query = new BiomesQuery(origin, biome);
        BiomesQuery.Result res = BiomesQuery.cache.getUnchecked(query);
        Optional<Location> location = res.getLocation(biome);

        if (location.isEmpty()) {
            if (!res.didBreakEarly()) return Optional.empty();

            res = query.fetch();
            BiomesQuery.cache.put(query, res);
            location = res.getLocation(biome);
        }

        return location;
    }

    @EventHandler
    private void cancelDragging(final InventoryDragEvent event) {
        if (event.getInventory().equals(menu[0]) || event.getInventory().equals(menu[1])) event.setCancelled(true);
    }

    private ItemStack itemWithName(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}

