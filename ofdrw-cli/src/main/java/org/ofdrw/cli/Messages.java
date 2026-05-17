package org.ofdrw.cli;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * CLI message localization utility.
 * <p>
 * Loads messages from {@code messages_en.properties} or {@code messages_ar.properties}
 * under {@code org/ofdrw/cli/} on the classpath.
 *
 * <p>Use {@link #setLocale(String)} to switch between {@code "en"} (English, default)
 * and {@code "ar"} (Arabic) before any message lookup.
 *
 * @author OFDRW Team
 * @since 2.3.9
 */
public final class Messages {

    private static ResourceBundle bundle = load(Locale.ENGLISH);

    private Messages() {}

    /**
     * Set the active locale for subsequent message lookups.
     *
     * @param lang language code: {@code "ar"} for Arabic, anything else defaults to English
     */
    public static void setLocale(String lang) {
        Locale locale = "ar".equalsIgnoreCase(lang) ? new Locale("ar") : Locale.ENGLISH;
        bundle = load(locale);
    }

    /**
     * Retrieve a localized message by key.
     *
     * @param key message key as defined in the properties files
     * @return the localized string, or the key itself if not found
     */
    public static String get(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
     * Retrieve a localized message and format it with the given arguments.
     *
     * @param key  message key
     * @param args arguments to substitute into the message pattern
     * @return the formatted localized string
     */
    public static String get(String key, Object... args) {
        return MessageFormat.format(get(key), args);
    }

    private static ResourceBundle load(Locale locale) {
        return ResourceBundle.getBundle("org.ofdrw.cli.messages", locale);
    }
}
