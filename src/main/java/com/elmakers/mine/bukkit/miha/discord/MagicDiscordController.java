package com.elmakers.mine.bukkit.miha.discord;

import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.elmakers.mine.bukkit.magic.MagicController;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class MagicDiscordController {
    private final MagicController magic;
    private String token;
    private String channel;
    private String commandChannel;
    private String mentionChannel;
    private String mentionId;
    private String reactionChannel;
    private String ignoreChannel;
    private String reactionEmote;
    private Emoji reactionEmoji;
    private String joinRole;
    private String joinChannel;
    private String guildId;
    private String command;
    private ConfigurationSection responseChannels;
    private boolean debug;
    private JDA jda = null;
    private Thread jdaThread = null;

    public MagicDiscordController(MagicController magic) {
        this.magic = magic;
        load();
    }

    public void shutdown() {
        if (jda != null) {
            jda.shutdownNow();
        }
        if (jdaThread != null) {
            jdaThread.interrupt();
            jdaThread = null;
        }
    }

    public void load() {
        Plugin plugin = magic.getPlugin();
        plugin.saveDefaultConfig();
        YamlConfiguration messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(new InputStreamReader(plugin.getResource("messages.yml"), "UTF-8"));
            magic.getMessages().load(messagesConfig);
        } catch (Exception ex) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load messages.yml resource", ex);
        }

        ConfigurationSection config = plugin.getConfig();
        token = config.getString("token", "");
        channel = config.getString("channel", "");
        commandChannel = config.getString("command_channel", "*");
        reactionChannel = config.getString("reaction_channel", "*");
        mentionChannel = config.getString("mention_channel", "*");
        ignoreChannel = config.getString("ignore_channel", "");
        reactionEmote = config.getString("reaction_emote", "");
        if (!reactionEmote.isEmpty()) {
            reactionEmoji = Emoji.fromFormatted(reactionEmote);
        }
        guildId = config.getString("guild", "");
        joinRole = config.getString("join_role", "");
        joinChannel = config.getString("join_channel", "");
        mentionId = config.getString("mention_id", "");
        command = config.getString("command", "mhelp");
        debug = config.getBoolean("debug", false);
        responseChannels = config.getConfigurationSection("response_channels");
    }

    public void start() {
        if (token == null || token.isEmpty()) {
            getLogger().warning("Please put your bot token in config.yml, otherwise this plugin can't work");
        } else {
            jdaThread = new Thread(new JDAConnector(this));
            jdaThread.start();
        }

        if (joinChannel != null && !joinChannel.isEmpty()) {
            getLogger().info("Sending join messages to " + joinChannel);
        }
    }

    public String getToken() {
        return token;
    }

    public String getChannel() {
        return channel;
    }

    public String getWelcomeChannel() {
        return joinChannel != null && !joinChannel.isEmpty() ? joinChannel : channel;
    }

    public String getReactionChannel() {
        return reactionChannel;
    }

    public String getIgnoreChannel() {
        return ignoreChannel;
    }

    public String getReactionEmote() {
        return reactionEmote;
    }

    public Emoji getReactionEmoji() {
        return reactionEmoji;
    }

    public String getCommandChannel() {
        return commandChannel;
    }

    public String getMentionChannel() {
        return mentionChannel;
    }

    public String getMentionId() {
        return mentionId;
    }

    public String getJoinRole() {
        return joinRole;
    }

    public String getCommand() {
        return command;
    }

    public String getGuild() {
        return guildId;
    }

    public String getChannelResponse(String channel) {
        return responseChannels == null ? null : responseChannels.getString(channel);
    }

    public boolean isDebug() {
        return debug;
    }

    public MagicController getMagic() {
        return magic;
    }

    protected void setJDA(JDA jda) {
        this.jda = jda;
        jda.getPresence().setActivity(Activity.playing(magic.getMessages().get("discord.status")));
        getLogger().info("Connected to the Discord server!");
    }

    public Logger getLogger() {
        return magic.getPlugin().getLogger();
    }
}
