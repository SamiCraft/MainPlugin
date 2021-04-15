package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.bukkit.Server;

import java.awt.*;
import java.time.Instant;

public class RconCommand extends GuildCommand {

    public RconCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        if (args.length >= 2) {
            Server server = plugin.getServer();
            User author = member.getUser();
            Guild guild = channel.getGuild();
            server.getScheduler().runTask(plugin, () -> {
                if (server.dispatchCommand(server.getConsoleSender(), String.join(" ", args))) {
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.GREEN)
                                    .setAuthor(author.getName(), null, author.getAvatarUrl())
                                    .setTitle(MarkdownUtil.bold("SUCCESS"))
                                    .setDescription(MarkdownUtil.bold("Command executed successfully!"))
                                    .setFooter(guild.getName(), guild.getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .build()
                    ).queue();
                } else {
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setAuthor(author.getName(), null, author.getAvatarUrl())
                                    .setTitle(MarkdownUtil.bold("ERROR"))
                                    .setDescription(MarkdownUtil.bold("Command execution failed, please check if the command exists!"))
                                    .setFooter(guild.getName(), guild.getIconUrl())
                                    .setTimestamp(Instant.now())
                                    .build()
                    ).queue();
                }
            });
        } else {
            channel.sendMessage("Not enough arguments").queue();
        }
    }
}
