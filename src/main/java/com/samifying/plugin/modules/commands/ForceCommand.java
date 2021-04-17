package com.samifying.plugin.modules.commands;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class ForceCommand extends GuildCommand {

    public ForceCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        if (args.length > 0) {
            JDA jda = channel.getJDA();
            if (args[0].equals("verify")) {
                if (args.length == 3) {
                    try {
                        DatabaseAccess access = new DatabaseAccess();
                        access.insertData(args[1], args[2]);
                        access.close();
                        addMinecraftRole(args[1], jda);
                        channel.sendMessage("Success, the person has been manually verified").queue();
                    } catch (Exception e) {
                        plugin.handleException(e);
                        channel.sendMessage("Error: " + e.getMessage()).queue();
                    }
                } else {
                    channel.sendMessage("Usage: `!force verify <discord-id> <minecraft-id>`").queue();
                }
                return;
            }
            if (args[0].equals("role")) {
                if (args.length == 2) {
                    addMinecraftRole(args[1], jda);
                    channel.sendMessage("Success, role has been added").queue();
                } else {
                    channel.sendMessage("Usage: `!force role <discord-id>`").queue();
                }
                return;
            }
            if (args[0].equals("delete")) {
                channel.sendMessage("Feature not yet implemented").queue();
            }
        } else {
            channel.sendMessage("Bad arguments, options: `verify`, `role` or 'delete'").queue();
        }
    }

    private void addMinecraftRole(String id, JDA jda) {
        Guild guild = jda.getGuildById(PluginData.GUILD_ID);
        if (guild != null) {
            guild.retrieveMemberById(id).queue(member -> {
                Role role = guild.getRoleById(PluginData.MINECRAFT_ROLE_ID);
                if (role != null) {
                    guild.addRoleToMember(member, role).queue();
                }
            });
        }
    }
}
