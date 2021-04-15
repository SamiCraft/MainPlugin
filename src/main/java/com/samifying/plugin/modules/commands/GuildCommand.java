package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public abstract class GuildCommand {

    protected final boolean guildSpecific;
    protected final boolean staffOnly;

    public GuildCommand(boolean guildSpecific, boolean staffOnly) {
        this.guildSpecific = guildSpecific;
        this.staffOnly = staffOnly;
    }

    public abstract void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args);

    public boolean isGuildSpecific() {
        return guildSpecific;
    }

    public boolean isStaffOnly() {
        return staffOnly;
    }
}
