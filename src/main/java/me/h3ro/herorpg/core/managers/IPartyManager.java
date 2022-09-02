package me.h3ro.herorpg.core.managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import me.h3ro.herorpg.core.modules.party.IParty;
import me.h3ro.herorpg.core.modules.player.IPlayer;

public interface IPartyManager {

    public void savePartyFile() throws FileNotFoundException, IOException;
    public void loadPartyFile() throws FileNotFoundException, IOException, ClassNotFoundException;
    
    public IParty createParty(IPlayer player);
    public void removeParty(IParty party);

    public void addToParty(IParty party, IPlayer player);
    public void removeFromParty(IParty party, IPlayer player);

}
