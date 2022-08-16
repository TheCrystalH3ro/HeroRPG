package me.h3ro.herorpg.modules.levels;

import java.util.List;

import me.h3ro.herorpg.core.modules.levels.ILevelRequirement;

public class LevelRequirement implements ILevelRequirement {
    
    private int level;
    private int xp;
    private boolean valid;

    public LevelRequirement(List<Integer> list) {

        if(list.size() < 2) {
            this.valid = false;
            return;
        }
        
        this.level = list.get(0);
        this.xp = list.get(1);

        this.valid = true;

    }

    public int getLevel() {
        return this.level;
    }

    public int getXP() {
        return this.xp;
    }

    public boolean isValid() {
        return this.valid;
    }

    @Override
    public int compareTo(ILevelRequirement requirement) {
        
        return this.level - requirement.getLevel();

    }

}
