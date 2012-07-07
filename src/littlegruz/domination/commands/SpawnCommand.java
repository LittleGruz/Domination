package littlegruz.domination.commands;

import littlegruz.domination.DomMain;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpawnCommand implements CommandExecutor{
   private DomMain plugin;
   
   public SpawnCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.SPAWNS)){
         /* Add default spawn area */
         if(cmd.getName().compareToIgnoreCase("setdefaultspawn") == 0){
            plugin.setSpawnPlace("default");
            sender.sendMessage("Right click a block to make it the default spawn location");
         }
         /* Add custom spawn area */
         else if(cmd.getName().compareToIgnoreCase("setspawn") == 0){
            if(args.length == 1){
               plugin.setSpawnPlace(args[0]);
               sender.sendMessage("Right click a block to make it the default spawn location");
            }
            else
               sender.sendMessage("Wrong number of arguments");
         }
      }
      return true;
   }
}
