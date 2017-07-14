package by.zti.main;

import by.zti.main.grabber.ModuleGrabber;

public class Main {
    public static void main(String[] args) {
        ModuleGrabber.getInstance().grab("modules").initModules();
    }
}
