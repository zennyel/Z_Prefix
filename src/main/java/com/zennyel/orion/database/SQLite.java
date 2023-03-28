package com.zennyel.orion.database;

import com.zennyel.orion.Z_Prefix;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.*;

public class SQLite {

    private Connection connection;
    private Z_Prefix instance;

    public SQLite(Z_Prefix instance) {
        this.instance = instance;
    }

    public void createTable(){
        String sql = "CREATE TABLE IF NOT EXISTS prefix_db(uuid VARCHAR(36),prefix VARCHAR(36))";
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("&cError creating prefix table!");
        }
    }


    public void clearPrefix(Player player) {
        String sql = "DELETE FROM prefix_db WHERE uuid=?";
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, player.getUniqueId().toString());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cError deleting prefix for " + player + " on database");
        }
    }

    public void setPrefix(Player player,String prefix){
        String sql = "REPLACE INTO prefix_db(uuid,prefix) VALUES (?,?)";
        try(Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(sql)){
            ps.setString(1, player.getUniqueId().toString());
            ps.setString(2, prefix);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cError inserting " + player + " on database");
            e.printStackTrace();
        }
    }

    public String getPrefix(Player player) {
        String sql = "SELECT prefix FROM prefix_db WHERE uuid = ?";
        try (Connection connection = getConnection(); PreparedStatement stm = connection.prepareStatement(sql)) {
            stm.setString(1, player.getUniqueId().toString());
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("prefix");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cError while getting prefix from " + player);
        }
        return null;
    }


    public void disconnect() {
        try {
            if (connection != null) {
                connection.close();
                Bukkit.getConsoleSender().sendMessage("§a§lDisconnecting of database!");
            }
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("§a§lDisconnecting of database!");
        }
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                File dataFolder = new File(instance.getDataFolder(), "database.db");
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cError connecting to database: " + e.getMessage());
        }
        return connection;
    }

}
