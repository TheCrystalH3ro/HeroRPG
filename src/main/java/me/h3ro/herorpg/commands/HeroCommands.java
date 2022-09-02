package me.h3ro.herorpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.h3ro.herorpg.App;
import me.h3ro.herorpg.core.managers.ILevelManager;
import me.h3ro.herorpg.core.managers.IPartyManager;
import me.h3ro.herorpg.core.modules.party.IParty;
import me.h3ro.herorpg.core.modules.player.IPlayer;
import me.h3ro.herorpg.utils.Utils;

public class HeroCommands implements CommandExecutor {

    private App plugin;

    private ILevelManager levelManager;
    private IPartyManager partyManager;
    
    public HeroCommands(App plugin, ILevelManager levelManager, IPartyManager partyManager){
        this.plugin = plugin;

        this.levelManager = levelManager;
        this.partyManager = partyManager;

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
                sender.sendMessage( Utils.chat("&6/herorpg party help &7- party commands.") );
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

                //herorpg party
                if(args[0].equalsIgnoreCase("party")){

                    if(sender.hasPermission("herorpg.show") || sender.hasPermission("herorpg.view")){

                        if(!(sender instanceof Player)){
                            return false;
                        }

                        IPlayer player = this.plugin.getPlayer(sender.getName());

                        IParty party = player.getParty();

                        if(party == null) {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7You are not a member of any party."));
                            return true;

                        }

                        sender.sendMessage( Utils.chat("&8--------------------- &7[&6HeroRPG&7] &8---------------------") );

                        if(party.isOwner(player)) {
                            sender.sendMessage(Utils.chat("&7You are the owner of a party"));
                        } else {
                            sender.sendMessage(Utils.chat("&7You are a member of a party of the player &6" + party.getOwner().getName()));
                        }

                        sender.sendMessage("");
                        
                        sender.sendMessage(Utils.chat("&7Members:"));

                        for(IPlayer member : party.getPlayers()) {

                            if(party.isOwner(member)) {
                                continue;
                            }

                            sender.sendMessage("&7- &3" + member.getName());

                        }
                        
                        sender.sendMessage("");

                        sender.sendMessage( Utils.chat("&8---------------------------------------------------") );

                        return true;

                    }

                    sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                    return false;

                }

            }

