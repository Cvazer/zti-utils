package by.zti.main.scanner;

import java.util.*;

public class ConsoleScanner {
    private List<Command> commands;
    private String commandPrefix, paramPrefix, defaultKey;
    private boolean running;
    private Thread thread;
    private Scanner input;

    private String welcome;
    private String error = "ERROR: Wrong input, can't handle";

    public ConsoleScanner(String commandPrefix, String paramPrefix) {
        this(commandPrefix, paramPrefix, true);
    }

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
            for (Command command: commands){
                System.out.println("\t"+commandPrefix+command.getName()+" - "+command.getDescription());
            }
            System.out.println();
        }));
    }

    public void start(){
        if(thread==null){return;}
        thread.run();
    }

    public void scan() {
        String in = input.nextLine();
        boolean consumed = false;

        Scanner scn = new Scanner(in);
        if (scn.findInLine(commandPrefix)==null) { System.out.println(error); return; }
        String cmd_name = null;
        if (scn.hasNext()) { cmd_name = scn.next(); }

        consumed = consume(cmd_name, scn);

        if (consumed) { return; }
        System.out.println(error);
    }

    private boolean consume(String cmd_name, Scanner scn) {
        for (Command command: commands) {
            if (command.getName().toUpperCase().contentEquals(cmd_name.toUpperCase())) {
                command.getConsumer().consume(getParams(scn));
                return true;
            }
        }
        return false;
    }

    private Map<String, List<String>> getParams(Scanner scn){
        Map<String, List<String>> params = new HashMap<>();
        String key = defaultKey;
        while (scn.hasNext()) {
            String token = scn.next();
            if(token.contains(paramPrefix)) {
                key = token.replace(paramPrefix, "");
                params.put(key, new ArrayList<>());
            } else {
                if (key== defaultKey &&params.get(defaultKey)==null){params.put(defaultKey, new ArrayList<>());}
                params.get(key).add(token);
            }
        }
        scn.close();
        return params;
    }

    public void stop(){
        running = false;
        input.close();
    }

    public String getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(String defaultKey) {
        this.defaultKey = defaultKey;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Thread getThread() {
        return thread;
    }

    public String getCommandPrefix() {
        return commandPrefix;
    }

    public void setCommandPrefix(String commandPrefix) {
        this.commandPrefix = commandPrefix;
    }

    public String getParamPrefix() {
        return paramPrefix;
    }

    public void setParamPrefix(String paramPrefix) {
        this.paramPrefix = paramPrefix;
    }

    public String getWelcome() {
        return welcome;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
    }

    public void addCommand(Command command){
        commands.add(command);
    }

    public void removeCommand(Command command){
        commands.remove(command);
    }

    public void removeCommand(String name){
        Command to_remove = null;
        for(Command command: commands){
            if (command.getName().toUpperCase().contentEquals(name.toUpperCase())){
                to_remove = command;
                break;
            }
        }
        if (to_remove!=null){commands.remove(to_remove);}
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
