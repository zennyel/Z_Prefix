package com.zennyel.orion;

import com.zennyel.orion.commands.Prefix;
import com.zennyel.orion.commands.SetPrefix;
import com.zennyel.orion.database.SQLite;
import com.zennyel.orion.listeners.PlayerChat;
import com.zennyel.orion.listeners.PlayerJoin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Z_Prefix extends JavaPlugin {
    private SQLite sql;
    private PrefixManager manager;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        setupSql();
        registerCommands();
        registerListeners();
        loadPrefix();
    }

    @Override
    public void onDisable() {
        sql.disconnect();
    }

    public void loadPrefix(){
        for(Player p: Bukkit.getOnlinePlayers()){
            if(sql.getPrefix(p) != null){
                manager.loadPrefix(p);
            }
        }
    }
    public void setupSql(){
        sql = new SQLite(this);
        sql.createTable();
        this.manager = new PrefixManager(sql, this.getConfig());
    }
    public void registerListeners(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoin(sql,manager), this);
        pm.registerEvents(new PlayerChat(sql), this);
    }

    public void registerCommands(){
        getCommand("prefix").setExecutor(new Prefix(this));
        getCommand("setprefix").setExecutor(new SetPrefix(this));
    }

    public PrefixManager getManager() {
        return manager;
    }

    public SQLite getSql() {
        return sql;
    }
}
