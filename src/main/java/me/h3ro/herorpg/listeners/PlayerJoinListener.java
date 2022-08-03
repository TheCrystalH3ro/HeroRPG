package me.h3ro.herorpg.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.managers.LevelManager;

public class PlayerJoinListener implements Listener {
    
    private App plugin;

    public PlayerJoinListener(App plugin){
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = (Player) event.getPlayer();
        UUID p_uuid = player.getUniqueId();

        LevelManager manager = new LevelManager(plugin);

        int playerLvl = manager.getPlayerLevel(player);

        if(playerLvl <= 0){
            manager.setPlayerLevel(player, 1);
        }

    }

}
