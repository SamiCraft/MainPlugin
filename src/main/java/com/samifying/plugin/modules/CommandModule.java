package com.samifying.plugin.modules;

import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import com.samifying.plugin.modules.commands.*;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

public class CommandModule extends ListenerAdapter {

    private final MainPlugin plugin;
    private final HashMap<String, GuildCommand> map;

    public CommandModule(MainPlugin plugin) {
        this.plugin = plugin;
        this.map = new HashMap<>();
        StatusCommand status = new StatusCommand(false, false);
        map.put("!online", status);
        map.put("!mcstatus", status);
        map.put("!seed", new SeedCommand(false, false));
        map.put("!botinvite", new BotInviteCommand(false, false));
        map.put("!config", new ConfigCommand(true, true));
        map.put("!spawn", new SpawnCommand(false, false));
        map.put("!tutorial", new TutorialCommand(false, false));
        map.put("!verify", new VerifyCommand(true, false));
        map.put("!whois", new WhoisCommand(true, true));
        map.put("!rcon", new RconCommand(true, true));
        map.put("!balance", new BalanceCommand(true, false));
        map.put("!myid", new MyIdCommand(false, false));
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            String[] message = event.getMessage().getContentRaw().trim().split("\\s+");
            GuildCommand command = map.get(message[0]);
            TextChannel channel = event.getChannel();
            Member member = event.getMember();
            if (command != null && isExecutable(command, member, channel)) {
                command.execute(plugin, member, channel, Arrays.copyOfRange(message, 1, message.length));
            }
        }
    }

    private boolean isExecutable(GuildCommand command, Member member, TextChannel channel) {
        if (command.isStaffOnly()) {
            // Checking if executors are admins
            String id = member.getId();
            return id.equals(PluginData.SAMIFYING_USER_ID) || id.equals(PluginData.PEQULA_USER_ID);
        }
        if (channel.getGuild().getId().equals(PluginData.GUILD_ID)) {
            // In the official discord server
            String id = channel.getId();
            return id.equals(PluginData.BOT_CHANNEL_ID) || id.equals(PluginData.STAFF_CHANNEL_ID);
        }
        return !(command.isGuildSpecific());
    }
}
