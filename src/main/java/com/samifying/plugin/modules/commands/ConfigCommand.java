package com.samifying.plugin.modules.commands;

import com.samifying.plugin.MainPlugin;
import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class ConfigCommand extends GuildCommand {

    public ConfigCommand(boolean guildSpecific, boolean staffOnly) {
        super(guildSpecific, staffOnly);
    }

    @Override
    public void execute(MainPlugin plugin, Member member, TextChannel channel, String[] args) {
        if (args.length > 0) {
            if (args[0].equals("verification")) {
                if (args.length == 2 && args[1].equals("enable")) {
                    PluginData.VERIFICATION_ENABLED = true;
                    channel.sendMessage("Verification has been enabled").queue();
                } else if (args.length == 2 && args[1].equals("disable")) {
                    PluginData.VERIFICATION_ENABLED = false;
                    channel.sendMessage("Verification has been disabled").queue();
                } else {
                    channel.sendMessageFormat("Available options: `enable` or `disable`%sCurrently verification is: **%s**",
                            System.lineSeparator(), getVerificationStatus()
                    ).queue();
                }
            }
        } else {
            channel.sendMessage("Invalid arguments, available options: `verification`").queue();
        }
    }

    private String getVerificationStatus() {
        if (PluginData.VERIFICATION_ENABLED) {
            return "Enabled";
        }
        return "Disabled";
    }
}
