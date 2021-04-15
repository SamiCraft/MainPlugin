package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class MyIdCommand extends GuildCommand {

    public MyIdCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        channel.sendMessageFormat("You Discord id: **%s**", member.getId()).queue();
    }
}
