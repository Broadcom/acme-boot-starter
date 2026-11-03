package com.acme.boot.util;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Minimal email address helper, implemented in-house to avoid an extra dependency.
 */
public final class EmailAddressValidator {

    private static final int MAX_LENGTH = 254;

    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+$");

    private EmailAddressValidator() {
    }

    /** Returns true if the address looks like a valid email. Null-safe. */
    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }
        String trimmed = email.trim();
        return trimmed.length() <= MAX_LENGTH
                && !trimmed.contains("..")
                && EMAIL.matcher(trimmed).matches();
    }

    /** Trims and lower-cases the address. Returns null for null input. */
    public static String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    /** Returns the domain part of the address, or null if the address is invalid. */
    public static String domainOf(String email) {
        if (!isValid(email)) {
            return null;
        }
        String normalized = normalize(email);
        return normalized.substring(normalized.indexOf('@') + 1);
    }
}
