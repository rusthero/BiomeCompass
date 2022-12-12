package io.github.rusthero.biomescompass.locate;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;

public final class BiomeLocatorRegistry {
    private static final BiomeLocatorRegistry instance = new BiomeLocatorRegistry();

    public static BiomeLocatorRegistry getInstance() {
        return instance;
    }

    private final HashSet<BiomeLocator> biomeLocators;

    private BiomeLocatorRegistry() {
        biomeLocators = new HashSet<>();
    }

    public BiomeLocator get(Player player) {
        Optional<BiomeLocator> locator = biomeLocators.stream().filter(finder -> finder.player.equals(player)).findFirst();

        return locator.orElseGet(() -> add(new BiomeLocator(player)));
    }

    private BiomeLocator add(BiomeLocator locator) {
        biomeLocators.add(locator);

        return locator;
    }
}
