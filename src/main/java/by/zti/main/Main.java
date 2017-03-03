package by.zti.main;

import by.zti.main.scanner.Command;
import by.zti.main.scanner.ConsoleScanner;

public class Main {
    public static void main(String[] args) {
        ConsoleScanner scn = new ConsoleScanner("/", "-", false);

        scn.addCommand(new Command("exit", "Close program").setConsumer(params -> {
            scn.stop();
        }));

        scn.start();
    }
}
