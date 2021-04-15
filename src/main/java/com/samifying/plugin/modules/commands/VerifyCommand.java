package com.samifying.plugin.modules.commands;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.bukkit.ChatColor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;

public class VerifyCommand extends GuildCommand {

    public VerifyCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        if (PluginData.VERIFICATION_ENABLED) {
            User user = member.getUser();
            if (member.getRoles().stream().anyMatch(role -> role.getId().equals(PluginData.LEVEL_10_ROLE_ID))) {
                if (args.length == 1) {
                    try {
                        // Fixing up any typos and receiving the mc uuid
                        JSONParser parser = new JSONParser();
                        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + args[0]);
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

                        // If the username exists
                        if (uuid != null && username != null) {

                            // Inserting into the database
                            DatabaseAccess access = new DatabaseAccess();

                            // If the person is already verified
                            String possibleDiscordId = access.retrieveDiscordId(uuid);
                            if (possibleDiscordId != null && possibleDiscordId.equals(user.getId())) {
                                channel.sendMessage("You are already verified").queue();
                                access.close();
                                return;
                            }

                            access.insertData(uuid, user.getId());
                            access.close();

                            // Success notification
                            Guild guild = channel.getGuild();
                            channel.sendMessage(member.getAsMention()).embed(
                                    new EmbedBuilder()
                                            .setColor(Color.GREEN)
                                            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                                            .setTitle(MarkdownUtil.bold("SUCCESS"))
                                            .setDescription(MarkdownUtil.bold("You have successfully linked your Minecraft account. You can now join the server!"))
                                            .addField("IP:", "play.samifying.com", false)
                                            .addField("Username:", username, false)
                                            .addField("Discord ID", user.getId(), false)
                                            .setFooter(guild.getName(), guild.getIconUrl())
                                            .setTimestamp(Instant.now())
                                            .build()
                            ).queue();

                            //Adding a role
                            Role mc = guild.getRoleById(PluginData.MINECRAFT_ROLE_ID);
                            if (mc != null) {
                                guild.addRoleToMember(member, mc).queue();
                            }

                            // Notifying the people on the server
                            plugin.getServer().broadcastMessage(ChatColor.AQUA + username + " just verified");
                        } else {
                            channel.sendMessage("Please try again, seams like you made a typo").queue();
                        }

                    } catch (Exception e) {
                        plugin.handleException(e);
                        channel.sendMessage("There was a temporary error, seek staff support").queue();
                    }

                } else {
                    channel.sendMessage(member.getAsMention()).embed(
                            new EmbedBuilder()
                                    .setColor(Color.ORANGE)
                                    .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                                    .setTitle(MarkdownUtil.bold("COMMAND USAGE"))
                                    .setDescription(MarkdownUtil.bold("This command will link your Minecraft account to your discord account. " +
                                            "Please use the following format:" +
                                            System.lineSeparator() +
                                            MarkdownUtil.codeblock("!verify <your-minecraft-username>")) +
                                            MarkdownUtil.italics("Minecraft usernames are **CaSe SeNsItIvE** !!!"))
                                    .setTimestamp(Instant.now())
                                    .build()
                    ).queue();
                }
            } else {
                channel.sendMessage("Sorry, you have to be Level 10 (Bronze II) in order to verify").queue();
            }
        } else {
            channel.sendMessage("At the moment verification has been disabled").queue();
        }
    }
}
