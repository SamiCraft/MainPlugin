package com.samifying.plugin.modules;

import com.samifying.plugin.DatabaseAccess;
import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.SQLException;

public class JoinModule extends ListenerAdapter {

    private final MainPlugin plugin;

    public JoinModule(MainPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        if (guild.getId().equals(PluginData.GUILD_ID)) {
            try {
                Member member = event.getMember();
                DatabaseAccess dba = new DatabaseAccess();
                String uuid = dba.retrieveUUID(member.getId());
                dba.close();
                if (uuid != null) {
                    plugin.getLogger().info(String.format("Member %s was found as %s", member.getEffectiveName(), uuid));
                    Role role = guild.getRoleById(PluginData.MINECRAFT_ROLE_ID);
                    if (role != null) {
                        guild.addRoleToMember(member, role).queue();
                        plugin.getLogger().info(String.format("Role %s was added to %s", role.getName(), member.getEffectiveName()));
                    }
                }
            } catch (SQLException | IOException e) {
                plugin.handleException(e);
            }
        }
    }
}
