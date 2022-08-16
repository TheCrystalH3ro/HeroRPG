package me.h3ro.herorpg.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.core.managers.ILevelManager;

public class ExperienceListener implements Listener {
    
    private App plugin;

    public ExperienceListener(App plugin){
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onExperienceReceived(PlayerExpChangeEvent event){

        if(!this.plugin.isXPBarEnabled()) {
            event.setAmount(0);
        }

    }

    @EventHandler
    public void onEntityKilled(EntityDeathEvent event){

        LivingEntity mob = event.getEntity();

        Player player = mob.getKiller();

        if(player == null) {
            return;
        }

        UUID p_uuid = player.getUniqueId();

        ILevelManager manager = this.plugin.getLevelManager();

        int dropXP = manager.getMobDropXP(mob.getType());

        manager.addExperienceToPlayer(Bukkit.getOfflinePlayer(p_uuid), dropXP);

    }
    
}
