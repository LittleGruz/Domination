package littlegruz.domination.commands;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.domination.DomMain;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.protection.managers.RegionManager;

public class WorldCommand implements CommandExecutor{
   private DomMain plugin;
   
   public WorldCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.WORLDS)){
         /* Add a world to be affected by Domination */
         if(cmd.getName().compareToIgnoreCase("adddomworld") == 0){
            if(args.length >= 1){
               if(plugin.getServer().getWorld(args[0]) != null){
                  plugin.getRegManMap().put(args[0], plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld("world")));
                  sender.sendMessage("World added");
               }
               else
                  sender.sendMessage("No world found with that name");
            }
            else{
               if(sender instanceof Player){
                  Player playa = (Player) sender;
                  plugin.getRegManMap().put(playa.getWorld().getName(), plugin.getWorldGuard().getRegionManager(playa.getWorld()));
                  sender.sendMessage("World added");
               }
               else
                  sender.sendMessage("You can't use that command from console without specifying the world");
            }
         }
         else if(cmd.getName().compareToIgnoreCase("removedomworld") == 0){
            if(args.length >= 1){
               if(plugin.getServer().getWorld(args[0]) != null){
                  plugin.getRegManMap().remove(args[0]);
                  sender.sendMessage("World removed");
               }
               else
                  sender.sendMessage("No world found with that name");
            }
            else{
               if(sender instanceof Player){
                  Player playa = (Player) sender;
                  plugin.getRegManMap().remove(playa.getWorld().getName());
                  sender.sendMessage("World removed");
               }
               else
                  sender.sendMessage("You can't use that command from console without specifying the world");
            }
         }
         else if(cmd.getName().compareToIgnoreCase("displaydomworlds") == 0){
            Iterator<Map.Entry<String, RegionManager>> it = plugin.getRegManMap().entrySet().iterator();
            sender.sendMessage("Active worlds:");
            while(it.hasNext()){
               Entry<String, RegionManager> worlds = it.next();
               sender.sendMessage(worlds.getKey());
            }
         }
         else
            return false;
      }
      return true;
   }

}
