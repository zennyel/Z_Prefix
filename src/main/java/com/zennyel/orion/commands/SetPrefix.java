package com.zennyel.orion.commands;

import com.zennyel.orion.PrefixManager;
import com.zennyel.orion.Z_Prefix;
import com.zennyel.orion.database.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SetPrefix implements CommandExecutor {

    private FileConfiguration configuration;
    private String tag;
    private Z_Prefix instance;
    private SQLite sql;
    private PrefixManager manager;

    public SetPrefix(Z_Prefix instance) {
        this.instance = instance;
        this.sql = instance.getSql();
        this.manager = instance.getManager();
        this.configuration = instance.getConfig();
        this.tag = configuration.getString("Config.tag").replace("&", "§");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(commandSender instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("Only can be used by players");
            return false;
        }

        Player player = (Player) commandSender;

        if(!player.hasPermission("prefix.use")){
            player.sendMessage(tag + " §cYou don't have permission to use this command!");
            return false;
        }

        if(args.length == 0){
            player.sendMessage(tag + " §cWrong command usage, use /setprefix <prefix>!");
            return false;
        }

        String prefix = args[0];

        if(hasPrefix(player)){
            player.sendMessage(tag + " §cYou already have a prefix set!");
            return false;
        }

        int maxLength = configuration.getInt("Config.max_length");
        if(args[0].length() > maxLength){
            player.sendMessage(tag + " §cMax length exceed, the prefix must be shorter!");
            return false;
        }

        List<String> badWords = configuration.getStringList("Blocked");
        for(String w : badWords){
            if(args[0].contains(w)){
                player.sendMessage(tag + " §cThis prefix is banned, try other!");
                return false;
            }
        }

        sql.setPrefix(player, prefix);
        player.sendMessage(tag + " §6Prefix " + args[0] + " set successfully!");
        manager.loadPrefix(player);
        return false;
    }
    public boolean hasPrefix(Player player){
        return sql.getPrefix(player) != null;
    }
}
