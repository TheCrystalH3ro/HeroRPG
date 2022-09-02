package me.h3ro.herorpg.modules.party;

import java.util.ArrayList;

import me.h3ro.herorpg.core.modules.party.IParty;
import me.h3ro.herorpg.core.modules.player.IPlayer;

public class Party implements IParty {

    private IPlayer owner;
    private ArrayList<IPlayer> members;
    
    public Party(IPlayer owner) {

        this.owner = owner;

        this.members = new ArrayList<>();

        this.addPlayer(owner);

    }

    @Override
    public IPlayer getOwner() {
        return this.owner;
    }

    @Override
    public ArrayList<IPlayer> getPlayers() {
        return this.members;
    }

    @Override
    public void addPlayer(IPlayer player) {

        if(this.members.contains(player)) {
            return;
        }

        this.members.add(player);

        player.setParty(this);

    }

    @Override
    public void removePlayer(IPlayer player) {
        
        if(!this.members.contains(player)) {
            return;
        }

        if(this.isOwner(player)) {
            return;
        }

        this.members.remove(player);
        player.setParty(null);

    }

    @Override
    public boolean isOwner(IPlayer player) {
        return this.owner.equals(player);
    }

    @Override
    public boolean isMember(IPlayer player) {
        return this.members.contains(player);
    }

    @Override
    public boolean equals(IParty party) {
        return this.owner.equals(party.getOwner());
    }

    @Override
    public void disband() {
        
        for(IPlayer player : this.members) {
            player.setParty(null);
        }

    }

}
