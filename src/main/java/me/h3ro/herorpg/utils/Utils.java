package me.h3ro.herorpg.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Utils {
    
    public static String chat(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack createItem(String materialString, int amount, int data, int customModelData, String displayName, List<String> description){
        ItemStack item;

        item = new ItemStack( Material.matchMaterial(materialString), amount, (short) data );
        List<String> lore = new ArrayList<>();

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName( Utils.chat(displayName) );

        for(String line : description){
            lore.add( Utils.chat(line) );
        }

        if(customModelData != 0){
            meta.setCustomModelData(customModelData);
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

}
