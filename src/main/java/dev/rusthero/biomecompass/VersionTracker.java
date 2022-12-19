package dev.rusthero.biomecompass;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * a utility class for the Biome Compass plugin that allows the plugin to check for updates from the plugin's GitHub
 * repository.
 */
public class VersionTracker {
    /**
     * URL of the GitHub API endpoint for the plugin's releases. This field is used to retrieve the latest version of
     * the plugin when the {@link VersionTracker} object is constructed.
     */
    private static final URL ENDPOINT_URL;

    static {
        try {
            ENDPOINT_URL = new URL("https://api.github.com/repos/rusthero/BiomeCompass/releases/latest");
        } catch (MalformedURLException exception) {
            // This should never throw.
            throw new RuntimeException(exception);
        }
    }

    /**
     * Current version of the plugin, as specified in the plugin's plugin.yml file.
     */
    public final String currentVersion;

    /**
     * latest version of the plugin, as retrieved from the GitHub API.
     */
    public final String latestVersion;

    public VersionTracker(BiomeCompass plugin) throws IOException {
        PluginDescriptionFile descriptionFile = plugin.getDescription();
        // We use the prefix "v" in release tag names.
        currentVersion = "v" + descriptionFile.getVersion();

        HttpURLConnection connection = (HttpURLConnection) ENDPOINT_URL.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonElement json = new JsonParser().parse(reader);

        latestVersion = json.getAsJsonObject().get("tag_name").getAsString();
    }

    /**
     * Checks if the plugin is up-to-date.
     *
     * @return {@code true} if the current version of the plugin is the same as the latest version, and {@code false}
     * otherwise.
     */
    public boolean isUpToDate() {
        return currentVersion.equals(latestVersion);
    }
}
