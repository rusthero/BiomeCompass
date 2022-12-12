package io.github.rusthero.biomescompass.finder;

import org.bukkit.Location;

import java.util.Optional;

public interface LocateBiomeCallback {
    void onQueryDone(Optional<Location> location);

    void onRunning();

    void onCooldown();
}
