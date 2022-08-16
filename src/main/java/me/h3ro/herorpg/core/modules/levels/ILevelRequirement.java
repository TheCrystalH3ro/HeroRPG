package me.h3ro.herorpg.core.modules.levels;

public interface ILevelRequirement extends Comparable<ILevelRequirement> {
    
    public int getLevel();
    public int getXP();
    public boolean isValid();

}
