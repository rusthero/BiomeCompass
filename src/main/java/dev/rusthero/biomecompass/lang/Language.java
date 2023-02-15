package dev.rusthero.biomecompass.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.rusthero.biomecompass.BiomeCompass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * Represents a language/translation read from resources. Contains an HashMap read from the corresponding translation
 * file to get strings using {@link Field}.
 */
public class Language {
    /**
     * Basically the JSON in a parsed form to retrieve strings using {@link Field}s.
     */
    private final HashMap<Field, String> strings = new HashMap<>();

    /**
     * Constructs a Language using locale code. The language is going to be read from lang/{localeCode}.json on the
     * resources' folder.
     *
     * @param localeCode Code of the language. It is en_us for American English. Locale codes are case-insensitive.
     * @throws IOException When language file for the given locale does not exist or unable to read.
     */
    public Language(String localeCode) throws IOException {
        final String path = format("/lang/%s.json", localeCode.toLowerCase());
        // Next line throws IOError if no file exists in the path which means unsupported locale.
        InputStream stream = BiomeCompass.class.getResourceAsStream(path);
        if (stream == null) throw new IOException(format("Locale with the code %s is not supported!", localeCode));
        // Read language JSON file as Map<String, String> to use them later.
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        // This Type thing looks awful, maybe we can improve in the future.
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> jsonMap = new Gson().fromJson(reader, type);
        reader.close();
        jsonMap.forEach((key, value) -> {
            Field field = Field.valueOf(key.replace(".", "_").toUpperCase());
            strings.put(field, value);
        });
    }

    /**
     * Gets the string using mapped to a field which is read from the translation file.
     *
     * @param field Field of the string mapped to it.
     * @return String mapped to the corresponding field.
     */
    public String getString(Field field) {
        return strings.get(field);
    }
}
