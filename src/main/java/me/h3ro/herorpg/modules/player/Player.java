package me.h3ro.herorpg.modules.player;

import java.util.UUID;

import org.bukkit.OfflinePlayer;

import me.h3ro.herorpg.core.modules.party.IParty;
import me.h3ro.herorpg.core.modules.player.IPlayer;

public class Player implements IPlayer {

    private OfflinePlayer offlinePlayer;

    private int experience;
    private int level;

    private IParty party;

    public Player(OfflinePlayer offlinePlayer) {

        this.offlinePlayer = offlinePlayer;

        this.experience = 0;
        this.level = 1;

        this.party = null;

    }

    @Override
    public String getName() {
        return this.offlinePlayer.getName();
    }

    @Override
    public UUID getUuid() {
        return this.offlinePlayer.getUniqueId();
    }

    @Override
    public boolean equals(IPlayer player) {
        return this.getUuid().equals(player.getUuid());
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return this.offlinePlayer;
    }

    @Override
    public org.bukkit.entity.Player getOnlinePlayer() {
        return this.offlinePlayer.getPlayer();
    }

    @Override
    public boolean isOnline() {
        return this.getOfflinePlayer().isOnline();
    }

    @Override
    public boolean hasPlayedBefore() {
        return this.getOfflinePlayer().hasPlayedBefore();
    }

    @Override
    public int getExperience() {
        return this.experience;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public IParty getParty() {
        return this.party;
    }

    @Override
    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setParty(IParty party) {
        this.party = party;
    }
    
}
