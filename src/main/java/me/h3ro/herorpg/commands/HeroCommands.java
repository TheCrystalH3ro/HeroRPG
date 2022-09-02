package me.h3ro.herorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.core.managers.ILevelManager;
import me.h3ro.herorpg.core.modules.player.IPlayer;
import me.h3ro.herorpg.utils.Utils;

public class HeroCommands implements CommandExecutor {

    private App plugin;

    private ILevelManager levelManager;
    
    public HeroCommands(App plugin, ILevelManager levelManager){
        this.plugin = plugin;

        this.levelManager = levelManager;

        plugin.getCommand("herorpg").setExecutor(this);
    }

    private void showPlayerExperience(CommandSender sender, IPlayer player) {

        int nextLvlXP = this.levelManager.getLevelRequirement(player);
    
        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &3"+ player.getName() +" &7's experience: &6" + player.getExperience() + "&7/&6" + nextLvlXP ));

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){

        if(sender.hasPermission("herorpg.use")){

            if(args.length == 0) {
                sender.sendMessage( Utils.chat("&8--------------------- &7[&6HeroRPG&7] &8---------------------") );
                sender.sendMessage( Utils.chat("") );
                sender.sendMessage( Utils.chat("&6/herorpg reload &7- reloads the plugin.") );
                sender.sendMessage( Utils.chat("&6/herorpg experience help &7- experience commands.") );
                sender.sendMessage( Utils.chat("&6/herorpg level help &7- level commands.") );
                sender.sendMessage( Utils.chat("") );
                sender.sendMessage( Utils.chat("&8-------------------- &7Made by &aH3ro &8-------------------") );
    
                return true;
            }
            
            //herorpg reload
            if(args[0].equalsIgnoreCase("reload")){
                
                if(sender.hasPermission("herorpg.reload")){
                    plugin.updateConfig();
                    sender.sendMessage( Utils.chat("&8[&6HeroRPG&8] &aReloaded HeroRPG config.") );
                    return true;
                }
                
                sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                return false;
            }

            if(args.length == 1){
                
                //herorpg experience
                if(args[0].equalsIgnoreCase("experience")){

                    if(sender.hasPermission("herorpg.show") || sender.hasPermission("herorpg.view")){

                        if(!(sender instanceof Player)){
                            return false;
                        }

                        IPlayer player = this.plugin.getPlayer(sender.getName());

                        int nextLvlXP = this.levelManager.getLevelRequirement(player);

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Your experience: &6" + player.getExperience() + "&7/&6" + nextLvlXP ));

                        return true;

                    }

                    sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                    return false;

                }

                //herorpg level
                if(args[0].equalsIgnoreCase("level")){

                    if(sender.hasPermission("herorpg.show") || sender.hasPermission("herorpg.view")){

                        if(!(sender instanceof Player)){
                            return false;
                        }

                        IPlayer player = this.plugin.getPlayer(sender.getName());

                        int playerLvl = player.getLevel();

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Your level: &6" + playerLvl));

                        return true;

                    }

                    sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                    return false;

                }

            }

            if(args.length == 2){

                if(args[0].equalsIgnoreCase("experience")){

                    //herorpg experience help
                    if(args[1].equalsIgnoreCase("help")){

                        sender.sendMessage( Utils.chat("&8--------------------- &7[&6HeroRPG&7] &8---------------------") );
                        sender.sendMessage(Utils.chat("&6/herorpg experience <player> &7- view experience of a player."));
                        sender.sendMessage( Utils.chat("&6/herorpg experience <add:remove:set> <player> <amount> &7- modify experience.") );
                        sender.sendMessage( Utils.chat("&8-------------------- &7Made by &aH3ro &8-------------------") );
                        return true;

                    }

                    //herorpg experience <add>
                    if(args[1].equalsIgnoreCase("add")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg experience add <player> <amount>"));
                        return true;

                    }

                    //herorpg experience <remove>
                    if(args[1].equalsIgnoreCase("remove")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg experience remove <player> <amount>"));
                        return true;

                    }

                    //herorpg experience <set>
                    if(args[1].equalsIgnoreCase("set")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg experience set <player> <amount>"));
                        return true;

                    }

                    //herorpg experience <get>
                    if(args[1].equalsIgnoreCase("get")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg experience get <player>"));
                        return true;

                    }
                    
                    //herorpg experience <player>
                    if(sender.hasPermission("herorpg.view")){

                        IPlayer player = this.plugin.getPlayer(args[1]);

                        if(player.hasPlayedBefore()){

                            this.showPlayerExperience(sender, player);

                            return true;

                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[1] + " &7could not be found!"));
                        }

                        return false;

                    }

                }

                if(args[0].equalsIgnoreCase("level")){

                    //herorpg level help
                    if(args[1].equalsIgnoreCase("help")){

                        sender.sendMessage( Utils.chat("&8--------------------- &7[&6HeroRPG&7] &8---------------------") );
                        sender.sendMessage(Utils.chat("&6/herorpg level <player> &7- view level of a player."));
                        sender.sendMessage( Utils.chat("&6/herorpg level <add:remove:set> <player> <amount> &7- modify level.") );
                        sender.sendMessage( Utils.chat("&8-------------------- &7Made by &aH3ro &8-------------------") );
                        return true;

                    }

                    //herorpg level <add>
                    if(args[1].equalsIgnoreCase("add")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg level add <player> <amount>"));
                        return true;

                    }

                    //herorpg level <remove>
                    if(args[1].equalsIgnoreCase("remove")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg level remove <player> <amount>"));
                        return true;

                    }

                    //herorpg level <set>
                    if(args[1].equalsIgnoreCase("set")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg level set <player> <amount>"));
                        return true;

                    }

                    //herorpg level <get>
                    if(args[1].equalsIgnoreCase("get")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg level get <player>"));
                        return true;

                    }
                    
                    //herorpg level <player>
                    if(sender.hasPermission("herorpg.view")){

                        IPlayer player = this.plugin.getPlayer(args[1]);

                        if(player.hasPlayedBefore()){
                            
                            int playerLvl = player.getLevel();

                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &3"+ player.getName() +" &7's level: &6" + playerLvl));

                            return true;

                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[1] + " &7could not be found!"));
                        }

                        return false;

                    }

                }

            }

            if(args.length == 3){

                if(args[0].equalsIgnoreCase("experience")){

                    IPlayer player = this.plugin.getPlayer(args[2]);

                    //herorpg experience get <player>
                    if(args[1].equalsIgnoreCase("get")){

                        if(sender.hasPermission("herorpg.view")){
    
                            if(player.hasPlayedBefore()){

                                this.showPlayerExperience(sender, player);

                                return true;
    
                            } else {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                            }

                            return false;

                        }

                        sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                        return false;

                    }

                    //herorpg experience <add:remove:set> <player>
                    sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg experience <add:remove:set> " + player.getName() + "&7 <amount>"));

                    return false;

                }

                if(args[0].equalsIgnoreCase("level")){

                    IPlayer player = this.plugin.getPlayer(args[2]);

                    //herorpg level get <player>
                    if(args[1].equalsIgnoreCase("get")){

                        if(sender.hasPermission("herorpg.view")){
    
                            if(player.hasPlayedBefore()){
                                
                                int playerLvl = player.getLevel();
    
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &3"+ player.getName() +" &7's level: &6" + playerLvl));

                                return true;
    
                            } else {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                            }

                            return false;

                        }

                        sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                        return false;

                    }

                    //herorpg level <add:remove:set> <player>
                    sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg level <add:remove:set> " + player.getName() + "&7 <amount>"));

                    return false;

                }
                
            }

            if(args.length == 4){

                if(args[0].equalsIgnoreCase("experience")){

                    IPlayer player = this.plugin.getPlayer(args[2]);
                    int amount = Integer.parseInt(args[3]);

                    //herorpg experience add <player> <amount>
                    if(args[1].equalsIgnoreCase("add")){

                        if(!sender.hasPermission("herorpg.experience.add")){
                            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
                            return false;
                        }

                        if(player.hasPlayedBefore()){
                            this.levelManager.addExperienceToPlayer(player, amount);
                            sender.sendMessage( Utils.chat( "&8[&6HeroRPG&8] &7You have successfully added &6" + args[3] + " XP &7to the player &3" + player.getName() ) );

                            return true;
                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                        }

                        return false;

                    }

                    //herorpg experience remove <player> <amount>
                    if(args[1].equalsIgnoreCase("remove")){

                        if(!sender.hasPermission("herorpg.experience.remove")){
                            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
                            return false;
                        }

                        if(player.hasPlayedBefore()){
                            this.levelManager.removeExperienceFromPlayer(player, amount);
                            sender.sendMessage( Utils.chat( "&8[&6HeroRPG&8] &7You have successfully removed &6" + args[3] + " XP &7from the player &3" + player.getName() ) );

                            return true;
                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                        }

                        return false;

                    }

                    //herorpg experience set <player> <amount>
                    if(args[1].equalsIgnoreCase("set")){

                        if(!sender.hasPermission("herorpg.experience.set")){
                            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
                            return false;
                        }

                        if(player.hasPlayedBefore()){
                            this.levelManager.setPlayerExperience(player, amount);
                            sender.sendMessage( Utils.chat( "&8[&6HeroRPG&8] &7You have successfully set the XP of player &3" + player.getName() + " &7to &6" + args[3] + " XP&7." ) );

                            return true;
                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                        }

                        return false;

                    }

                }

                if(args[0].equalsIgnoreCase("level")){

                    IPlayer player = this.plugin.getPlayer(args[2]);
                    int amount = Integer.parseInt(args[3]);

                    //herorpg level add <player> <amount>
                    if(args[1].equalsIgnoreCase("add")){

                        if(!sender.hasPermission("herorpg.level.add")){
                            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
                            return false;
                        }

                        if(player.hasPlayedBefore()){
                            this.levelManager.addLevelToPlayer(player, amount);
                            sender.sendMessage( Utils.chat( "&8[&6HeroRPG&8] &7You have successfully added &6" + args[3] + " LVL &7to the player &3" + player.getName() ) );

                            return true;
                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                        }

                        return false;

                    }

                    //herorpg level remove <player> <amount>
                    if(args[1].equalsIgnoreCase("remove")){

                        if(!sender.hasPermission("herorpg.level.remove")){
                            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
                            return false;
                        }

                        if(player.hasPlayedBefore()){
                            this.levelManager.removeLevelFromPlayer(player, amount);
                            sender.sendMessage( Utils.chat( "&8[&6HeroRPG&8] &7You have successfully removed &6" + args[3] + " LVL &7from the player &3" + player.getName() ) );

                            return true;
                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                        }

                        return false;

                    }

                    //herorpg level set <player> <amount>
                    if(args[1].equalsIgnoreCase("set")){

                        if(!sender.hasPermission("herorpg.level.set")){
                            sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));
                            return false;
                        }

                        if(player.hasPlayedBefore()){
                            this.levelManager.setPlayerLevel(player, amount);
                            sender.sendMessage( Utils.chat( "&8[&6HeroRPG&8] &7You have successfully set the LVL of player &3" + player.getName() + " &7to &6" + args[3] + "&7." ) );

                            return true;
                        } else {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));
                        }

                        return false;

                    }

                }

            }
    
            sender.sendMessage( Utils.chat("&8[&6HeroRPG&8] &cWrong command. Use /herorpg for a command list.") );
            
        }

        sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

        return false;
    }
    
}
