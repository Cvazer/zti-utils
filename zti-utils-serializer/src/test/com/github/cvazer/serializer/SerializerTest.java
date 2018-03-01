package com.github.cvazer.serializer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("ResultOfMethodCallIgnored")
@DisplayName("Serializer")
class SerializerTest {
    private File file;

    @Test
    @DisplayName("With empty constructor")
    void emptyConstruct(){
        assertNull(new Serializer<>().getFile());
    }

    @Test
    @DisplayName("With String path constructor")
    void pathConstruct(){
        file = new File("test.sr");
        Serializer<Object> serializer = new Serializer<>("test.sr");
        assertAll(
                () -> assertNotNull(serializer.getFile()),
                () -> assertTrue(serializer.getFile().getName().contentEquals("test.sr")),
                () -> assertTrue(serializer.getFile().exists())
        );
    }

    @Test
    @DisplayName("With file constructor")
    void fileConstruct(){
        file = new File("test2.sr");
        Serializer<Object> serializer = new Serializer<>(file);
        assertAll(
                () -> assertNotNull(serializer.getFile()),
                () -> assertTrue(serializer.getFile().getName().contentEquals("test2.sr")),
                () -> assertTrue(serializer.getFile().exists())
        );
    }

    @Test
    @DisplayName(".serialize(T object) method")
    void serialize(){
        file = new File("test3.sr");
        Serializer<Object> serializer = new Serializer<>();
        serializer.setFile(file);
        serializer.serialize(1);
        assertAll(
                () -> assertTrue(file.exists()),
                () -> assertTrue(file.length()!=0)
        );
    }

    @Test
    @DisplayName(".deserialize(T object) method")
    void deSerialize() {
        file = new File("test4.sr");
        Serializer<Integer> serializer = new Serializer<>(file);
        serializer.serialize(2);
        assertEquals(2, serializer.deserialize().intValue());
    }

    @AfterEach
    void cleanUp(){
        if (file==null){return;}
        file.delete();
    }

}
