package littlegruz.domination.commands;

import littlegruz.domination.DomMain;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RespawnCommand implements CommandExecutor{
   private DomMain plugin;
   
   public RespawnCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.SPAWNS)){
         /* Add default spawn area */
         if(cmd.getName().compareToIgnoreCase("setdefaultrespawn") == 0){
            plugin.setSpawnPlace("default");
            sender.sendMessage("Hit a block with a stick to make it the default spawn location");
         }
         /* Add custom spawn area */
         else if(cmd.getName().compareToIgnoreCase("setrespawn") == 0){
            if(args.length == 1){
               plugin.setSpawnPlace(args[0]);
               sender.sendMessage("Hit a block with a stick to make it a town spawn location");
            }
            else
               sender.sendMessage("Wrong number of arguments");
         }
         /* Cancel custom spawn placement */
         else if(cmd.getName().compareToIgnoreCase("cancelrespawn") == 0){
            if(args.length == 1){
               plugin.setSpawnPlace("");
               sender.sendMessage("Placement canceled");
            }
            else
               sender.sendMessage("Wrong number of arguments");
         }
      }
      return true;
   }
}
