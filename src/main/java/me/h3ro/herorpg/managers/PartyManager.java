package me.h3ro.herorpg.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.OfflinePlayer;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.modules.party.IParty;
import me.h3ro.herorpg.modules.party.Party;

public class PartyManager {
    
    private App plugin;

    private ArrayList<IParty> parties;

    public PartyManager(App plugin) {

        this.plugin = plugin;

        this.parties = new ArrayList<>();

    }

    public void createParty(OfflinePlayer player) {
    
        IParty party = new Party(player);

        if(this.parties.contains(party)) {
            return;
        }

        this.parties.add(party);

    }

    public void removeParty(IParty party) {

        if(!this.parties.contains(party)) {
            return;
        }

        this.parties.remove(party);

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
            output.writeObject(this.parties);
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
        }

        ObjectInputStream input = new ObjectInputStream( new GZIPInputStream( new FileInputStream(file) ) );
        Object readObject = input.readObject();
        input.close();

        if( !(readObject instanceof ArrayList) ){
            throw new IOException("Data is not an Array List");
        }

        this.parties = (ArrayList<IParty>) readObject;

    }

}
