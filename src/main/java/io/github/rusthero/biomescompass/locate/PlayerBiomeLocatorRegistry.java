package io.github.rusthero.biomescompass.locate;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;

public final class PlayerBiomeLocatorRegistry {
    private final HashSet<PlayerBiomeLocator> biomeLocators;

    public PlayerBiomeLocatorRegistry() {
        biomeLocators = new HashSet<>();
    }

    public PlayerBiomeLocator get(Player player) {
        Optional<PlayerBiomeLocator> locator = biomeLocators.stream().filter(finder -> finder.player.equals(player)).findFirst();

        return locator.orElseGet(() -> add(new PlayerBiomeLocator(player)));
    }

    private PlayerBiomeLocator add(PlayerBiomeLocator locator) {
        biomeLocators.add(locator);

        return locator;
    }
}
