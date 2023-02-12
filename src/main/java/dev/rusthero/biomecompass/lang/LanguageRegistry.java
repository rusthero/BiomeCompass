package dev.rusthero.biomecompass.lang;

import java.util.HashSet;

public final class LanguageRegistry {
    private final HashSet<Language> languages;

    public LanguageRegistry() {
        this.languages = new HashSet<>();
    }

    public void register(String localeCode) {
        // TODO Read file etc
        new Language(localeCode.toLowerCase());
    }

    public Language get(String localeCode) {
        return languages
                .stream()
                .filter(language -> language.localeCode.equalsIgnoreCase(localeCode))
                .findFirst()
                .orElse(new Language("en_us"));
    }
}
