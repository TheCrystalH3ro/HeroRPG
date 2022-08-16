package me.h3ro.herorpg.core.managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.OfflinePlayer;

import me.h3ro.herorpg.core.modules.party.IParty;

public interface IPartyManager {

    public void savePartyFile() throws FileNotFoundException, IOException;
    public void loadPartyFile() throws FileNotFoundException, IOException, ClassNotFoundException;
    
    public void createParty(OfflinePlayer player);
    public void removeParty(IParty party);

}
