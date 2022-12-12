package io.github.rusthero.biomescompass;

import io.github.rusthero.biomescompass.gui.LocateBiomeMenu;
import io.github.rusthero.biomescompass.item.BiomesCompassItem;
import io.github.rusthero.biomescompass.listener.MenuClickListener;
import io.github.rusthero.biomescompass.listener.MenuDragListener;
import io.github.rusthero.biomescompass.listener.ItemUseListener;
import io.github.rusthero.biomescompass.locate.BiomeLocator;
import io.github.rusthero.biomescompass.locate.BiomeLocatorRegistry;
import io.github.rusthero.biomescompass.locate.LocateBiomeCallback;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class BiomesCompass extends JavaPlugin {
    private BiomeLocatorRegistry biomeLocators;
    private LocateBiomeMenu selectMenu;

    @Override
    public void onEnable() {
        // TODO Configuration

        biomeLocators = new BiomeLocatorRegistry();
        selectMenu = new LocateBiomeMenu();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ItemUseListener(this), this);
        pluginManager.registerEvents(new MenuClickListener(this, selectMenu), this);
        pluginManager.registerEvents(new MenuDragListener(selectMenu), this);

        getServer().addRecipe(BiomesCompassItem.getRecipe(this));

        getLogger().info("BiomesCompass is enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("BiomesCompass is disabled");
    }

    public void useItem(Player player) {
        BiomeLocator locator = biomeLocators.get(player);

        if (locator.isRunning()) {
            player.sendMessage("Please wait, searching.");
            return;
        }

        if (locator.isOnCooldown()) {
            player.sendMessage("Please wait, you are on cooldown.");
            return;
        }

        selectMenu.open(player);
    }

    public void searchBiome(Player player, Biome biome) {
        biomeLocators.get(player).asyncLocateBiome(biome, this, new LocateBiomeCallback() {
            @Override
            public void onQueryDone(Optional<Location> optLocation) {
                optLocation.ifPresentOrElse(location -> {
                    player.sendMessage("Closest biome you selected is at: " + location.toVector());
                    player.setCompassTarget(location);
                }, () -> player.sendMessage("Could not find the biome in max search area"));
            }

            @Override
            public void onRunning() {
                player.sendMessage("You are already searching a biome!");
            }

            @Override
            public void onCooldown() {
                player.sendMessage("Please wait, you are on cooldown!");
            }
        });
    }
}