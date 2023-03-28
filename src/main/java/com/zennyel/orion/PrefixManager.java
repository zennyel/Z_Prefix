package com.zennyel.orion;

import com.zennyel.orion.database.SQLite;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class PrefixManager {
    private SQLite sql;
    private FileConfiguration configuration;

    public PrefixManager(SQLite sql, FileConfiguration configuration) {
        this.sql = sql;
        this.configuration = configuration;
    }

    public void loadPrefix(Player player) {
        if(sql.getPrefix(player) == null){
            player.setPlayerListName(player.getName());
            player.setCustomName(player.getName());
            return;
        }
        int color = configuration.getInt("Config.border_colors");
        String prefix = "§" + color +"[" + sql.getPrefix(player).replace("&", "§") + "§" + color +"] "+ "§7";
        if(configuration.getBoolean("Config.above_head")) {
            player.setCustomName(prefix + player.getName());
        }
        player.setCustomNameVisible(true);
        if(configuration.getBoolean("Config.tabPrefix")){
            player.setPlayerListName(prefix + player.getName());
        }
    }


}
