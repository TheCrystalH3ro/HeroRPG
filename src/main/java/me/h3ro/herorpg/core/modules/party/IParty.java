package me.h3ro.herorpg.core.modules.party;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;

public interface IParty {
    
    public OfflinePlayer getOwner();

    public ArrayList<OfflinePlayer> getPlayers();

    public void addPlayer(OfflinePlayer player);
    public void removePlayer(OfflinePlayer player);

    public boolean isOwner(OfflinePlayer player);
    public boolean isMember(OfflinePlayer player);

    public boolean equals(IParty party);

}
