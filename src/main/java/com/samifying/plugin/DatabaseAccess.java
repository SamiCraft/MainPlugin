package com.samifying.plugin;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;

public class DatabaseAccess {

    private final Connection connection;

    public DatabaseAccess() throws SQLException, IOException {
        String url = PluginData.getInstance().getMysqlConnectionUrl();
        this.connection = DriverManager.getConnection(url);
    }

    public void insertData(String uuid, String discordId) throws SQLException {
        String sql = "INSERT INTO `twitch_link`.`data` (`uuid`, `discord_id`, `time`) VALUES (?, ?, ?);";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, uuid);
        ps.setString(2, discordId);
        ps.setString(3, String.valueOf(Instant.now().toEpochMilli()));
        ps.executeUpdate();
        ps.close();
    }

    public String retrieveDiscordId(String uuid) throws SQLException {
        String sql = "SELECT `discord_id` FROM `twitch_link`.`data` WHERE uuid=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, uuid);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            return resultSet.getString("discord_id");
        }
        return null;
    }

    public String retrieveUUID(String discordId) throws SQLException {
        String sql = "SELECT `uuid` FROM `twitch_link`.`data` WHERE discord_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, discordId);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            return resultSet.getString("uuid");
        }
        return null;
    }

    public String retrieveVerificationTimestamp(String discordId) throws SQLException {
        String sql = "SELECT `discord_id` FROM `twitch_link`.`data` WHERE discord_id=?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, discordId);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            return resultSet.getString("timestamp");
        }
        return null;
    }

    public void close() throws SQLException {
        connection.close();
    }

}
