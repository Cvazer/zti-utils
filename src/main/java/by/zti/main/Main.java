package by.zti.main;

import by.zti.main.scanner.Command;
import by.zti.main.scanner.ConsoleScanner;

public class Main {
    public static void main(String[] args) {
        ConsoleScanner scanner = new ConsoleScanner("/", "-", false);
        scanner.addCommand(new Command("exit", "").setConsumer(params -> scanner.stop()));
        scanner.start();
    }
}
