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

public class VersionTracker {
    private static final URL API_URL;

    static {
        try {
            API_URL = new URL("https://api.github.com/repos/rusthero/BiomeCompass/releases/latest");
        } catch (MalformedURLException exception) {
            // This should never throw.
            throw new RuntimeException(exception);
        }
    }

    public final String currentVersion;
    public final String latestVersion;

    public VersionTracker(BiomeCompass plugin) throws IOException {
        PluginDescriptionFile descriptionFile = plugin.getDescription();
        currentVersion = "v" + descriptionFile.getVersion();

        HttpURLConnection connection = (HttpURLConnection) API_URL.openConnection();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        JsonElement json = new JsonParser().parse(reader);

        latestVersion = json.getAsJsonObject().get("tag_name").getAsString();
    }

    public boolean isUpToDate() {
        return currentVersion.equals(latestVersion);
    }
}
