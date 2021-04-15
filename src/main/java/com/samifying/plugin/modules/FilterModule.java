package com.samifying.plugin.modules;

import com.samifying.plugin.PluginData;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class FilterModule extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        User author = event.getAuthor();
        if (channel.getId().equals(PluginData.TWITCH_CLIPS_CHANNEL_ID) && !author.getId().equals(PluginData.SAMIFYING_USER_ID)) {
            Message message = event.getMessage();
            String raw = message.getContentRaw();
            if (!raw.contains("https://clips.twitch.tv/")) {
                message.delete().queue();
                author.openPrivateChannel().queue(
                        privateChannel -> privateChannel.sendMessage("This channel's messages ("
                                + channel.getName()
                                + ") can only contain links from **Twitch Clips** (https://clips.twitch.tv/)").queue()
                );
            }
        }
    }
}
