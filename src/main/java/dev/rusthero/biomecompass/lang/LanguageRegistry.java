package dev.rusthero.biomecompass.lang;

import java.util.HashSet;

public final class LanguageRegistry {
    private final HashSet<Language> languages;

    public LanguageRegistry() {
        this.languages = new HashSet<>();
    }

    public boolean register(String localeCode) {
        // TODO Read file etc

        Language language = new Language(localeCode.toLowerCase());
        languages.add(language);
        
        return true;
    }

    public Language get(String localeCode) {
        return languages
                .stream()
                .filter(language -> language.localeCode.equalsIgnoreCase(localeCode))
                .findFirst()
                .orElse(new Language("en_us"));
    }
}
