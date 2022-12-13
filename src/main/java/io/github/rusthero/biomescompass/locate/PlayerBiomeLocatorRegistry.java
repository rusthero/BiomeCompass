package io.github.rusthero.biomescompass.locate;

import org.bukkit.entity.Player;

import java.util.HashSet;

public final class PlayerBiomeLocatorRegistry {
    private final HashSet<PlayerBiomeLocator> biomeLocators;

    public PlayerBiomeLocatorRegistry() {
        biomeLocators = new HashSet<>();
    }

    public PlayerBiomeLocator get(Player player) {
        return biomeLocators.stream().filter(finder -> finder.player.equals(player)).findFirst().orElseGet(() -> add(new PlayerBiomeLocator(player)));
    }

    private PlayerBiomeLocator add(PlayerBiomeLocator locator) {
        biomeLocators.add(locator);

        return locator;
    }
}
