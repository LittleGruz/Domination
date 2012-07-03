package littlegruz.domination.commands;

import littlegruz.domination.DomMain;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TownCommand implements CommandExecutor{
   private DomMain plugin;
   
   public TownCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.TOWNSHIPS)){
         /* Add township area point */
         if(cmd.getName().compareToIgnoreCase("addtownship") == 0){
            if(args.length == 2){
               if(sender instanceof Player){
                  if(plugin.getWorldGuard().getRegionManager(((Player)sender).getWorld()).hasRegion(args[0])
                        && plugin.getWorldGuard().getRegionManager(((Player)sender).getWorld()).hasRegion(args[1])){
                     if(plugin.getCapturePointMap().get(args[0]) != null){
                        plugin.getCapturePointMap().get(args[0]).setTownship(args[1]);
                        sender.sendMessage("Township added");
                     }
                     else
                        sender.sendMessage("No capture point found with that name");
                  }
                  else
                     sender.sendMessage("No region found with one of those names");
               }
               else{
                  sender.sendMessage("You must specify the world name if using the console");
                  return false;
               }
            }
            else if(args.length == 3){
               if(plugin.getServer().getWorld(args[0]) != null){
                  if(plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld(args[0])).hasRegion(args[1])
                        && plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld(args[0])).hasRegion(args[2])){
                     if(plugin.getCapturePointMap().get(args[1]) != null){
                        plugin.getCapturePointMap().get(args[1]).setTownship(args[2]);
                        sender.sendMessage("Township added");
                     }
                     else
                        sender.sendMessage("No capture point found with that name");
                  }
                  else
                     sender.sendMessage("No region found with that name");
               }
               else
                  sender.sendMessage("No world found with that name");
            }
            else
               sender.sendMessage("Wrong number of arguments");
         }
         /* Remove township area point */
         else if(cmd.getName().compareToIgnoreCase("removetownship") == 0){
            if(args.length == 2){
               if(plugin.getCapturePointMap().get(args[0]) != null){
                  plugin.getCapturePointMap().get(args[0]).setTownship(args[1]);
                  sender.sendMessage("Township removed");
               }
               else
                  sender.sendMessage("No capture point found with that name");
            }
            else
               sender.sendMessage("Wrong number of arguments");
         }
         else
            return false;
      }
      else
         sender.sendMessage("You do not have sufficient permissions");
      return true;
   }
}
