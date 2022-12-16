package dev.rusthero.biomecompass.locate;

import dev.rusthero.biomecompass.BiomeCompass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import java.util.Optional;

/**
 * Represents a player who is performing biome location queries. This class provides a higher-level interface for
 * players to access the `LocateBiomeQuery` class.
 * Note that the cache has nothing to do with players or locators, but rather with locations. This class is used to
 * apply cool-downs and to check if a player is already running a query.
 * <p>
 * Every player has a single `PlayerBiomeLocator` assigned to them in the {@link PlayerBiomeLocatorRegistry}. This
 * class should not be directly constructed, which is why its constructor is package-level protected.
 *
 * @see LocateBiomeQuery
 * @see LocateBiomeCallback
 * @see PlayerBiomeLocatorRegistry
 */
public class PlayerBiomeLocator {
    /**
     * The player who is performing the biome location queries. The player's location will be used as the origin of
     * the search.
     */
    public final Player player;

    /**
     * Whether a query is currently running for this player. This flag is modified in the asynchronous
     * {@link #asyncLocateBiome} method and is used to prevent the player from running multiple queries at once.
     *
     * @see dev.rusthero.biomecompass.listeners.ItemUseListener
     */
    private boolean running = false;

    /**
     * Whether this player is currently on cooldown for performing a query. The cooldown duration is read from the
     * plugin's settings in {@link dev.rusthero.biomecompass.Settings#cooldown}. The cooldown period begins when the
     * query is started, not when it is completed. This flag will always be false if the cooldown duration is 0 seconds.
     *
     * @see dev.rusthero.biomecompass.listeners.ItemUseListener
     */
    private boolean onCooldown = false;

    /**
     * Constructs a new `PlayerBiomeLocator` object for the given player. This constructor should not be accessed
     * directly and should only be called from the {@link PlayerBiomeLocatorRegistry} to prevent the creation of
     * duplicate locators for the same player. A single locator should be constructed for each player, so the
     * registry should be used to manage this.
     *
     * @param player The player whose location will be used as the origin when performing locate biome queries.
     */
    PlayerBiomeLocator(Player player) {
        this.player = player;
    }

    /**
     * This method synchronously locates the nearest target biome for a player. It blocks the main thread, so it
     * should not be used directly, but rather in an asynchronous task. Use the {@link #asyncLocateBiome} method for
     * that purpose. Because this method is synchronous, it does not modify the {@link #running} and{@link #onCooldown}
     * flags.
     * <p>
     * This method will not search the entire range, but will instead finish immediately when the target biome is
     * located. However, any biomes that are found along the way will still be cached.
     *
     * @param biome The biome to be located nearest to the player.
     * @param cache The `LocateBiomeCache` that will be used to get and load the query result.
     * @return An optional location of the locate biome query that holds the location of the nearest biome to the
     * player. It will be empty if the biome was not found within the {
     * @link dev.rusthero.biomecompass.Settings#radius}.
     * * @see #asyncLocateBiome(Biome, BiomeCompass, LocateBiomeCallback)
     */
    public Optional<Location> locateBiome(Biome biome, LocateBiomeCache cache) {
        // Construct a query using the player's location as the origin and the target biome from the parameter.
        LocateBiomeQuery query = new LocateBiomeQuery(player.getLocation(), biome);
        // Attempt to get the cached query in the area, or search for it if it does not exist.
        LocateBiomeQueryResult result = cache.get(query);

        // If the location for the target biome was not found in the cache, there are two possible reasons for this:
        Optional<Location> location = result.getLocation(biome);
        if (location.isEmpty()) {
            // First, the target biome may not exist within the search range. If an early break did not occur, it
            // means that the target biome was not found, and we can return an empty optional.
            if (!result.isEarlyBreak()) return Optional.empty();

            // Second, the cache may be invalid because the previous query ended before finding the current target.
            // Locate biome queries break early when the target is found. If a common/nearby biome was queried before
            // and cached, it is possible that the current target was not found, especially if it is rare. In this
            // case, the cache is not useful, and we must invalidate it and fetch the target again.
            result = cache.load(query); // TODO: Continue from where early break left to improve performance.

            location = result.getLocation(biome);
        }
        return location;
    }

    /**
     * This is an asynchronous method to locate the nearest target biome for a player. It modifies the
     * {@link #running} and {@link #onCooldown} flags to keep track of the query. Because of the asynchronous nature,
     * instead of returning a value, a callback is called when the query is done.
     * <p>
     * This method will not search the entire range, but will finish immediately when the target biome is located.
     * However, biomes that are found on the way will still be cached.
     *
     * @param biome    Biome to be located nearest to the player.
     * @param plugin   BiomeCompass plugin to be used for starting the asynchronous task and getting the cache.
     * @param callback LocateBiomeCallback to be called when the query is done, with the biome location as a parameter.
     * @see LocateBiomeCallback#onQueryDone
     * @see dev.rusthero.biomecompass.listeners.MenuClickListener
     */
    public void asyncLocateBiome(Biome biome, BiomeCompass plugin, LocateBiomeCallback callback) {
        // We do not want to create multiple queries, and the cooldown period is useful for performance.
        if (running || onCooldown) return;

        // Use the synchronous locateBiome method inside an asynchronous task and return the nearest target biome found.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            running = true;
            callback.onQueryDone(locateBiome(biome, plugin.getLocateBiomeCache()));
            running = false;
        });


        // We do not want to bother with the cooldown flag if the duration is smaller than or equal to 0 seconds.
        // Start the cooldown timer from the moment this method is called
        onCooldown = true;
        final long cooldown = plugin.getSettings().cooldown * 20L;
        if (cooldown > 0) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> onCooldown = false, cooldown);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isOnCooldown() {
        return onCooldown;
    }
}
