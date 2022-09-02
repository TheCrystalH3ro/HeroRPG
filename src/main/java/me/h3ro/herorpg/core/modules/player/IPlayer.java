package me.h3ro.herorpg.core.modules.player;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.h3ro.herorpg.core.modules.party.IParty;

public interface IPlayer {
    
    public String getName();
    public UUID getUuid();

    public boolean equals(IPlayer player);

    public OfflinePlayer getOfflinePlayer();
    public Player getOnlinePlayer();

    public boolean isOnline();
    public boolean hasPlayedBefore();

    public int getExperience();
    public int getLevel();

    public void setExperience(int experience);
    public void setLevel(int level);

    public IParty getParty();

    public void setParty(IParty party);

}
