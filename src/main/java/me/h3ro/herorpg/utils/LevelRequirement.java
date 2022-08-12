package me.h3ro.herorpg.utils;

import java.util.List;

public class LevelRequirement  implements Comparable<LevelRequirement> {
    
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
    public int compareTo(LevelRequirement requirement) {
        
        return this.level - requirement.level;

    }

}
