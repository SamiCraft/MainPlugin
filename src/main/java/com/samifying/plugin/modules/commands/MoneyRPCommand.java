package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class MoneyRPCommand extends GuildCommand {

    public MoneyRPCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        channel.sendMessageFormat("You can find instructions on how to install and download this Fabric mod here:%s **%s**",
                System.lineSeparator(),
                "https://github.com/SamiCraft/MoneyRPC").queue();
    }
}
