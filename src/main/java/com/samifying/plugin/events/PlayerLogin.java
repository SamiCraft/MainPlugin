package com.samifying.plugin.events;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerLogin implements Listener {

    private final MainPlugin plugin;

    public PlayerLogin(MainPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncPlayerPreLoginEvent(AsyncPlayerPreLoginEvent event) {
        try {
            DatabaseAccess connection = new DatabaseAccess();
            String userId = connection.retrieveDiscordId(event.getUniqueId().toString().replace("-", ""));
            connection.close();
            if (userId != null) {
                Guild guild = plugin.getJda().getGuildById(PluginData.GUILD_ID);
                if (guild != null) {
                    Member member = guild.retrieveMemberById(userId).complete();
                    if (member != null) {
                        if (member.getRoles().stream().anyMatch(role -> role.getId().equals(PluginData.LEVEL_10_ROLE_ID))) {
                            // Member has joined
                            plugin.getLogger().info(String.format("Discord member %s (%s) logged in successfully", member.getEffectiveName(), member.getId()));
                            plugin.getServer().broadcastMessage(
                                    String.format("%s%s (%s) joined as %s", ChatColor.AQUA, member.getUser().getAsTag(), member.getEffectiveName(), event.getName())
                            );
                        } else {
                            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You need to be Level 10 (Bronze II) in order to play");
                        }
                    } else {
                        event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You are not a Discord server member");
                    }
                } else {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Internal error, Discord server unavailable");
                }
            } else {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You are not verified");
            }
        } catch (Exception e) {
            plugin.handleException(e);
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
