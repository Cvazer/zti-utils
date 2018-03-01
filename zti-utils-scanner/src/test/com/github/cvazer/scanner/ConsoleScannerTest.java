package com.github.cvazer.scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleScannerTest {
    private static InputStream oldIn = System.in;
    private static PrintStream oldOut = System.out;

    @Test
    @DisplayName("Constructor")
    void constructor() throws NoSuchFieldException {
        ConsoleScanner scanner = new ConsoleScanner("", "");
        assertEquals(scanner.getCommandPrefix()+scanner.getParamPrefix(), "");
        scanner.setCommandPrefix("/");
        scanner.setParamPrefix("-");
        scanner.setDefaultKey("lll");
        scanner.setError("404");
        Field field = scanner.getClass().getDeclaredField("error");
        field.setAccessible(true);
        assertAll(
                () -> assertTrue(scanner.getCommandPrefix().contentEquals("/")),
                () -> assertTrue(scanner.getParamPrefix().contentEquals("-")),
                () -> assertTrue(scanner.getDefaultKey().contentEquals("lll")),
                () -> assertEquals("404", field.get(scanner)),
                () -> assertNotNull(scanner.getCommands())
        );
    }

    @Test
    @DisplayName("Constructor Async")
    void constructorAsync() {
        ConsoleScanner scanner = new ConsoleScanner("/", "-", false);
        scanner.setWelcome("welcome");
        scanner.getCommands().get(0).getConsumer().consume(new HashMap<>());
        assertAll(
                () -> assertNotNull(scanner.getWelcome()),
                () -> assertNotNull(scanner.getThread()),
                () -> assertEquals(1, scanner.getCommands().size())
        );
    }


    @SuppressWarnings({"InfiniteLoopStatement", "StatementWithEmptyBody"})
    @Test
    @DisplayName("Start")
    void start() throws InterruptedException {
        ConsoleScanner scanner = new ConsoleScanner("/", "-", false);
        scanner.start();
        Thread.sleep(10);
        assertEquals(true ,scanner.isRunning());
        scanner.stop();
        assertFalse(scanner.isRunning());
        scanner.setRunning(false);
    }

    @Test
    @DisplayName("Scan-ScanLine-Consume")
    void scan() {
        ByteArrayInputStream in = new ByteArrayInputStream("/test -p test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setIn(in);
        ConsoleScanner scanner = new ConsoleScanner("/", "-");
        scanner.addCommand(new Command("test", "").setConsumer(params -> System.out.print(params.get("p").get(0))));
        scanner.scan();
        assertEquals("test", out.toString());
    }

    @Test
    @DisplayName("Scan-ScanLine-Consume-Error")
    void scanError() {
        ByteArrayInputStream in = new ByteArrayInputStream("/testERROR -p test".getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        System.setIn(in);
        ConsoleScanner scanner = new ConsoleScanner("/", "-");
        scanner.setError("ERROR!");
        scanner.addCommand(new Command("test", "").setConsumer(params -> System.out.print(params.get("p").get(0))));
        scanner.scan();
        assertEquals(true, out.toString().contains("ERROR!"));
    }

    @Test
    @DisplayName("Remove commands")
    void remove(){
        ConsoleScanner scanner = new ConsoleScanner("/", "");
        Command command = new Command("test1", "");
        Command command2 = new Command("test2", "");
        scanner.addCommand(command);
        scanner.addCommand(command2);
        assertEquals(2, scanner.getCommands().size());
        assertTrue(scanner.removeCommand(command));
        assertTrue(scanner.removeCommand("test2"));
        assertFalse(scanner.removeCommand("moCommand"));
        assertEquals(0, scanner.getCommands().size());
        List<Command> commandList = new ArrayList<>();
        scanner.setCommands(commandList);
        assertSame(commandList, scanner.getCommands());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    @DisplayName("Read file")
    void readFile() throws IOException {
        File file = new File("nonexistant");
        ConsoleScanner scanner = new ConsoleScanner("/", "-");
        assertThrows(IllegalArgumentException.class, () -> scanner.scan(file));
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("/test -p 100");
        writer.close();
        scanner.addCommand(new Command("test", "").setConsumer(params -> System.out.print(params.get("p").get(0))));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        scanner.scan(file);
        assertEquals("100", out.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @AfterAll
    static void cleanUp(){
        System.setIn(oldIn);
        System.setOut(oldOut);
        File file = new File("nonexistant");
        if (file.exists()){file.delete();}
    }
}