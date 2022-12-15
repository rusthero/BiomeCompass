package dev.rusthero.biomecompass.locate;

import org.bukkit.Location;

import java.util.Optional;

public interface LocateBiomeCallback {
    void onQueryDone(Optional<Location> location);
}
