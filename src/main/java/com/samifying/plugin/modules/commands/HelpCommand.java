package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.awt.*;

public class HelpCommand extends GuildCommand {

    public HelpCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        if (args.length == 1 && args[0].equals("admin")) {
            channel.sendMessage(new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle(MarkdownUtil.bold("Available admin commands"))
                    .addField("!config", "Configures various properties", false)
                    .addField("!rcon", "Executes a command on the Minecraft server", false)
                    .addField("!whois", "Retrieves data relating verified players", false)
                    .build()).queue();
            return;
        }
        channel.sendMessage(new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(MarkdownUtil.bold("Available commands"))
                .addField("!balance", "Displays your balance", false)
                .addField("!botinvite", "Displays the bot invite link", false)
                .addField("!myid", "Displays your Discord id", false)
                .addField("!seed", "Displays current world seed", false)
                .addField("!spawn", "Displays current world's spawn location", false)
                .addField("!mcstatus/!online", "Displays current Minecraft server status", false)
                .addField("!tutorial", "Sends a link for the Minecraft server plugin tutorial playlist", false)
                .addField("!verify", "Links your Discord account to your Minecraft account", false)
                .addField("!moneyrpc", "Displays a link to the mod's download and tutorial page", false)
                .build()).queue();
    }
}
