package dev.rusthero.biomecompass.gui;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class BiomesMenu implements Listener {
    private final HashMap<World.Environment, Inventory> pages = new HashMap<>();

    public BiomesMenu() {
        Inventory overworldInv = Bukkit.createInventory(null, 54, "§2[§1OVERWORLD§2] §9Select a biome");
        Inventory netherInv = Bukkit.createInventory(null, 9, "§4[§6NETHER§4] §cSelect a biome");
        Inventory endInv = Bukkit.createInventory(null, 9, "§0[§5THE END§0] §eSelect a biome");

        pages.put(World.Environment.NORMAL, overworldInv);
        pages.put(World.Environment.NETHER, netherInv);
        pages.put(World.Environment.THE_END, endInv);

        for (Biome biome : Biome.values()) {
            try {
                BiomeElement element = BiomeElement.valueOf(biome.name());
                pages.get(element.environment).addItem(element.item);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    public void open(final HumanEntity entity) {
        entity.openInventory(pages.get(entity.getWorld().getEnvironment()));
    }

    public boolean contains(Inventory inventory) {
        return pages.containsValue(inventory);
    }
}

