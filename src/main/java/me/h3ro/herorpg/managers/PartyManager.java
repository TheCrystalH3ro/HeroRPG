package me.h3ro.herorpg.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.core.managers.IPartyManager;
import me.h3ro.herorpg.core.modules.party.IParty;
import me.h3ro.herorpg.core.modules.player.IPlayer;
import me.h3ro.herorpg.modules.party.Party;
import me.h3ro.herorpg.utils.Utils;

public class PartyManager implements IPartyManager {
    
    private App plugin;

    private ArrayList<IParty> parties;
    private HashMap<UUID, ArrayList<UUID>> partyData;

    public PartyManager(App plugin) {

        this.plugin = plugin;

        this.parties = new ArrayList<>();
        this.partyData = new HashMap<>();

    }

    public IParty createParty(IPlayer player) {
    
        IParty party = new Party(player);

        if(this.parties.contains(party)) {
            return player.getParty();
        }

        this.parties.add(party);

        this.partyData.put(party.getOwner().getUuid(), new ArrayList<UUID>());

        return party;

    }

    public void addToParty(IParty party, IPlayer player) {

        if(party.isMember(player)) {
            return;
        }

        party.addPlayer(player);
        
        ArrayList<UUID> playerList = this.partyData.get(party.getOwner().getUuid());

        playerList.add(player.getUuid());

        String message = Utils.chat("&8[&6HeroRPG&8] &7Player &3" + player.getName() + "&7 has joined your party!");

        party.notify(message);

    }

    public void removeFromParty(IParty party, IPlayer player) {

        if(!party.isMember(player)) {
            return;
        }

        if(party.isOwner(player)) {
            return;
        }

        party.removePlayer(player);

        ArrayList<UUID> playerList = this.partyData.get(party.getOwner().getUuid());

        playerList.remove(player.getUuid());

    }

    public void removeParty(IParty party) {

        if(!this.parties.contains(party)) {
            return;
        }

        this.parties.remove(party);

        party.disband();

        this.partyData.remove(party.getOwner().getUuid());

        String message = Utils.chat("&8[&6HeroRPG&8] &7Your party has been disbanded!");

        party.notify(message, false);

    }

    public void savePartyFile() throws FileNotFoundException, IOException {

        String fileLocation = String.format("%s\\Data\\party.dat", plugin.getDataFolder().getAbsolutePath());

        File file = new File(fileLocation);

        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        ObjectOutputStream output = new ObjectOutputStream( new GZIPOutputStream( new FileOutputStream(file) ) );

        try {
            output.writeObject(this.partyData);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    public void loadPartyFile() throws FileNotFoundException, IOException, ClassNotFoundException {

        String fileLocation = String.format("%s\\Data\\party.dat", plugin.getDataFolder().getAbsolutePath());

        File file = new File(fileLocation);

        if(!file.exists() || file == null){
            file.getParentFile().mkdirs();
            file.createNewFile();
            return;
        }

        ObjectInputStream input = new ObjectInputStream( new GZIPInputStream( new FileInputStream(file) ) );

        Object readObject = input.readObject();
        input.close();

        if( !(readObject instanceof HashMap) ){
            throw new IOException("Data is not a hashmap");
        }

        this.partyData = (HashMap<UUID, ArrayList<UUID>>) readObject;

        for (UUID ownerId : this.partyData.keySet()) {

            if(!this.plugin.isPlayerLoaded(ownerId)) {
                this.plugin.initPlayer(Bukkit.getOfflinePlayer(ownerId));
            }
            
            IPlayer owner = this.plugin.getPlayer(ownerId);

            IParty party = new Party(owner);

            this.parties.add(party);

            for (UUID playerId : this.partyData.get(ownerId)) {

                if(!this.plugin.isPlayerLoaded(playerId)) {
                    this.plugin.initPlayer(Bukkit.getOfflinePlayer(playerId));
                }
                
                IPlayer player = this.plugin.getPlayer(playerId);

                party.addPlayer(player);

            }

        }

    }

}
