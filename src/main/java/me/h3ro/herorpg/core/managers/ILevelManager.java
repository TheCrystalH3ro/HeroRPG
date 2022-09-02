package me.h3ro.herorpg.core.managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.entity.EntityType;

import me.h3ro.herorpg.core.modules.player.IPlayer;

public interface ILevelManager {

    public void saveExperienceFile() throws FileNotFoundException, IOException;
    public void saveLevelFile() throws FileNotFoundException, IOException;
    public void loadExperienceFile() throws FileNotFoundException, IOException, ClassNotFoundException;
    public void loadLevelFile() throws FileNotFoundException, IOException, ClassNotFoundException;

    public int getPlayerExperience(IPlayer player);
    public void setPlayerExperience(IPlayer player, int amount);
    public void addExperienceToPlayer(IPlayer player, int amount);
    public void removeExperienceFromPlayer(IPlayer player, int amount);

    public int getPlayerLevel(IPlayer player);
    public void setPlayerLevel(IPlayer player, int amount);
    public void addLevelToPlayer(IPlayer player, int amount);
    public void removeLevelFromPlayer(IPlayer player, int amount);

    public void updateExperienceDisplay(IPlayer player);
    public void updateLevelDisplay(IPlayer player);

    public int getLevelRequirement(IPlayer player);
    public int getMobDropXP(EntityType type);
    
}
