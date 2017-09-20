package com.matsg.nationsbans.sql;

import com.matsg.nationsbans.Ban;
import com.matsg.nationsbans.NationsBans;
import com.matsg.nationsbans.SettingsManager;
import com.matsg.nationsbans.config.SQLConfig;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.*;
import java.util.*;

public class SQLManager {

    private static SQLManager instance;
    private final String ip, database, username, password;
    private Connection connection;
    private SQLConfig sql;

    private SQLManager(Plugin plugin) {
        this.sql = SettingsManager.getInstance().getSQLConfig();
        this.ip = sql.getDatabaseIP();
        this.database = sql.getDatabaseName();
        this.username = sql.getUsername();
        this.password = sql.getPassword();

        try {
            connection = openConnection();

            plugin.getLogger().info("Connection to database established.");
        } catch (Exception e) {
            plugin.getLogger().warning("Can not connect to the SQL database! Have you configured everything correctly?");
        } finally {
            closeConnection();
        }
    }

    public static SQLManager getInstance() {
        if (instance == null) {
            instance = new SQLManager(NationsBans.getPlugin());
        }
        return instance;
    }

    public void closeConnection() {
        try {
            if (isConnected()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Ban getBan(String name) {
        try {
            connection = openConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE PlayerName = ? AND PardonDate > CURRENT_TIMESTAMP;");
            ps.setString(1, name);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Ban ban = new Ban(
                    UUID.fromString(rs.getString("PlayerUUID")),
                    rs.getString("PlayerName"),
                    rs.getString("StaffName"),
                    rs.getString("Reason"),
                    rs.getTimestamp("BanDate"),
                    rs.getTimestamp("PardonDate")
            );

            ps.close();
            rs.close();

            return ban;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    public Ban getBan(UUID uuid) {
        try {
            connection = openConnection();

            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE PlayerUUID = ? AND PardonDate > CURRENT_TIMESTAMP;");
            ps.setString(1, uuid.toString());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            Ban ban = new Ban(
                    UUID.fromString(rs.getString("PlayerUUID")),
                    rs.getString("PlayerName"),
                    rs.getString("StaffName"),
                    rs.getString("Reason"),
                    rs.getTimestamp("BanDate"),
                    rs.getTimestamp("PardonDate")
            );

            ps.close();
            rs.close();

            return ban;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
        return null;
    }

    public boolean isConnected() throws SQLException {
        return connection != null || !connection.isClosed();
    }

    public Connection openConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database, username, password);
        return connection;
    }

    public void registerBan(Ban ban) {
        try {
            connection = openConnection();

            PreparedStatement ps = connection.prepareStatement("INSERT INTO `bans`(PlayerUUID, PlayerName, StaffName, Reason, BanDate, PardonDate) VALUES(?, ?, ?, ?, ?, ?);");
            ps.setString(1, ban.getPlayerUUID().toString());
            ps.setString(2, ban.getPlayerName());
            ps.setString(3, ban.getStaffName());
            ps.setString(4, ban.getReason());
            ps.setTimestamp(5, ban.getBanDate());
            ps.setTimestamp(6, ban.getPardonDate());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public void removeBan(Ban ban) {
        try {
            connection = openConnection();

            //We want the record to continue to exist in the database, so update the pardon date rather than removing the ban
            PreparedStatement ps = connection.prepareStatement("UPDATE `bans` SET PardonDate = CURRENT_TIMESTAMP WHERE PlayerUUID = ? AND PardonDate > CURRENT_TIMESTAMP;");
            ps.setString(1, ban.getPlayerUUID().toString());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }
}