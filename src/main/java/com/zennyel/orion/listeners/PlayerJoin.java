package com.zennyel.orion.listeners;

import com.zennyel.orion.PrefixManager;
import com.zennyel.orion.database.SQLite;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private SQLite sqLite;
    private PrefixManager manager;

    public PlayerJoin(SQLite sqLite, PrefixManager manager) {
        this.sqLite = sqLite;
        this.manager = manager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(sqLite.getPrefix(p) != null){
            manager.loadPrefix(p);
        }
    }
}
