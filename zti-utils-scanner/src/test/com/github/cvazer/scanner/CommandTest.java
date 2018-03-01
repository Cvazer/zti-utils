package com.github.cvazer.scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    @DisplayName("Constructor and getters")
    void constructor(){
        Command command = new Command("Test", "test");
        assertAll(
                () -> assertTrue(command.getName().contentEquals("Test")),
                () -> assertTrue(command.getDescription().contentEquals("test")),
                () -> assertNull(command.getConsumer())
        );
    }

    @Test
    @DisplayName("Setters")
    void setters(){
        Command command = new Command("Test2", "test2");
        final String[] text = {""};
        command.setConsumer(params -> text[0] += "cons1");
        command.setName("Test3");
        command.setDescription("test3");
        command.getConsumer().consume(new HashMap<>());
        assertAll(
                () -> assertTrue(command.getName().contentEquals("Test3")),
                () -> assertTrue(command.getDescription().contentEquals("test3")),
                () -> assertNotNull(command.getConsumer()),
                () -> assertTrue(text[0].contentEquals("cons1"))
        );
    }

}
