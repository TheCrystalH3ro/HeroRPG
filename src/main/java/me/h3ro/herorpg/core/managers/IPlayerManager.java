package me.h3ro.herorpg.core.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.h3ro.herorpg.core.modules.player.IPlayer;

public interface IPlayerManager {

    public HashMap<UUID, IPlayer> getPlayers();

    public boolean hasPlayedBefore(IPlayer player);

    public void addPlayer(IPlayer player);

    public ArrayList<IPlayer> getOnlinePlayers();

    public IPlayer getPlayer(UUID playerId);
    public IPlayer getPlayer(String playerName);

}
