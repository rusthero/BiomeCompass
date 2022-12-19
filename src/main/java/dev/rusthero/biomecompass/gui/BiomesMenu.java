package dev.rusthero.biomecompass.gui;

import dev.rusthero.biomecompass.BiomeElement;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

import static net.md_5.bungee.api.ChatMessageType.ACTION_BAR;
import static org.bukkit.World.Environment;

/**
 * This class represents a menu for selecting biomes. It contains three inventories that open according to the
 * player's dimension. The BiomeElement enum is used to get the item data of biomes.
 *
 * @see BiomeElement
 */
public class BiomesMenu {
    /**
     * A map that associates each of the three dimensions with a corresponding inventory that holds the biomes for
     * that dimension.
     */
    private final HashMap<Environment, Inventory> inventories;

    /**
     * This constructor creates three inventories for selecting biomes in different dimensions. It also adds every
     * BiomeElement to their corresponding inventories according to their dimensions.
     */
    public BiomesMenu(HashMap<Biome, Boolean> biomes) {
        inventories = new HashMap<>();

        Inventory overworld = Bukkit.createInventory(null, 54, "§2§l[§1§lOVERWORLD§2§l] §9Select a biome");
        inventories.put(Environment.NORMAL, overworld);

        Inventory nether = Bukkit.createInventory(null, 9, "§4§l[§6§lNETHER§4§l] §cSelect a biome");
        inventories.put(Environment.NETHER, nether);

        Inventory end = Bukkit.createInventory(null, 9, "§0§l[§5§lTHE END§0§l] §eSelect a biome");
        inventories.put(Environment.THE_END, end);

        // Add every BiomeElement to their corresponding inventories based on their dimensions.
        for (Biome biome : Biome.values())
            try {
                // Skip biomes which are set false in configuration
                if (biomes.get(biome) != null && !biomes.get(biome)) continue;

                BiomeElement element = BiomeElement.valueOf(biome.name());
                Inventory inventory = inventories.get(element.environment);
                inventory.addItem(element.item);
            } catch (IllegalArgumentException ignored) {
                // If the biome is not registered as a BiomeElement, it will be caught here. This means that a new
                // biome has been added and the plugin is outdated. The missing biomes will be skipped, but the rest
                // will still work correctly.
            }
    }

    /**
     * This method opens the appropriate menu for the player based on the player's dimension.
     *
     * @param player The player who will open the menu.
     */
    public void open(Player player) {
        Environment environment = player.getWorld().getEnvironment();
        Inventory inventory = inventories.get(environment);

        // If no biomes are added to the inventory, no need to show an empty menu
        if (inventory.firstEmpty() == 0)
            player.spigot().sendMessage(ACTION_BAR, new TextComponent("§cNot allowed in this dimension"));
        else
            player.openInventory(inventory);
    }

    /**
     * This method checks if the given inventory is part of the biomes menu.
     *
     * @param inventory The inventory that will be checked.
     * @return {@code true} if the inventory is part of the biomes menu, {@code false} otherwise.
     */
    public boolean contains(Inventory inventory) {
        return inventories.containsValue(inventory);
    }
}

