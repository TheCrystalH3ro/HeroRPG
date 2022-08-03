package me.h3ro.herorpg.configuration;

import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;

public class AppConfig {
    
    public static FileConfiguration setupConfig(FileConfiguration config){

        //Levels
        config.addDefault("Levels.xpBar", false);
        config.addDefault("Levels.maxLevel", 5);
        if(config.getConfigurationSection("Levels.levelUpXp") == null) {
            config.addDefault("Levels.levelUpXp.level2", new ArrayList<>());
        }
        config.addDefault("Levels.dropRate.base", 0);
        config.addDefault("Levels.dropRate.mobTypes", new ArrayList<>());

        config.options().copyDefaults(true);

        return config;
        
    }

}
