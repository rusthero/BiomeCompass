package dev.rusthero.biomecompass.locate;

import dev.rusthero.biomecompass.BiomeCompass;
import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.Optional;

/**
 * Represents a callback from the
 * {@link PlayerBiomeLocator#asyncLocateBiome(Biome, BiomeCompass, LocateBiomeCallback)} method.
 * The asynchronous nature of locating a biome makes it difficult to return a location value, so the `onQueryDone`
 * method is called
 * with an optional location parameter instead.
 *
 * @see PlayerBiomeLocator
 */
public interface LocateBiomeCallback {
    /**
     * Called by the {@link PlayerBiomeLocator#asyncLocateBiome(Biome, BiomeCompass, LocateBiomeCallback)} method
     * when the locating process is complete.
     * This method can be used to bind the location of the nearest biome to a
     * {@link dev.rusthero.biomecompass.items.BiomeCompassItem}.
     *
     * @param location An Optional containing the nearest location of the specified biome, or an empty Optional if
     *                 the biome was not found within the maximum search range.
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void onQueryDone(Optional<Location> location);
}
