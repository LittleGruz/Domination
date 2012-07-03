package littlegruz.domination.commands;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CPCommand implements CommandExecutor{
   private DomMain plugin;
   
   public CPCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.CAPTUREPOINTS)){
         /* Add capture point */
         if(cmd.getName().compareToIgnoreCase("addcapturepoint") == 0){
            if(args.length == 1){
               if(sender instanceof Player){
                  if(plugin.getWorldGuard().getRegionManager(((Player) sender).getWorld()).hasRegion(args[0])){
                     plugin.getCapturePointMap().put(args[0], new CapturePoint(args[0]));
                     sender.sendMessage("Capture point added");
                  }
                  else
                     sender.sendMessage("No region found with that name");
               }
               else{
                  sender.sendMessage("You must specify the world name if using the console");
                  return false;
               }
            }
            else if(args.length == 2){
               if(plugin.getServer().getWorld(args[0]) != null){
                  if(plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld(args[0])).hasRegion(args[1])){
                     plugin.getCapturePointMap().put(args[1], new CapturePoint(args[1]));
                     sender.sendMessage("Capture point added");
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
         /* Remove capture point */
         else if(cmd.getName().compareToIgnoreCase("removecapturepoint") == 0){
            if(args.length == 1){
               plugin.getCapturePointMap().remove(args[0]);
               sender.sendMessage("Capture point removed");
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
