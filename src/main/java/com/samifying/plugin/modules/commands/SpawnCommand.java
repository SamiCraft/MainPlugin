package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Location;

public class SpawnCommand extends GuildCommand {

    public SpawnCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        Location location = plugin.getServer().getWorlds().get(0).getSpawnLocation();
        channel.sendMessageFormat("World spawn location: **%.2f %.2f %.2f**",
                location.getX(), location.getY(), location.getZ()
        ).queue();
    }
}
