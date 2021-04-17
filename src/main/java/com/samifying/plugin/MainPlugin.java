package com.samifying.plugin;

import com.samifying.plugin.events.DiscordBridge;
import com.samifying.plugin.events.LootModification;
import com.samifying.plugin.events.PlayerLogin;
import com.samifying.plugin.modules.CommandModule;
import com.samifying.plugin.modules.FilterModule;
import com.samifying.plugin.modules.JoinModule;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.SelfUser;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.logging.Logger;

public final class MainPlugin extends JavaPlugin {

    private Logger logger;
    private Server server;
    private Economy economy;
    private JDA jda;

    @Override
    public void onEnable() {

        // Loading all the properties
        logger = getLogger();
        server = getServer();

        // Checking for Vault presence
        if (!setupEconomy()) {
            logger.warning("This plugin requires Vault");
            logger.warning("Plugin will exit");
            server.getPluginManager().disablePlugin(this);
            return;
        }
        logger.info("Found Vault as economy manager");

        // Discord bot connection
        logger.info("Connecting to Discord API");
        try {
            jda = JDABuilder.createDefault(PluginData.getInstance().getDiscordToken())
                    .setActivity(Activity.listening("!mchelp"))
                    .addEventListeners(new CommandModule(this))
                    .addEventListeners(new FilterModule())
                    .addEventListeners(new JoinModule(this))
                    .build();
            try {
                jda.awaitReady();
                logger.info("Successfully connected to Discord API");
                SelfUser bot = jda.getSelfUser();
                logger.info("Name: " + bot.getName());
                logger.info("ID: " + bot.getId());
                logger.info("Servers: " + jda.getGuilds().size());
            } catch (InterruptedException e) {
                handleException(e);
            }
        } catch (LoginException | IOException e) {
            handleException(e);
        }

        // Creating the balance API endpoint at v1/balance
        logger.info("Enabling the balance API endpoint");
        Spark.get("v1/balance", "application/json", (request, response) -> {
            // Retrieving the uuid
            DatabaseAccess dba = new DatabaseAccess();
            String uuid = dba.retrieveUUID(request.queryParams("id"));
            dba.close();
            if (uuid != null) {
                // Retrieving the player
                OfflinePlayer subject = null;
                for (OfflinePlayer player : server.getOfflinePlayers()) {
                    if (player.getUniqueId().toString().replace("-", "").equals(uuid)) {
                        // its a match
                        subject = player;
                    }
                }
                //Check if the player was found
                if (subject != null) {
                    double balance = economy.getBalance(subject);
                    return String.format("%.0f", balance);
                }
            }
            return "Unknown";
        });

        // Registering events
        PluginManager manager = getServer().getPluginManager();
        logger.info("Registering events");
        manager.registerEvents(new DiscordBridge(this), this);
        manager.registerEvents(new PlayerLogin(this), this);
        manager.registerEvents(new LootModification(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (jda != null) {
            logger.info("Disconnecting from Discord API");
            jda.shutdownNow();
        }
        logger.info("Closing the balance API endpoint");
        Spark.stop();
    }

    public void handleException(Exception e) {
        logger.severe("Exception occurred: " + e.getClass().getName());
        logger.severe("With message: " + e.getMessage());
        e.printStackTrace();
    }

    public JDA getJda() {
        return jda;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = server.getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public Economy getEconomy() {
        return economy;
    }
}
