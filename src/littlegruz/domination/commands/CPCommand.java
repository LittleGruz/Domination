package littlegruz.domination.commands;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CPCommand implements CommandExecutor{
   private DomMain plugin;
   
   public CPCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(sender.hasPermission("domination.capturepoints")){
         /* Add capture point */
         if(cmd.getName().compareToIgnoreCase("addcapturepoint") == 0){
            if(args.length == 1){
               plugin.getCapturePointMap().put(args[0], new CapturePoint(args[0]));
               sender.sendMessage("Capture point added");
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
