package com.samifying.plugin.events;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;

public class DiscordBridge implements Listener {

    private final MainPlugin plugin;
    private final Server server;
    private final JDA jda;

    public DiscordBridge(MainPlugin plugin) {
        this.plugin = plugin;
        this.server = plugin.getServer();
        this.jda = plugin.getJda();
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = retrieveUser(player);
        if (user != null) {
            sendEmbed(new EmbedBuilder()
                    .setColor(Color.GREEN)
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setTitle(MarkdownUtil.bold(player.getName() + " joined").toUpperCase())
                    .addField("Currently online:", server.getOnlinePlayers().size() + "/" + server.getMaxPlayers(), false)
                    .setTimestamp(Instant.now())
                    .build()
            );
        }
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = retrieveUser(player);
        if (user != null) {
            sendEmbed(new EmbedBuilder()
                    .setColor(Color.RED)
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setTitle(MarkdownUtil.bold(player.getName() + " left").toUpperCase())
                    .addField("Currently online:", server.getOnlinePlayers().size() - 1 + "/" + server.getMaxPlayers(), false)
                    .setTimestamp(Instant.now())
                    .build()
            );
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getEntity();
        User user = retrieveUser(player);
        Location location = player.getLocation();
        World world = location.getWorld();
        if (user != null) {
            sendEmbed(new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                    .setTitle(MarkdownUtil.bold(player.getName() + " died").toUpperCase())
                    .setDescription(ChatColor.stripColor(event.getDeathMessage()))
                    .setTimestamp(Instant.now())
                    .build()
            );
            if (world != null) {
                plugin.getLogger().info(String.format(
                        "Player [%s (%s)] died in world [%s] at [%.2f %.2f %.2f]",
                        player.getName(),
                        user.getAsTag(),
                        world.getName(),
                        location.getX(),
                        location.getY(),
                        location.getZ()));
            }
        }
    }

    private void sendEmbed(MessageEmbed embed) {
        TextChannel channel = jda.getTextChannelById(PluginData.NOTIFICATIONS_CHANNEL_ID);
        if (channel != null) {
            channel.sendMessage(embed).queue();
        }
    }

    private User retrieveUser(Player player) {
        try {
            DatabaseAccess access = new DatabaseAccess();
            String userId = access.retrieveDiscordId(player.getUniqueId().toString().replace("-", ""));
            access.close();
            return jda.retrieveUserById(userId).complete();
        } catch (SQLException | IOException e) {
            plugin.handleException(e);
        }
        return null;
    }
}
