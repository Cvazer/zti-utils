package com.github.cvazer.scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This is a utility class that allows it's instances to read input stream of system console and execute particular code associated
 * with different commands depending on the input of the stream. In order to properly use this class you should create an instance of it
 * and populate it with different commands.
 *
 * @author Yan Frankovski
 * @since ZTIU 1.0.1
 * @see Command
 * @see Consumable
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ConsoleScanner {
    /** This list is used as storage for all added commands. */
    private List<Command> commands;
    /** This values are used for parsing incoming {@code String} for commands and parameters input. */
    private String commandPrefix, paramPrefix, defaultKey;
    /** This value is used to indicate scanner life cycle. */
    private boolean running;
    /** This is a reference to {@code Thread} object for asynchronous scanner mode. */
    private Thread thread;
    /** This is the instance of a {@code Scanner} to be used during input reading. */
    private Scanner input;
    /** This value is a welcome string to be displayed during asynchronous scanner mode. */
    private String welcome;
    /** This value is the error message for displaying when input is not recognised. */
    private String error;

    /**
     * Allocates new {@code ConsoleScanner} object that contains all data necessary to popper reading of system input.
     * Scanner instance created bu this constructor will work in synchronous mode.
     *
     * @param commandPrefix Command prefix that will be used to determine which of the scanner token is actual command name.
     * @param paramPrefix Parameter prefix thai will be used to determine which of the scanner token is actual parameter name.
     */
    public ConsoleScanner(String commandPrefix, String paramPrefix) {
        this(commandPrefix, paramPrefix, true);
    }

    /**
     * Allocates new {@code ConsoleScanner} object that contains all data necessary to popper reading of system input.
     * Scanner instance created bu this constructor will work in asynchronous mode.
     *
     * @param commandPrefix Command prefix that will be used to determine which of the scanner token is actual command name.
     * @param paramPrefix Parameter prefix thai will be used to determine which of the scanner token is actual parameter name.
     * @param sync Asynchronous boolean flag
     */
    public ConsoleScanner(String commandPrefix, String paramPrefix, boolean sync) {
        this.commandPrefix = commandPrefix;
        this.paramPrefix = paramPrefix;
        defaultKey = "DEFAULT";
        commands = new ArrayList<>();
        input = new Scanner(System.in);
        if(sync){ return; }
        welcome = "\nThis is a default welcome message.\nYou can change it by calling .setWelcome(String s) method" +
                "\nYou can see full command list by typing \""+commandPrefix+"help\"\n";
        thread = new Thread(() -> {
            running = true;
            System.out.println(welcome);
            while (running){
                scan();
            }
        });

        addCommand(new Command("help", "Show all available commands").setConsumer(params -> {
            System.out.println("\nAVAILABLE COMMANDS:\n");
            commands.forEach(command -> System.out.println("\t"+commandPrefix+command.getName()+" - "+command.getDescription()));
            System.out.println();
        }));
    }

    /**
     * This method should be used to start scanner in asynchronous mode
     */
    public void start(){
        if(thread==null){return;}
        thread.start();
    }

    /**
     * This method should be used any time you want to read something from console.
     * In synchronous mode you have to call it manually, in asynchronous mode ot will be called automatically by scanner thread.
     */
    public void scan() {
        if (!input.hasNext()){return;}
        scanLine(input.nextLine());
    }

    /**
     * This method is used to start parsing incoming {@code String}.
     *
     * @param in input string read from system stream.
     */
    private void scanLine(String in){
        boolean consumed;

        Scanner scn = new Scanner(in);
        if (scn.findInLine(commandPrefix)==null) { System.out.println(error); return; }
        String cmd_name = null;
        if (scn.hasNext()) { cmd_name = scn.next(); }

        consumed = consume(cmd_name, scn);

        if (consumed) { return; }
        System.out.println(error);
    }

    /**
     * This method should be used if you want to load a set of sequential commands from external text file.
     * This could be useful for initialising and populating your program from external sources.
     *
     * @param file {@code File} object pointing to script file.
     */
    public void scan(File file){
        Scanner scn = null;
        try {
            scn = new Scanner(file);
            while (scn.hasNextLine()){
                scanLine(scn.nextLine());
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File does not exists");
        } finally {
            if (scn!=null)scn.close();
        }
    }

    /**
     * This method is used internally to associate given command name with particular command.
     *
     * @param cmd_name Given command name.
     * @param scn {@code Scanner} instance reference.
     * @return Boolean flag whether or not string was accepted(consumed) by any command.
     */
    private boolean consume(String cmd_name, Scanner scn) {
        for (Command command: commands) {
            if (command.getName().toUpperCase().contentEquals(cmd_name.toUpperCase())) {
                command.getConsumer().consume(getParams(scn));
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used internally to parse string from the scanner into map
     *
     * @param scn {@code Scanner} instance with given {@code String} in it.
     * @return Map that contains parsed key-params sets.
     */
    private Map<String, List<String>> getParams(Scanner scn){
        Map<String, List<String>> params = new HashMap<>();
        String key = defaultKey;
        while (scn.hasNext()) {
            String token = scn.next();
            if(token.contains(paramPrefix)) {
                key = token.replace(paramPrefix, "");
                params.put(key, new ArrayList<>());
            } else {
                if (key.contentEquals(defaultKey) &&params.get(defaultKey)==null){params.put(defaultKey, new ArrayList<>());}
                params.get(key).add(token);
            }
        }
        scn.close();
        return params;
    }

    /**
     * This method should be called to stop scanner in asynchronous mode.
     */
    public void stop(){
        running = false;
        input.close();
    }

    /**
     * @return List of all commands in scanner.
     */
    public List<Command> getCommands(){
        return commands;
    }

    /**
     * @return {@code String} that used as a default key for params map.
     */
    public String getDefaultKey() {
        return defaultKey;
    }

    /**
     * @param defaultKey New default key for params map.
     */
    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }

    /**
     * @param error New error message.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return {@code Thread} used during scanner asynchronous mode.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * @return {@code String} that used as command prefix.
     */
    public String getCommandPrefix() {
        return commandPrefix;
    }

    /**
     * @param commandPrefix New command prefix.
     */
    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    /**
     * @return {@code String} that used as parameter prefix.
     */
    public String getParamPrefix() {
        return paramPrefix;
    }

    /**
     * @param paramPrefix New parameter prefix.
     */
    public void setParamPrefix(String paramPrefix) {
        this.paramPrefix = paramPrefix;
    }

    /**
     * @return {@code String} associated with welcome message during asynchronous mode.
     */
    public String getWelcome() {
        return welcome;
    }

    /**
     * @param welcome New welcome message.
     */
    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    /**
     * This method should be used to add new {@code Command} instance to scanner.
     *
     * @param command {@code Command} instance to be added to scanner.
     */
    public void addCommand(Command command){
        commands.add(command);
    }

    /**
     * This method should be used to remove particular command from the scanner.
     * @param command {@code Command} instance to be removed.
     * @return {@code boolean} true if operation is successful.
     */
    public boolean removeCommand(Command command){
        return commands.remove(command);
    }

    /**
     * This method should be used to remove particular command from the scanner.
     * @param name {@code String} name of {@code Command} instance to be removed from the scanner.\
     * @return {@code boolean} true if operation is successful.
     */
    public boolean removeCommand(String name){
        Command to_remove = null;
        for(Command command: commands){
            if (command.getName().toUpperCase().contentEquals(name.toUpperCase())){
                to_remove = command;
            }
        }
        if (to_remove!=null){commands.remove(to_remove); return true;}
        else {return false;}
    }

    /**
     * Setter for commands list.
     * @param commands - ArrayList of Commands
     */
    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }

    /**
     * @return Whether of not the scanner is running right now (asynchronous mode)
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running New running boolean flag for the scanner.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
}
