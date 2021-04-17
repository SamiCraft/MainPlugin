package com.samifying.plugin;

import java.io.*;
import java.util.Properties;

public class PluginData {

    public static final String GUILD_ID = "264801645370671114";
    public static final String BOT_CHANNEL_ID = "400395820979191808";
    public static final String STAFF_CHANNEL_ID = "377127195346337792";
    public static final String NOTIFICATIONS_CHANNEL_ID = "810614278271926282";
    public static final String TWITCH_CLIPS_CHANNEL_ID = "823996140928106526";
    public static final String LEVEL_10_ROLE_ID = "712696663440883773";
    public static final String MINECRAFT_ROLE_ID = "804343293886464052";
    public static final String SAMIFYING_USER_ID = "179261209529417729";
    public static final String PEQULA_USER_ID = "358236836113547265";
    public static boolean VERIFICATION_ENABLED = true;

    private static PluginData instance = null;

    private final String discordToken;
    private final String mysqlConnectionUrl;

    private PluginData(String discordToken, String mysqlConnectionUrl) {
        this.discordToken = discordToken;
        this.mysqlConnectionUrl = mysqlConnectionUrl;
    }

    public static PluginData getInstance() throws IOException {
        if (instance == null) {
            //Reading configuration from file
            File config = new File("plugin.properties");
            Properties prop = new Properties();
            if (!config.exists()) {
                try (OutputStream output = new FileOutputStream(config)) {
                    prop.setProperty("discord.token", "token");
                    prop.setProperty("mysql.url", "jdbc:mysql://");
                    prop.store(output, "Main plugin configuration file");
                }
            }
            try (InputStream input = new FileInputStream(config)) {
                prop.load(input);
                instance = new PluginData(
                        prop.getProperty("discord.token"),
                        prop.getProperty("mysql.url"));
            }
        }
        return instance;
    }

    public String getDiscordToken() {
        return discordToken;
    }

    public String getMysqlConnectionUrl() {
        return mysqlConnectionUrl;
    }
}
