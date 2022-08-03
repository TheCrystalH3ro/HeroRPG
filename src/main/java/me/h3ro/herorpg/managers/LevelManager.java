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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.utils.Utils;

public class LevelManager {

    private App plugin;
    
    public static HashMap<UUID, Integer> experience = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Integer> level = new HashMap<UUID, Integer>();

    public LevelManager(App plugin){
        this.plugin = plugin;
        setupLevelExperience();
    }

    public int maxLvl;

    public List<List<Integer>> lvlUpReq = new ArrayList<List<Integer>>();

    public int nextLvl;
    public int nextLvlXP;

    public int prevLvl;
    public int prevLvlXP;

    public int[] toLvlUp;

    public void saveExperienceFile() throws FileNotFoundException, IOException {

        String fileLocation = String.format("%s\\Data\\experience.dat", plugin.getDataFolder().getAbsolutePath());

        File file = new File(fileLocation);

        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        ObjectOutputStream output = new ObjectOutputStream( new GZIPOutputStream( new FileOutputStream(file) ) );

        try {
            output.writeObject(experience);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public void saveLevelFile() throws FileNotFoundException, IOException {

        String fileLocation = String.format("%s\\Data\\level.dat", plugin.getDataFolder().getAbsolutePath());

        File file = new File(fileLocation);

        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        ObjectOutputStream output = new ObjectOutputStream( new GZIPOutputStream( new FileOutputStream(file) ) );

        try {
            output.writeObject(level);
            output.flush();
            output.close();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public void loadExperienceFile() throws FileNotFoundException, IOException, ClassNotFoundException {

        String fileLocation = String.format("%s\\Data\\experience.dat", plugin.getDataFolder().getAbsolutePath());

        File file = new File(fileLocation);

        if(!file.exists() || file == null){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        ObjectInputStream input = new ObjectInputStream( new GZIPInputStream( new FileInputStream(file) ) );
        Object readObject = input.readObject();
        input.close();

        if( !(readObject instanceof HashMap) ){
            throw new IOException("Data is not a hashmap");
        }

        experience = (HashMap<UUID, Integer>) readObject;

        for (UUID key : experience.keySet()){
            experience.put(key, experience.get(key));
        }

    }

    public void loadLevelFile() throws FileNotFoundException, IOException, ClassNotFoundException {

        String fileLocation = String.format("%s\\Data\\level.dat", plugin.getDataFolder().getAbsolutePath());

        File file = new File(fileLocation);

        if(!file.exists() || file == null){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        ObjectInputStream input = new ObjectInputStream( new GZIPInputStream( new FileInputStream(file) ) );
        Object readObject = input.readObject();
        input.close();

        if( !(readObject instanceof HashMap) ){
            throw new IOException("Data is not a hashmap");
        }

        level = (HashMap<UUID, Integer>) readObject;

        for (UUID key : level.keySet()){
            level.put(key, level.get(key));
        }

    }

    public void setupLevelExperience(){
        int lvlXP;
        int lvlRange;
        int index = 0;
        
        this.maxLvl = plugin.config.getInt("Levels.maxLevel");

        this.toLvlUp = new int[maxLvl+1];

        ConfigurationSection lvlKeys = plugin.config.getConfigurationSection("Levels.levelUpXp");

        for(String lvlKey : lvlKeys.getKeys(true)){
            if(lvlKey.equals("level")) {
                continue;
            }
            lvlUpReq.add( lvlKeys.getIntegerList(lvlKey) ); 
        }

        for(int i=0; i <= maxLvl; i++){
            
            if(i == 0 || i == 1){
                toLvlUp[i] = 0;
                continue;
            }

            //Default values
            if(lvlUpReq.size() == 0 || lvlUpReq.get(0).size() == 0){

                prevLvl = 2;
                nextLvl = maxLvl;
    
                prevLvlXP = 100;
                nextLvlXP = 100;

            } else if( lvlUpReq.size() == 1 ){

                prevLvl = lvlUpReq.get(0).get(0);
                nextLvl = maxLvl;

                prevLvlXP = lvlUpReq.get(0).get(1);
                nextLvlXP = lvlUpReq.get(0).get(1);


            } else {

                if(i < lvlUpReq.get(index).get(0)){
                    prevLvl = 2;
                    nextLvl = maxLvl;

                    prevLvlXP = lvlUpReq.get(index).get(1);
                    nextLvlXP = lvlUpReq.get(index).get(1);
                }

                if(i > lvlUpReq.get(index+1).get(0)){

                    if(lvlUpReq.size() > index+2){

                        index++;

                    } else {

                        prevLvl = lvlUpReq.get(index+1).get(0);
                        nextLvl = maxLvl;

                        prevLvlXP = lvlUpReq.get(index+1).get(1);
                        nextLvlXP = lvlUpReq.get(index+1).get(1);

                    }

                }

                if(i >= lvlUpReq.get(index).get(0) && i <= lvlUpReq.get(index+1).get(0)){
                    prevLvl = lvlUpReq.get(index).get(0);
                    nextLvl = lvlUpReq.get(index+1).get(0);
                        
                    prevLvlXP = lvlUpReq.get(index).get(1);
                    nextLvlXP = lvlUpReq.get(index+1).get(1);
                }

            }

            lvlRange = (nextLvl == prevLvl) ? 1 : nextLvl - prevLvl;

            //Previous XP to Level UP + (current level - prevLevel) * (NextLVL - PrevLVL) / number of levels in between
            lvlXP = prevLvlXP + ((nextLvlXP - prevLvlXP) * (i-prevLvl) / lvlRange);

            toLvlUp[i] = lvlXP;

        }
    }

    public void addExperienceToPlayer(OfflinePlayer player, int amount){

        UUID p_uuid = player.getUniqueId();

        if( experience.get(p_uuid) != null){

            experience.put(p_uuid, experience.get(p_uuid) + amount );

        } else {

            experience.put(p_uuid, amount);

        }

        experienceReceived(player);

    }

    public void removeExperienceFromPlayer(OfflinePlayer player, int amount){
        UUID p_uuid = player.getUniqueId();
                
        if(experience.get(p_uuid) != null){
            
            int expToSet = experience.get(p_uuid) - amount;
    
            if(expToSet < 0){
                expToSet = 0;
            }
            
            experience.put(p_uuid, expToSet);

        }
    }

    public void setPlayerExperience(OfflinePlayer player, int amount) {
        UUID p_uuid = player.getUniqueId();

        if(amount < 0){
            amount = 0;
        }
        
        experience.put(p_uuid, amount);

        experienceReceived(player);
    }

    public int getPlayerExperience(OfflinePlayer player){
        UUID p_uuid = player.getUniqueId();
        
        if(experience.get(p_uuid) != null){
            return experience.get(p_uuid);
        } else {
            return 0;
        }
    }

    public void addLevelToPlayer(OfflinePlayer player, int amount){

        UUID p_uuid = player.getUniqueId();

        int lvlToAdd;

        setPlayerExperience(player, 0);

        if(amount > maxLvl){
            amount = maxLvl;
        }

        if( level.get(p_uuid) != null){

            lvlToAdd = level.get(p_uuid) + amount;

            if(lvlToAdd > maxLvl){
                lvlToAdd = maxLvl;
            }

            level.put(p_uuid, lvlToAdd );

        } else {

            lvlToAdd = amount;

            if(lvlToAdd > maxLvl){
                lvlToAdd = maxLvl;
            }

            level.put(p_uuid, amount);

        }

    }

    public void levelUp(OfflinePlayer player){

        UUID p_uuid = player.getUniqueId();

        int lvlToAdd;

        if( level.get(p_uuid) != null){

            lvlToAdd = level.get(p_uuid) + 1;

            if(lvlToAdd > maxLvl){
                lvlToAdd = maxLvl;
            }

            level.put(p_uuid, lvlToAdd );

        } else {

            lvlToAdd = 2;

            if(lvlToAdd > maxLvl){
                lvlToAdd = maxLvl;
            }

            level.put(p_uuid, lvlToAdd);

        }

    }

    public void removeLevelFromPlayer(OfflinePlayer player, int amount){
        UUID p_uuid = player.getUniqueId();

        setPlayerExperience(player, 0);
                
        if(level.get(p_uuid) != null){
            
            int lvlToSet = level.get(p_uuid) - amount;
    
            if(lvlToSet <= 0){
                lvlToSet = 1;
            }
            
            level.put(p_uuid, lvlToSet);

        }
    }

    public void setPlayerLevel(OfflinePlayer player, int amount) {
        UUID p_uuid = player.getUniqueId();

        experience.put(p_uuid, 0);

        if(amount <= 0){
            amount = 1;
        }

        if(amount > maxLvl){
            amount = maxLvl;
        }
        
        level.put(p_uuid, amount);

    }

    public int getPlayerLevel(OfflinePlayer player){
        UUID p_uuid = player.getUniqueId();
        
        if(level.get(p_uuid) != null){
            return level.get(p_uuid);
        } else {
            return 0;
        }
    }

    public void experienceReceived(OfflinePlayer player){

        UUID p_uuid = player.getUniqueId();

        int playerXP = experience.get(p_uuid);

        int playerLvl = getPlayerLevel(player);

        if(playerLvl >= maxLvl){
            return;
        }

        while(playerXP >= toLvlUp[ playerLvl + 1 ]){

            playerXP -= toLvlUp[ playerLvl + 1 ];

            playerLvl++;

            onLevelUp(player, playerLvl);

            if(playerLvl >= maxLvl){
                break;
            }

        }

        if(playerLvl >= maxLvl){
            setPlayerLevel(player, maxLvl);
            return;
        }

        experience.put(p_uuid, playerXP);
        level.put(p_uuid, playerLvl);

    }

    public void onLevelUp(OfflinePlayer p, int level){

        if(p.isOnline()){
            Player player = (Player) p;
            player.sendMessage(Utils.chat("&8[&6HeroRPG&8] &aYou have leveled up to level &6" + level + "&7!"));
        }


    }

}
