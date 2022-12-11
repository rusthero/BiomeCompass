package io.github.rusthero.biomescompass;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BiomesCompass extends JavaPlugin implements Listener {
    private BiomesMenu menu;

    @Override
    public void onEnable() {
        menu = new BiomesMenu();

        // TODO Configuration

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(this, this);
        pluginManager.registerEvents(menu, this);

        getServer().addRecipe(getRecipe());

        getLogger().info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomesCompass is disabled");
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) return;
        if (event.getItem() == null || !event.getItem().equals(getItem())) return;

        Player player = event.getPlayer();
        if (!player.hasPermission("biomescompass.use")) return;

        menu.open(player);
    }

    private ItemStack getItem() {
        ItemStack item = new ItemStack(Material.COMPASS, 1);

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_GREEN + "Biomes Compass");
        item.setItemMeta(meta);

        return item;
    }

    private ShapedRecipe getRecipe() {
        // TODO Add permission check for the recipe
        ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(this, "biomes_compass"), getItem());

        recipe.shape("SLS", "LCL", "SLS");

        recipe.setIngredient('S', Material.OAK_SAPLING);
        recipe.setIngredient('L', Material.OAK_LOG);
        recipe.setIngredient('C', Material.COMPASS);

        return recipe;
    }
}