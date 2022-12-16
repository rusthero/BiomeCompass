package dev.rusthero.biomecompass.locate;

import org.bukkit.entity.Player;

import java.util.HashSet;

/**
 * Represents a registry for {@link PlayerBiomeLocator}s. The purpose of this class is to provide a way to access a
 * unique {@link PlayerBiomeLocator} instance for each player. The registry should be used when accessing player
 * biome locators.
 *
 * @see PlayerBiomeLocator
 */
public final class PlayerBiomeLocatorRegistry {
    /**
     * A set containing the registered {@link PlayerBiomeLocator}s.
     * <p>
     * This set is used to store and access the {@link PlayerBiomeLocator} instances associated with each player.
     */
    private final HashSet<PlayerBiomeLocator> biomeLocators;

    /**
     * Constructs a new empty {@link PlayerBiomeLocatorRegistry}.
     * <p>
     * Initializes the {@link #biomeLocators} set to be used for storing and accessing the registered
     * {@link PlayerBiomeLocator} instances.
     */
    public PlayerBiomeLocatorRegistry() {
        biomeLocators = new HashSet<>();
    }

    /**
     * Retrieves the {@link PlayerBiomeLocator} for the given player. If a locator for the player does not exist, a new
     * one will be constructed and added to the registry.
     *
     * @param player Player for whom the locator will be used to locate biomes. The player's location will be used as
     *               the origin for queries.
     * @return {@link PlayerBiomeLocator} for the given player.
     */
    public PlayerBiomeLocator get(Player player) {
        // Check if a locator for the player already exists in the registry. If it does, return it.
        return biomeLocators
                .stream()
                .filter(finder -> finder.player.equals(player))
                .findFirst()
                .orElseGet(() -> {
                    // Otherwise, construct a new locator for the player and add it to the registry.
                    PlayerBiomeLocator locator = new PlayerBiomeLocator(player);
                    biomeLocators.add(locator);
                    return locator;
                });
    }
}
