package com.elmakers.mine.bukkit.miha;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.magic.Mage;
import com.elmakers.mine.bukkit.magic.MagicController;
import com.elmakers.mine.bukkit.miha.dummy.DummyPlugin;
import com.elmakers.mine.bukkit.miha.dummy.DummyServer;
import com.elmakers.mine.bukkit.miha.dummy.SystemCommandSender;
import com.elmakers.mine.bukkit.miha.evaluate.EvaluateTask;
import com.elmakers.mine.bukkit.miha.platform.Platform;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;

public class MagicMiha {
    private final MagicController controller;
    private final Mage mage;
    private final CommandSender sender;

    public static void main(String[] args) {
        DummyServer server = new DummyServer();
        Bukkit.setServer(server);
        DummyPlugin plugin = new DummyPlugin(server);
        MagicController controller = new MagicController(plugin);
        Platform platform = new Platform(controller);
        CompatibilityLib.initialize(platform);
        MagicMiha miha = new MagicMiha(controller);
        miha.run();
    }

    private MagicMiha(MagicController controller) {
        this.controller = controller;
        sender = new SystemCommandSender();
        mage = new Mage("Interrogator", controller);
    }

    private void loadMessages() throws IOException, InvalidConfigurationException {
        YamlConfiguration messages = CompatibilityLib.getCompatibilityUtils().loadBuiltinConfiguration("defaults/messages.defaults.yml");
        controller.getMessages().load(messages);
    }

    public void run() {
        boolean running = true;
        try (Scanner scanner = new Scanner(System.in)) {
            loadMessages();
            sender.sendMessage("Type 'stop' to exit.");
            while (running) {
                System.out.print("> ");
                String command = scanner.nextLine();

                switch (command.toLowerCase()) {
                    case "stop":
                        sender.sendMessage("Exiting application");
                        running = false;
                        break;
                    case "evaluate":
                        evaluate();
                        break;
                    default:
                        sender.sendMessage("Unknown command: " + command);
                        break;
                }
            }
        } catch (Exception ex) {
            sender.sendMessage("An error occurred, shutting down: " + ex.getMessage());
        }
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
            sender.sendMessage("An error occurred evaluating goals: " + ex.getMessage());
        }
    }
}
