package com.acme.boot.notification;

/**
 * Thrown when a {@link Notification} cannot be delivered.
 */
public class AcmeNotificationException extends Exception {

    private static final long serialVersionUID = 1L;

    public AcmeNotificationException(String message) {
        super(message);
    }

    public AcmeNotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
