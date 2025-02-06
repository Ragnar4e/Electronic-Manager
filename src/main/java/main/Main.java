package main;

import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();

        // Add shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nShutting down application...");
            try {
                // Close any open resources
                if (ui != null) {
                    ui.shutdown();
                }
            } catch (Exception e) {
                System.err.println("Error during shutdown: " + e.getMessage());
            }
            System.out.println("Shutdown complete.");
        }));

        try {
            System.out.println("Starting Electronic Home Manager...");
            ui.start();
        } catch (Exception e) {
            System.out.println("Application error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}