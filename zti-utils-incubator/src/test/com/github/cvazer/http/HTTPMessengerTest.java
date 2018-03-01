package com.github.cvazer.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class HTTPMessengerTest {

    @Test
    @DisplayName("Send")
    void send(){
        HTTPMessenger messenger = new HTTPMessenger().start(1488);
        messenger.createContext("/", (data, uri) -> {
            String text = new String(data);
            text+="GOTIT";
            return text.getBytes();
        });
        assertEquals("TEST GOTIT", new String(new HTTPMessenger().send("http://localhost:1488/", "TEST ".getBytes())));
        messenger.stop(0);
    }

    @Test
    @DisplayName("Send with exception")
    void sendException(){
        assertNull(new HTTPMessenger().send("http-invalid://localhost:1488/", "TEST ".getBytes()));
    }

    @Test
    @DisplayName("Send via Object[] params exception")
    void sendArrayException(){
        assertThrows(IllegalArgumentException.class, () -> new HTTPMessenger().send("Test".getBytes(), new Object[]{1}));
    }

    @Test
    @DisplayName("Send via Object[] params")
    void sendArray(){
        HTTPMessenger messenger = new HTTPMessenger().start(1488);
        messenger.createContext("/", (data, uri) -> {
            String text = new String(data);
            text+="GOTIT";
            return text.getBytes();
        });
        assertEquals("TEST GOTIT", new String(new HTTPMessenger().send("TEST ".getBytes(), new Object[]{"http://localhost:1488/"})));
        messenger.stop(0);
    }

    @Test
    @DisplayName("Start")
    void start(){
        assertNotNull(new HTTPMessenger().start(1489).getServer());
    }

    @Test
    @DisplayName("Start exception")
    void startException(){
        new HTTPMessenger().start(1499);
        assertNull(new HTTPMessenger().start(1499));
    }
}
