package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class TutorialCommand extends GuildCommand {

    public TutorialCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        channel.sendMessageFormat("Tutorial playlist:%s**%s**",
                System.lineSeparator(),
                "https://youtube.com/playlist?list=PLtys1INBP_8hxX5dQ_Bd7AI1EJCuemHDK"
        ).queue();
    }
}
