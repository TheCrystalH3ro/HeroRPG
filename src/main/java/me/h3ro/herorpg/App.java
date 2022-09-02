package me.h3ro.herorpg;

import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import me.h3ro.herorpg.commands.HeroCommands;
import me.h3ro.herorpg.configuration.AppConfig;
import me.h3ro.herorpg.core.managers.ILevelManager;
import me.h3ro.herorpg.core.managers.IPartyManager;
import me.h3ro.herorpg.core.managers.IPlayerManager;
import me.h3ro.herorpg.core.modules.player.IPlayer;
import me.h3ro.herorpg.listeners.ExperienceListener;
import me.h3ro.herorpg.listeners.PlayerJoinListener;
import me.h3ro.herorpg.managers.LevelManager;
import me.h3ro.herorpg.managers.PartyManager;
import me.h3ro.herorpg.managers.PlayerManager;
import me.h3ro.herorpg.modules.player.Player;

public class App extends JavaPlugin {
    
    private FileConfiguration config;
    
    private IPlayerManager playerManager;
    private ILevelManager levelManager;
    private IPartyManager partyManager;

    public void updateConfig() {

        this.reloadConfig();
        this.config = this.getConfig();

        this.updatePlayers();

    }

    @Override
    public void onEnable() {

        this.config = AppConfig.setupConfig(this.getConfig());
        this.saveConfig();
        
        this.registerManagers();
        this.registerCommands();
        this.registerListeners();

        this.updatePlayers();
        
    }

    @Override
    public void onDisable() {

        try{
            this.levelManager.saveExperienceFile();
            this.levelManager.saveLevelFile();
        } catch(IOException e){
            e.printStackTrace();
        }

        try{
            this.partyManager.savePartyFile();
        } catch(IOException e){
            e.printStackTrace();
        }

    }

    private void registerManagers() {

        this.playerManager= new PlayerManager(this);

        this.levelManager = new LevelManager(this);

        try{
            this.levelManager.loadExperienceFile();
            this.levelManager.loadLevelFile();
        } catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }

        this.partyManager = new PartyManager(this);

        try{
            this.partyManager.loadPartyFile();
        } catch(ClassNotFoundException | IOException e){
            e.printStackTrace();
        }

    }

    private void registerCommands() {
        new HeroCommands(this, this.levelManager, this.partyManager);
    }

    private void registerListeners() {
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

    public boolean isXPBarEnabled() {
        return this.config.getBoolean("Levels.xpBar");
    }

    public void initPlayerJoin(OfflinePlayer player) {

        IPlayer myPlayer = new Player(player);

        if(!this.playerManager.hasPlayedBefore(myPlayer)) {
            this.playerManager.addPlayer(myPlayer);
        }

        this.updatePlayer(myPlayer);

    }

    private void updatePlayers() {

        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {

            IPlayer myPlayer = new Player(player);

            if(!this.playerManager.hasPlayedBefore(myPlayer)) {
                this.playerManager.addPlayer(myPlayer);
            }
    
            this.updatePlayer(myPlayer);

        }

    }

    private void updatePlayerUI(IPlayer player) {

        this.levelManager.updateExperienceDisplay(player);
        this.levelManager.updateLevelDisplay(player);
        
    }

    private void updatePlayer(IPlayer player) {

        int playerLvl = this.levelManager.getPlayerLevel(player);
        int playerXP = this.levelManager.getPlayerExperience(player);
        
        player.setLevel(playerLvl);
        player.setExperience(playerXP);

        this.updatePlayerUI(player);

    }

    public ILevelManager getLevelManager() {
        return this.levelManager;
    }

    public IPlayer getPlayer(UUID playerId) {
        return this.playerManager.getPlayer(playerId);
    }

    public IPlayer getPlayer(String playerName) {
        return this.playerManager.getPlayer(playerName);
    }

}
