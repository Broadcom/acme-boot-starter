package com.acme.boot.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationTest {

    private HttpServer server;
    private final AtomicReference<String> receivedBody = new AtomicReference<>();
    private final AtomicReference<String> receivedMethod = new AtomicReference<>();
    private volatile int responseStatus = 200;

    @BeforeEach
    void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/notify", exchange -> {
            receivedMethod.set(exchange.getRequestMethod());
            receivedBody.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(responseStatus, -1);
            exchange.close();
        });
        server.start();
    }

    @AfterEach
    void stopServer() {
        server.stop(0);
    }

    private String endpoint() {
        return "http://127.0.0.1:" + server.getAddress().getPort() + "/notify";
    }

    @Test
    void constructorKeepsEndpointAndMessage() {
        Notification notification = new Notification("http://localhost/x", "hello");
        assertEquals("http://localhost/x", notification.getEndpoint());
        assertEquals("hello", notification.getMessage());
    }

    @Test
    void postsMessageToEndpoint() throws IOException {
        new Notification(endpoint(), "Your order shipped").sendNotification();

        assertEquals("POST", receivedMethod.get());
        assertEquals("Your order shipped", receivedBody.get());
    }

    @Test
    void failsWhenEndpointRejectsMessage() {
        responseStatus = 500;

        IOException e = assertThrows(IOException.class,
                () -> new Notification(endpoint(), "msg").sendNotification());
        assertTrue(e.getMessage().contains("500"));
    }

    @Test
    void failsWhenEndpointIsUnreachable() {
        String unreachable = endpoint();
        server.stop(0);

        assertThrows(IOException.class, () -> new Notification(unreachable, "msg").sendNotification());
    }
}
