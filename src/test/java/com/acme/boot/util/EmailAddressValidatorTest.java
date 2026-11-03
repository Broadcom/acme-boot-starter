package com.acme.boot.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class EmailAddressValidatorTest {

    @ParameterizedTest
    @ValueSource(strings = {"user@acme.com", "first.last+tag@sub.acme.co.uk", " padded@acme.com "})
    void acceptsValidAddresses(String email) {
        assertTrue(EmailAddressValidator.isValid(email));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "plain", "@acme.com", "user@", "user@acme", "user@@acme.com",
            "user@acme..com", "us er@acme.com", "user@-acme.com"})
    void rejectsInvalidAddresses(String email) {
        assertFalse(EmailAddressValidator.isValid(email));
    }

    @Test
    void rejectsTooLongAddress() {
        String longLocal = new String(new char[250]).replace('\0', 'a');
        assertFalse(EmailAddressValidator.isValid(longLocal + "@acme.com"));
    }

    @Test
    void normalizeTrimsAndLowerCases() {
        assertEquals("user@acme.com", EmailAddressValidator.normalize("  User@ACME.com "));
        assertNull(EmailAddressValidator.normalize(null));
    }

    @Test
    void domainOfReturnsDomainOrNull() {
        assertEquals("acme.com", EmailAddressValidator.domainOf("User@ACME.com"));
        assertNull(EmailAddressValidator.domainOf("not-an-email"));
    }
}
