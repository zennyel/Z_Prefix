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

public class Prefix implements CommandExecutor {
    private FileConfiguration configuration;
    private String tag;
    private Z_Prefix instance;
    private SQLite sql;
    private PrefixManager manager;

    public Prefix(Z_Prefix instance) {
        this.instance = instance;
        this.sql = instance.getSql();
        this.manager = instance.getManager();
        this.configuration = instance.getConfig();
        this.tag = configuration.getString("Config.tag").replace("&", "§");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)){
            Bukkit.getConsoleSender().sendMessage("Only can be used by players");
            return false;
        }
        Player player = (Player)sender;

        if(!player.hasPermission("prefix.admin")){
            player.sendMessage(tag + "§cYou don't have permission to use this command!");
            return false;
        }

        if(args.length == 0){
            player.sendMessage(tag + "§cWrong command, available commands:\n" +
                    "§c→ /prefix set <player> <prefix>\n"+
                    "§c→ /prefix reset <player> <prefix>");
            return false;
        }



        switch (args[0]){
            case "set":
                if(args.length < 3){
                    player.sendMessage(tag + " §cIncorrect usage, use /prefix set <player> <prefix>");
                    return false;
                }

                Player target = Bukkit.getPlayer(args[1]);

                if (!target.isOnline()){
                    player.sendMessage(tag + " §cThat player isn't online or doesn't exists!");
                    return  false;
                }

                if(hasPrefix(player)){
                    player.sendMessage(tag + " §cThis player already has a prefix!");
                    return  false;
                }

                int maxLength = configuration.getInt("Config.max_length");
                if(args[2].length() > maxLength){
                    player.sendMessage(tag + " §cMax length exceed, the prefix must be shorter!");
                    return false;
                }

                sql.setPrefix(player, args[2]);
                manager.loadPrefix(player);
                player.sendMessage(tag + " §6Prefix " + sql.getPrefix(player) + " set successfully, to " + target.getName() + " !!!");
                break;

            case "reset":
                if(args.length < 2){
                    player.sendMessage(tag + " §cIncorrect usage, use /prefix reset <player>");
                    return false;
                }

                Player player1 = Bukkit.getPlayer(args[1]);

                if (!player1.isOnline()){
                        player.sendMessage(tag + " §cThat player is not online or doesn't exists!");
                        return  false;
                }
                if(!hasPrefix(player)){
                    player.sendMessage(tag + " §cThis player already don't have a prefix!");
                    return  false;
                }

                player.sendMessage(tag + " §6Prefix from " + player.getName() + " '"+ sql.getPrefix(player) + "' successfully removed!");
                sql.clearPrefix(player);
                manager.loadPrefix(player);

                break;
            default:
                player.sendMessage(tag + " §cThis command doesn't exists!");
        }

        return false;
    }

    public boolean hasPrefix(Player player){
        return sql.getPrefix(player) != null;
    }
}
