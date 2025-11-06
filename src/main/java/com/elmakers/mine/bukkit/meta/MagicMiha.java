package com.elmakers.mine.bukkit.meta;

import java.util.Scanner;

import com.elmakers.mine.bukkit.magic.Mage;
import com.elmakers.mine.bukkit.magic.MagicController;
import com.elmakers.mine.bukkit.meta.platform.Platform;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;

public class MagicMiha {
    private final MagicController controller;
    private final Mage mage;


    public static void main(String[] args) {
        DummyPlugin plugin = new DummyPlugin();
        MagicController controller = new MagicController(plugin);
        Platform platform = new Platform(controller);
        CompatibilityLib.initialize(platform);
        MagicMiha miha = new MagicMiha(controller);
        try {
            miha.run();
        } catch (Exception ex) {
            System.out.println("An error occurred, shutting down: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private MagicMiha(MagicController controller) {
        this.controller = controller;
        mage = new Mage("Interrogator", controller);
    }

    public void run() {
        boolean running = true;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type 'stop' to exit.");
        while (running) {
            System.out.print("> ");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "stop":
                    System.out.println("Exiting application");
                    running = false;
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        }
        scanner.close();
    }
}
