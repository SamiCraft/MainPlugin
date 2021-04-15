package com.samifying.plugin.modules.commands;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.bukkit.OfflinePlayer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.time.Instant;

public class WhoisCommand extends GuildCommand {

    public WhoisCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        if (args.length == 2) {
            if (args[0].equals("discord")) {
                try {
                    DatabaseAccess access = new DatabaseAccess();
                    String id = args[1];
                    String uuid = access.retrieveUUID(id);
                    access.close();
                    if (uuid != null) {
                        for (OfflinePlayer p : plugin.getServer().getOfflinePlayers()) {
                            if (p.getUniqueId().toString().replace("-", "").equals(uuid)) {
                                sendEmbed(channel, id, p.getName());
                                return;
                            }
                        }
                    } else {
                        channel.sendMessage("No data found for selected user!").queue();
                    }
                } catch (SQLException | IOException e) {
                    plugin.handleException(e);
                    channel.sendMessage("Temporary database error").queue();
                }
            }
            if (args[0].equals("minecraft")) {
                try {
                    JSONParser parser = new JSONParser();
                    URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[1]);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
                    String lines;
                    String uuid = null;
                    String username = null;
                    while ((lines = reader.readLine()) != null) {
                        JSONArray array = new JSONArray();
                        array.add(parser.parse(lines));
                        for (Object o : array) {
                            JSONObject jsonObject = (JSONObject) o;
                            username = (String) jsonObject.get("name");
                            uuid = (String) jsonObject.get("id");
                        }
                    }

                    if (uuid != null && username != null) {
                        DatabaseAccess access = new DatabaseAccess();
                        String userId = access.retrieveDiscordId(uuid);
                        access.close();
                        if (userId != null) {
                            sendEmbed(channel, userId, username);
                        } else {
                            channel.sendMessage("Person is not verified").queue();
                        }
                    } else {
                        channel.sendMessage("No data found for selected user!").queue();
                    }
                } catch (Exception e) {
                    plugin.handleException(e);
                    channel.sendMessage("Temporary database error").queue();
                }
            }
        } else {
            channel.sendMessage("Command usages: `!whois minecraft <minecraft-username>` or `!whois discord <discord-id>`").queue();
        }
    }

    private void sendEmbed(TextChannel channel, String discordId, String minecraftUsername) {
        User user = channel.getJDA().retrieveUserById(discordId).complete();
        Guild guild = channel.getGuild();
        channel.sendMessage(new EmbedBuilder()
                .setColor(Color.ORANGE)
                .setTitle(MarkdownUtil.bold("User Data"))
                .addField("Minecraft Username:", minecraftUsername, false)
                .addField("Discord Username:", user.getAsTag(), false)
                .setThumbnail(user.getEffectiveAvatarUrl())
                .setFooter(guild.getName(), guild.getIconUrl())
                .setTimestamp(Instant.now())
                .build()).queue();
    }
}
