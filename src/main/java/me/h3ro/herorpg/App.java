package me.h3ro.herorpg;

import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.h3ro.herorpg.commands.HeroCommands;
import me.h3ro.herorpg.configuration.AppConfig;
import me.h3ro.herorpg.listeners.ExperienceListener;
import me.h3ro.herorpg.listeners.PlayerJoinListener;
import me.h3ro.herorpg.managers.LevelManager;

public class App extends JavaPlugin {
    
    public FileConfiguration config = getConfig();

    public void updateConfig(){
        this.reloadConfig();
        this.config = this.getConfig();
    }

    @Override
    public void onEnable(){
        config = AppConfig.setupConfig(config);
        saveConfig();

        LevelManager levelManager = new LevelManager(this);
        try{
            levelManager.loadExperienceFile();
            levelManager.loadLevelFile();
        } catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }
        
        registerManagers();
        registerCommands();
        registerListeners();
        
    }

    @Override
    public void onDisable(){

        LevelManager levelManager = new LevelManager(this);
        try{
            levelManager.saveExperienceFile();
            levelManager.saveLevelFile();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    public void registerManagers(){
        LevelManager levelManager = new LevelManager(this);
    }

    public void registerCommands(){
        new HeroCommands(this);
    }

    public void registerListeners(){
        new PlayerJoinListener(this);
        new ExperienceListener(this);
    }

}
