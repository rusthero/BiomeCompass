package io.github.rusthero.biomescompass.locate;

import org.bukkit.Location;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.Optional;

public class LocateBiomeQueryResult {
    private final HashMap<Biome, Location> biomeLocations;
    private final boolean earlyBreak;

    LocateBiomeQueryResult(HashMap<Biome, Location> biomeLocations, boolean earlyBreak) {
        this.biomeLocations = biomeLocations;
        this.earlyBreak = earlyBreak;
    }

    public Optional<Location> getLocation(Biome biome) {
        return Optional.ofNullable(biomeLocations.get(biome));
    }

    public boolean isEarlyBreak() {
        return earlyBreak;
    }
}
