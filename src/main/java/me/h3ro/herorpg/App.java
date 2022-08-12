package me.h3ro.herorpg;

import java.io.IOException;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import me.h3ro.herorpg.commands.HeroCommands;
import me.h3ro.herorpg.configuration.AppConfig;
import me.h3ro.herorpg.listeners.ExperienceListener;
import me.h3ro.herorpg.listeners.PlayerJoinListener;
import me.h3ro.herorpg.managers.LevelManager;

public class App extends JavaPlugin {
    
    private FileConfiguration config;
    
    private LevelManager levelManager;

    public void updateConfig(){
        this.reloadConfig();
        this.config = this.getConfig();
    }

    @Override
    public void onEnable(){

        this.config = AppConfig.setupConfig(this.getConfig());
        this.saveConfig();
        
        this.registerManagers();
        this.registerCommands();
        this.registerListeners();
        
    }

    @Override
    public void onDisable(){

        try{
            this.levelManager.saveExperienceFile();
            this.levelManager.saveLevelFile();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    private void registerManagers(){

        this.levelManager = new LevelManager(this);

        try{
            this.levelManager.loadExperienceFile();
            this.levelManager.loadLevelFile();
        } catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }

    }

    private void registerCommands(){
        new HeroCommands(this, this.levelManager);
    }

    private void registerListeners(){
        new PlayerJoinListener(this);
        new ExperienceListener(this);
    }

    public int getMaxLvl() {
        return this.config.getInt("Levels.maxLevel");
    }

    public ConfigurationSection getLvlKeys() {
        return this.config.getConfigurationSection("Levels.levelUpXp");
    }

    public int getBaseXP() {
        return this.config.getInt("Levels.dropRate.base");
    }

    public Integer getMobDropXP(EntityType type) {

        String path = "Levels.dropRate.mobTypes." + type.toString();

        if(!this.config.isInt(path)) {
            return null;
        }

        return this.config.getInt(path);
    }

    public void initPlayerJoin(OfflinePlayer player) {

        int playerLvl = this.levelManager.getPlayerLevel(player);

        if(playerLvl <= 0){
            this.levelManager.setPlayerLevel(player, 1);
        }

    }

    public LevelManager getLevelManager() {
        return this.levelManager;
    }

}
