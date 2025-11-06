package com.elmakers.mine.bukkit.miha;

import java.io.InputStreamReader;
import java.util.Scanner;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.magic.Mage;
import com.elmakers.mine.bukkit.magic.MagicController;
import com.elmakers.mine.bukkit.miha.dummy.DummyPlugin;
import com.elmakers.mine.bukkit.miha.dummy.SystemCommandSender;
import com.elmakers.mine.bukkit.miha.evaluate.EvaluateTask;
import com.elmakers.mine.bukkit.miha.platform.Platform;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;

public class MagicMiha {
    private final MagicController controller;
    private final Mage mage;
    private final CommandSender sender;

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
        sender = new SystemCommandSender();
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
                case "evaluate":
                    evaluate();
                    break;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        }
        scanner.close();
    }

    private void evaluate() {
        YamlConfiguration goals = new YamlConfiguration();
        Plugin plugin = controller.getPlugin();
        try {
            goals.load(new InputStreamReader(plugin.getResource("goals.yml")));
            EvaluateTask evaluateTask = new EvaluateTask(sender, plugin, controller, goals, 1);
            Thread evaluateThread = new Thread(evaluateTask);
            evaluateThread.start();
        } catch (Exception ex) {
            System.out.println("An error occurred evaluating goals: " + ex.getMessage());
        }
    }
}
