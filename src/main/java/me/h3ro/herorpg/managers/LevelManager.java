package me.h3ro.herorpg.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.utils.LevelRequirement;
import me.h3ro.herorpg.utils.Utils;

public class LevelManager {

    private App plugin;
    
    public static HashMap<UUID, Integer> experience = new HashMap<UUID, Integer>();
    public static HashMap<UUID, Integer> level = new HashMap<UUID, Integer>();

    private int maxLvl;

    private PriorityQueue<LevelRequirement> lvlUpReq;

    private ArrayList<Integer> toLvlUp;

    public LevelManager(App plugin){
        this.plugin = plugin;

        this.lvlUpReq = new PriorityQueue<>();

        this.setupLevelExperience();
    }

    
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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

    private void setupLevelExperience(){
        
        this.maxLvl = plugin.getMaxLvl();

        this.toLvlUp = new ArrayList<>();

        ConfigurationSection lvlKeys = plugin.getLvlKeys();

        for(String lvlKey : lvlKeys.getKeys(true)){
            if(lvlKey.equals("level")) {
                continue;
            }
            lvlUpReq.add( new LevelRequirement(lvlKeys.getIntegerList(lvlKey)) ); 
        }

        boolean levelsDefined = lvlUpReq.size() != 0;
        boolean levelsValid = lvlUpReq.peek().isValid();

        if(!levelsDefined || !levelsValid) {
            this.toLvlUp =  new ArrayList<Integer>(Collections.nCopies(this.maxLvl, 100));
            this.toLvlUp.add(0, 0);
            this.toLvlUp.set(1, 0);
            return;
        }

        LevelRequirement previous = null;
        LevelRequirement levelMark = lvlUpReq.poll();

        if(lvlUpReq.peek() == null) {
            this.toLvlUp =  new ArrayList<Integer>(Collections.nCopies(this.maxLvl, levelMark.getXP()));
            this.toLvlUp.add(0, 0);
            this.toLvlUp.set(1, 0);
            return;
        } 

        int xpStep = 0;
        int lvlRange = 0;

        int lvlXP;
        int prevLvlXP = 0;

        for(int i=0; i <= maxLvl; i++) {
            
            //Skip level 0 and 1
            if(i == 0 || i == 1) {
                this.toLvlUp.add(i, 0);
                continue;
            }

            if(previous == null) {

                lvlXP = levelMark.getXP();
                xpStep = lvlXP;
                prevLvlXP = lvlXP;

                for(; i < levelMark.getLevel(); i++) {
                    toLvlUp.add(lvlXP);
                }

            }

            //Next level requirement is not defined
            if(levelMark == null) {

                for(; i <= maxLvl; i++) {
                    lvlXP = prevLvlXP + xpStep;
                    toLvlUp.add(lvlXP);
                    prevLvlXP = lvlXP;
                }

                continue;

            }

            if(levelMark.getLevel() == i) {

                lvlXP = levelMark.getXP();
                prevLvlXP = lvlXP;

                toLvlUp.add(i, lvlXP);

                previous = levelMark;
                levelMark = lvlUpReq.poll();

                if(levelMark != null) {

                    lvlRange = levelMark.getLevel() - previous.getLevel();
                    xpStep = (levelMark.getXP() - previous.getXP()) / lvlRange;

                }

                continue;

            }

            lvlXP = prevLvlXP + xpStep;
            prevLvlXP = lvlXP;

            toLvlUp.add(lvlXP);

        }
    }

    public void addExperienceToPlayer(OfflinePlayer player, int amount){

        UUID p_uuid = player.getUniqueId();

        if( experience.get(p_uuid) != null){

            experience.put(p_uuid, experience.get(p_uuid) + amount );

        } else {

            experience.put(p_uuid, amount);

        }

        this.experienceReceived(player);

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

        this.experienceReceived(player);
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

        while(playerXP >= toLvlUp.get(playerLvl + 1)){

            playerXP -= toLvlUp.get(playerLvl + 1);

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

    public int getLevelRequirement(OfflinePlayer player) {

        int level = this.getPlayerLevel(player) + 1;

        if(this.toLvlUp.size() <= level) {
            return this.getPlayerExperience(player);
        }

        return this.toLvlUp.get(level);

    }

}
