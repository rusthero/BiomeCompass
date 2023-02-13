package dev.rusthero.biomecompass.lang;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.rusthero.biomecompass.BiomeCompass;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class Language {
    private final HashMap<Field, String> strings;

    public Language(String localeCode) throws IOException {
        final String path = format("/lang/%s.json", localeCode);
        try (InputStream stream = BiomeCompass.class.getResourceAsStream(path)) {
            assert stream != null; // AssertionError is thrown when json file does not exist for the locale

            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            Type type = new TypeToken<Map<String, String>>() {

            }.getType();
            Map<String, String> jsonMap = new Gson().fromJson(reader, type);
            reader.close();

            strings = new HashMap<>();
            jsonMap.forEach((key, value) -> {
                Field field = Field.valueOf(key.replace(".", "_").toUpperCase());
                strings.put(field, value);
            });
        } catch (IOException exception) {
            throw new IOException("Cannot read language file: " + exception.getMessage());
        } catch (AssertionError error) {
            throw new FileNotFoundException(format("Translation file %s.json cannot be found!", localeCode));
        }
    }

    public String getString(Field field) {
        return strings.get(field);
    }
}
