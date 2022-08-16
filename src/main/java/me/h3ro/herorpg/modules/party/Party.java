package me.h3ro.herorpg.modules.party;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;

import me.h3ro.herorpg.core.modules.party.IParty;

public class Party implements IParty {

    private OfflinePlayer owner;
    private ArrayList<OfflinePlayer> members;
    
    public Party(OfflinePlayer owner) {

        this.owner = owner;

        this.members = new ArrayList<>();

        this.members.add(this.owner);

    }

    @Override
    public OfflinePlayer getOwner() {
        return this.owner;
    }

    @Override
    public ArrayList<OfflinePlayer> getPlayers() {
        return this.members;
    }

    @Override
    public void addPlayer(OfflinePlayer player) {

        if(this.members.contains(player)) {
            return;
        }

        this.members.add(player);

    }

    @Override
    public void removePlayer(OfflinePlayer player) {
        
        if(!this.members.contains(player)) {
            return;
        }

        this.members.remove(player);

    }

    @Override
    public boolean isOwner(OfflinePlayer player) {
        return this.owner.equals(player);
    }

    @Override
    public boolean isMember(OfflinePlayer player) {
        return this.members.contains(player);
    }

    @Override
    public boolean equals(IParty party) {
        return this.owner.equals(party.getOwner());
    }

}
