package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Arrays;
import java.util.List;

public class BotInviteCommand extends GuildCommand {

    public BotInviteCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        List<Permission> perms = Arrays.asList(
                Permission.MESSAGE_READ,
                Permission.MESSAGE_WRITE,
                Permission.MESSAGE_EMBED_LINKS
        );
        channel.sendMessageFormat("My invite link: **%s**", channel.getJDA().getInviteUrl(perms)).queue();
    }
}
