package me.h3ro.herorpg.core.modules.party;

import java.util.ArrayList;

import me.h3ro.herorpg.core.modules.player.IPlayer;

public interface IParty {
    
    public IPlayer getOwner();

    public ArrayList<IPlayer> getPlayers();

    public void addPlayer(IPlayer player);
    public void removePlayer(IPlayer player);

    public boolean isOwner(IPlayer player);
    public boolean isMember(IPlayer player);

    public void disband();

    public void notify(String message);
    public void notify(String message, boolean includeOwner);

    public boolean equals(IParty party);

}