            if(args.length == 2){

                if(args[0].equalsIgnoreCase("experience")) {

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

                if(args[0].equalsIgnoreCase("party")){

                    //herorpg party help
                    if(args[1].equalsIgnoreCase("help")) {

                        sender.sendMessage( Utils.chat("&8--------------------- &7[&6HeroRPG&7] &8---------------------") );
                        sender.sendMessage(Utils.chat("&6/herorpg party &7- view your party"));
                        sender.sendMessage(Utils.chat("&6/herorpg party <player> &7- view party of a player."));
                        sender.sendMessage(Utils.chat("&6/herorpg party create &7- create a party."));
                        sender.sendMessage(Utils.chat("&6/herorpg party leave &7- leave a party."));
                        sender.sendMessage( Utils.chat("&6/herorpg party <add:remove> <player> &7- add/remove a player to/from the party.") );
                        sender.sendMessage( Utils.chat("&8-------------------- &7Made by &aH3ro &8-------------------") );
                        return true;

                    }

                    //herorpg party create
                    if(args[1].equalsIgnoreCase("create")) {

                        if(!(sender instanceof Player)){
                            return false;
                        }

                        if(sender.hasPermission("herorpg.party.create")) {
    
                            IPlayer player = this.plugin.getPlayer(sender.getName());

                            if(player.getParty() != null) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &cYou are already in a party. You need to leave your current party before creating a new one."));
                                return false;
                            }

                            this.partyManager.createParty(player);

                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7You have successfully created a party!"));
                            return true;

                        }

                    }

                    //herorpg party leave
                    if(args[1].equalsIgnoreCase("leave")) {

                        if(!(sender instanceof Player)){
                            return false;
                        }

                        IPlayer player = this.plugin.getPlayer(sender.getName());

                        if(player.getParty() == null) {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &cYou are not a member of any party."));
                            return false;
                        }

                        IParty party = player.getParty();

                        if(party.isOwner(player)) {
                            this.partyManager.removeParty(party);
                        } else {

                            this.partyManager.removeFromParty(party, player);

                            String message = Utils.chat("&8[&6HeroRPG&8] &7Player &3" + player.getName() + "&7 has left your party!");

                            party.notify(message);

                        }

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7You have successfully left your party!"));
                        return true;

                    }

                    //herorpg party add
                    if(args[1].equalsIgnoreCase("add")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg party add <player>"));
                        return true;

                    }

                    //herorpg party remove
                    if(args[1].equalsIgnoreCase("remove")){

                        sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg party remove <player>"));
                        return true;

                    }
                    
                    //herorpg party <player>
                    if(sender.hasPermission("herorpg.party.inspect")){

                        IPlayer player = this.plugin.getPlayer(args[1]);

                        if(player == null) {
                            
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[1] + " &7could not be found!"));

                            return false;

                        }

                        IParty party = player.getParty();

                        if(party == null) {
                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &3"+ player.getName() +"&7 is not a member of any party."));
                            return true;

                        }

                        sender.sendMessage( Utils.chat("&8--------------------- &7[&6HeroRPG&7] &8---------------------") );

                        sender.sendMessage(Utils.chat("&7Party of the player &6" + party.getOwner().getName()));

                        sender.sendMessage("");
                        
                        sender.sendMessage(Utils.chat("&7Members:"));

                        for(IPlayer member : party.getPlayers()) {

                            if(party.isOwner(member)) {
                                continue;
                            }

                            sender.sendMessage("&7- &3" + member.getName());

                        }
                        
                        sender.sendMessage("");

                        sender.sendMessage( Utils.chat("&8---------------------------------------------------") );

                        return true;

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

                if(args[0].equalsIgnoreCase("party")){

                    if(!(sender instanceof Player)){
                        return false;
                    }

                    IPlayer owner = this.plugin.getPlayer(sender.getName());

                    IPlayer player = this.plugin.getPlayer(args[2]);

                    //herorpg party add <player>
                    if(args[1].equalsIgnoreCase("add")) {

                        if(sender.hasPermission("herorpg.party.manage")) {

                            IParty party = owner.getParty();

                            if(party == null) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &cYou are not a member of any party!"));
                                return false;
                            }

                            if(!party.isOwner(owner)) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &cYou are not the owner of the party!"));
                                return false;
                            }

                            if(player == null) {
                                
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));

                                return false;

                            }

                            if(player.getParty() != null) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &3" + player.getName() + "&7 is already in a party."));
                                return false;
                            }

                            String message = Utils.chat("&8[&6HeroRPG&8] &7Player &3" + player.getName() + "&7 has joined your party!");

                            party.notify(message, false);

                            this.partyManager.addToParty(party, player);

                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7You have successfully added player &3"+ player.getName() + "&7 to the party!"));
                            return true;

                        }

                        sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                        return false;

                    }

                    //herorpg party remove <player>
                    if(args[1].equalsIgnoreCase("remove")) {

                        if(sender.hasPermission("herorpg.party.manage")) {

                            IParty party = owner.getParty();

                            if(party == null) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &cYou are not a member of any party!"));
                                return false;
                            }

                            if(!party.isOwner(owner)) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &cYou are not the owner of the party!"));
                                return false;
                            }

                            if(player == null) {
                                
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &c" + args[2] + " &7could not be found!"));

                                return false;

                            }

                            if(party.isOwner(player)) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7You can't kick the owner of the party!"));
                                return false;
                            }

                            if(!party.isMember(player)) {
                                sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7Player &3" + player.getName() + "&7 is not a member of your party!"));
                                return false;
                            }

                            this.partyManager.removeFromParty(party, player);

                            String message = Utils.chat("&8[&6HeroRPG&8] &7Player &3" + player.getName() + "&7 has been kicked from your party!");

                            party.notify(message, false);

                            sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7You have successfully removed player &3"+ player.getName() + "&7 from the party!"));
                            return true;

                        }

                        sender.sendMessage(Utils.chat("&cYou do not have permission to execute this command!"));

                        return false;

                    }

                    //herorpg party <add:remove> <player>
                    sender.sendMessage(Utils.chat("&8[&6HeroRPG&8] &7/herorpg party <add:remove> " + player.getName()));

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
