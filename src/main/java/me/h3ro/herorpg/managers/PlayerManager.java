package me.h3ro.herorpg.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.core.managers.IPlayerManager;
import me.h3ro.herorpg.core.modules.player.IPlayer;
import me.h3ro.herorpg.modules.player.Player;

public class PlayerManager implements IPlayerManager {

    private App plugin;

    private HashMap<UUID, IPlayer> players;

    public PlayerManager(App plugin) {

        this.plugin = plugin;
        
        this.players = new HashMap<>();

    }

    @Override
    public HashMap<UUID, IPlayer> getPlayers() {
        return this.players;
    }

    @Override
    public boolean hasPlayedBefore(IPlayer player) {
        return this.players.containsKey(player.getUuid());
    }

    @Override
    public void addPlayer(IPlayer player) {
        this.players.put(player.getUuid(), player);
    }

    @Override
    public ArrayList<IPlayer> getOnlinePlayers() {
        
        ArrayList<IPlayer> onlinePlayers = new ArrayList<>();

        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {

            OfflinePlayer p = Bukkit.getOfflinePlayer(player.getUniqueId());

            onlinePlayers.add(new Player(p));

        }

        return onlinePlayers;

    }

    @Override
    public IPlayer getPlayer(UUID playerId) {
        return this.players.get(playerId);
    }

    @Override
    public IPlayer getPlayer(String playerName) {
        
        OfflinePlayer player = Bukkit.getPlayerExact(playerName);

        UUID playerId = player.getUniqueId();

        return this.getPlayer(playerId);

    }
    
}