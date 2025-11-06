package com.elmakers.mine.bukkit.miha;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.magic.Mage;
import com.elmakers.mine.bukkit.magic.MagicController;
import com.elmakers.mine.bukkit.miha.discord.MagicDiscordController;
import com.elmakers.mine.bukkit.miha.dummy.DummyPlugin;
import com.elmakers.mine.bukkit.miha.dummy.DummyServer;
import com.elmakers.mine.bukkit.miha.dummy.SystemCommandSender;
import com.elmakers.mine.bukkit.miha.evaluate.EvaluateTask;
import com.elmakers.mine.bukkit.miha.platform.Platform;
import com.elmakers.mine.bukkit.utility.ChatUtils;
import com.elmakers.mine.bukkit.utility.CompatibilityLib;
import com.elmakers.mine.bukkit.utility.StringUtils;
import com.elmakers.mine.bukkit.utility.help.Help;
import com.elmakers.mine.bukkit.utility.help.HelpTopic;
import com.elmakers.mine.bukkit.utility.help.HelpTopicMatch;

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
        MagicDiscordController discord = null;
        try (Scanner scanner = new Scanner(System.in)) {
            loadMessages();
            discord = new MagicDiscordController(controller);
            discord.load();
            discord.start();
            sender.sendMessage("Type 'stop' to exit.");
            while (running) {
                System.out.print("> ");
                String command = scanner.nextLine();
                String[] commandAndArgs = command.split(" ");
                command = commandAndArgs[0];
                String[] args = new String[commandAndArgs.length - 1];
                System.arraycopy(commandAndArgs, 1, args, 0, args.length);

                switch (command.toLowerCase()) {
                    case "stop":
                        sender.sendMessage("Exiting application");
                        running = false;
                        break;
                    case "evaluate":
                        int repeat = 0;
                        if (args.length > 0) {
                            try {
                                repeat = Integer.parseInt(args[0]);
                            } catch (Exception ex) {
                                sender.sendMessage("Invalid count: " + args[0]);
                                return;
                            }
                        }
                        evaluate(repeat);
                        break;
                    case "help":
                    case "mhelp":
                        help(args);
                        break;
                    default:
                        sender.sendMessage("Unknown command: " + command);
                        break;
                }
            }
        } catch (Exception ex) {
            sender.sendMessage("An error occurred, shutting down: " + ex.getMessage());
        }
        if (discord != null) {
            discord.shutdown();
        }
    }

    private void help(String[] args) {
        Help help = controller.getMessages().getHelp();
        if (args.length == 1) {
            HelpTopic topic = help.getTopic(args[0]);
            if (topic != null) {
                sender.sendMessage(topic.getText());
                return;
            }
        }

        List<String> keywords = Arrays.asList(ChatUtils.getWords(StringUtils.join(args, " ").toLowerCase()));
        List<HelpTopicMatch> matches = help.findMatches(keywords, 1);
        if (matches.isEmpty()) {
            sender.sendMessage( "No matches found");
        } else {
            sender.sendMessage(matches.get(0).getTopic().getText());
        }
    }

    private void evaluate(int repeat) {
        YamlConfiguration goals = new YamlConfiguration();
        Plugin plugin = controller.getPlugin();
        try {
            goals.load(new InputStreamReader(plugin.getResource("goals.yml")));
            EvaluateTask evaluateTask = new EvaluateTask(sender, plugin, controller, goals, repeat);
            Thread evaluateThread = new Thread(evaluateTask);
            evaluateThread.start();
        } catch (Exception ex) {
            sender.sendMessage("An error occurred evaluating goals: " + ex.getMessage());
        }
    }
}
