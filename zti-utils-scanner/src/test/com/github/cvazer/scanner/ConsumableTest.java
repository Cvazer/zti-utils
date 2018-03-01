package com.github.cvazer.scanner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsumableTest {

    @SuppressWarnings("TrivialFunctionalExpressionUsage")
    @Test
    @DisplayName("Consume")
    void consume(){
        Map<String, List<String>> params = new HashMap<>();
        params.put("test", Arrays.asList("Test-Data", "Test-Data2"));
        final String[] text = {null};
        ((Consumable) params1 -> params1.get("test").forEach(param -> text[0] += param)).consume(params);
        assertTrue(text[0].contentEquals("nullTest-DataTest-Data2"));
    }

}
