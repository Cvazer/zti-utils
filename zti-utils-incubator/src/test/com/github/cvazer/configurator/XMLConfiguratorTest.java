package com.github.cvazer.configurator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class XMLConfiguratorTest {
    private File file;

    @Test
    @DisplayName("Constructor")
    void constructor(){
        file = new File("constructor-test.xml");
        assertTrue(ConfiguratorFactory.getInstance(file) instanceof XMLConfigurator);
    }

    @Test
    @DisplayName("Constructor exception")
    void constructorException(){
        file = new File("constructor-test.txt");
        assertThrows(IllegalArgumentException.class, () -> ConfiguratorFactory.getInstance(file));
    }

    @Test
    @DisplayName("Read file")
    void read() throws URISyntaxException {
        Configurator configurator = ConfiguratorFactory.getInstance(
                new File(Objects.requireNonNull(getClass().getClassLoader().getResource("read-xml-test.xml")
                ).toURI()));
        Map<String, String> params = configurator.read("main");
        assertAll(
                () -> assertEquals("test2", params.get("test")),
                () -> assertEquals("test4", params.get("test3"))
        );
    }
}
