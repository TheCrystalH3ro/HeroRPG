package me.h3ro.herorpg.core.managers;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;

public interface ILevelManager {

    public void saveExperienceFile() throws FileNotFoundException, IOException;
    public void saveLevelFile() throws FileNotFoundException, IOException;
    public void loadExperienceFile() throws FileNotFoundException, IOException, ClassNotFoundException;
    public void loadLevelFile() throws FileNotFoundException, IOException, ClassNotFoundException;

    public int getPlayerExperience(OfflinePlayer player);
    public void setPlayerExperience(OfflinePlayer player, int amount);
    public void addExperienceToPlayer(OfflinePlayer player, int amount);
    public void removeExperienceFromPlayer(OfflinePlayer player, int amount);

    public int getPlayerLevel(OfflinePlayer player);
    public void setPlayerLevel(OfflinePlayer player, int amount);
    public void addLevelToPlayer(OfflinePlayer player, int amount);
    public void removeLevelFromPlayer(OfflinePlayer player, int amount);

    public void updateExperienceDisplay(OfflinePlayer player);
    public void updateLevelDisplay(OfflinePlayer player);

    public int getLevelRequirement(OfflinePlayer player);
    public int getMobDropXP(EntityType type);
    
}
