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

public class Language {
    private final HashMap<Field, String> strings = new HashMap<>();

    public Language(String localeCode) throws IOException {
        final String path = format("/lang/%s.json", localeCode.toLowerCase());
        // Next line throws IOError if no file exists in the path which means unsupported locale.
        InputStream stream = BiomeCompass.class.getResourceAsStream(path);
        if (stream == null) throw new IOException(format("Locale with the code %s is not supported!", localeCode));
        // Read language JSON file as Map<String, String> to use them later.
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> jsonMap = new Gson().fromJson(reader, type);
        reader.close();
        jsonMap.forEach((key, value) -> {
            Field field = Field.valueOf(key.replace(".", "_").toUpperCase());
            strings.put(field, value);
        });
    }

    public String getString(Field field) {
        return strings.get(field);
    }
}
