package com.samifying.plugin.modules.commands;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.OfflinePlayer;

import java.io.IOException;
import java.sql.SQLException;

public class BalanceCommand extends GuildCommand {

    public BalanceCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        try {
            DatabaseAccess dba = new DatabaseAccess();
            String uuid = dba.retrieveUUID(member.getId());
            dba.close();
            if (uuid != null) {
                OfflinePlayer subject = null;
                for (OfflinePlayer player : plugin.getServer().getOfflinePlayers()) {
                    if (player.getUniqueId().toString().replace("-", "").equals(uuid)) {
                        // its a match
                        subject = player;
                    }
                }
                if (subject != null) {
                    double balance = plugin.getEconomy().getBalance(subject);
                    channel.sendMessageFormat("Balance on the Minecraft server for **%s [%s]** is: **$%.0f**", member.getUser().getAsTag(), member.getEffectiveName(), balance).queue();
                } else {
                    channel.sendMessage("You have never joined the server").queue();
                }
            } else {
                channel.sendMessage("You are not verified").queue();
            }
        } catch (SQLException | IOException e) {
            plugin.handleException(e);
            channel.sendMessage("Error occurred while executing the command").queue();
        }
    }
}
