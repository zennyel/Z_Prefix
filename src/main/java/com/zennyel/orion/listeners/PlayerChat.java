package com.zennyel.orion.listeners;

import com.zennyel.orion.database.SQLite;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChat implements Listener {
    private SQLite sqLite;

    public PlayerChat(SQLite sqLite) {
        this.sqLite = sqLite;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        if(sqLite.getPrefix(player) == null){
            return;
        }
        String prefix = "§8[" + sqLite.getPrefix(player).replace("&", "§") + "§8]§7 ";
        event.setFormat(prefix + event.getFormat());
    }

}
