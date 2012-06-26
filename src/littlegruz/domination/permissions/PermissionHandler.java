package littlegruz.domination.permissions;

import org.bukkit.command.CommandSender;

/**
 * Class to handle permission node checks. Mostly only to support PEX natively,
 * due to SuperPerm compatibility with PEX issues.
 * 
 * @author Mitsugaru
 * 
 */
public class PermissionHandler{

   public static boolean has(CommandSender sender, PermissionNode node){
      return has(sender, node.getNode());
   }

   /**
    * 
    * @param sender
    *           - sender of command or player of event
    * @param node
    *           - Permission node to check, as String
    * @return True if sender has the node, else false
    */
   public static boolean has(CommandSender sender, String node){
      // Attempt to use SuperPerms or op
      if(sender.isOp() || sender.hasPermission(node)){
         return true;
      }
      // Else, they don't have permission
      return false;
   }
}
