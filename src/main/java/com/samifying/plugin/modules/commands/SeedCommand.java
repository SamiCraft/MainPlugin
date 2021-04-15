package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class SeedCommand extends GuildCommand {

    public SeedCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        channel.sendMessageFormat("World seed: **%s**", plugin.getServer().getWorlds().get(0).getSeed()).queue();
    }
}
