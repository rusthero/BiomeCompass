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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public final String currentVersion;
    public final String latestVersion;

    public VersionTracker(BiomeCompass plugin) throws IOException {
        PluginDescriptionFile descriptionFile = plugin.getDescription();
        currentVersion = "v" + descriptionFile.getVersion();

        HttpURLConnection connection = (HttpURLConnection) API_URL.openConnection();
        int responseCode = connection.getResponseCode();
        // TODO Remove these debug lines
        plugin.getLogger().info("\nSending 'GET' request to URL : " + API_URL);
        plugin.getLogger().info("Response Code : " + responseCode);

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        /*String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) response.append(inputLine);
        reader.close();
        plugin.getLogger().info(response.toString());*/

        JsonElement json = new JsonParser().parse(reader);
        latestVersion = json.getAsJsonObject().get("tag_name").getAsString();
        plugin.getLogger().info("Latest version is " + latestVersion);
    }

    public boolean isUpToDate() {
        return currentVersion.equals(latestVersion);
    }
}
