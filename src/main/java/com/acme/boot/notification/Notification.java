package com.acme.boot.notification;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * A notification that is delivered by POSTing its message to an HTTP endpoint.
 */
public class Notification {

    private final String endpoint;
    private final String message;

    public Notification(String endpoint, String message) {
        this.endpoint = endpoint;
        this.message = message;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getMessage() {
        return message;
    }

    /**
     * Sends the message to the configured endpoint.
     *
     * @throws IOException if the endpoint cannot be reached or answers with a non-2xx status
     */
    public void sendNotification() throws IOException {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(endpoint))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "text/plain; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() / 100 != 2) {
                throw new IOException("Notification rejected by " + endpoint + ": HTTP " + response.statusCode());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while sending notification to " + endpoint, e);
        }
    }
}
