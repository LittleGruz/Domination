package littlegruz.domination.commands;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.DomParty;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PartyCommand implements CommandExecutor{
   private DomMain plugin;
   
   public PartyCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.PARTY)){
         /* Create a party */
         if(cmd.getName().compareToIgnoreCase("createparty") == 0){
            if(args.length > 0){
               if(plugin.getPartyMap().get(args[0]) == null){
                  DomParty dp;
                  
                  plugin.getPartyMap().put(args[0], new DomParty(args[0]));
                  dp = plugin.getPartyMap().get(args[0]);
                  dp.addMember(sender.getName());
                  plugin.getPlayerMap().get(sender.getName()).setParty(args[0]);
                  
                  sendInvite(args, dp, 1);
                  sender.sendMessage(args[0] + " party created");
               }
               else
                  sender.sendMessage("A party with that name already exists");
            }
            else
               sender.sendMessage("Not enough arguments");
         }
         /* Join a party */
         else if(cmd.getName().compareToIgnoreCase("joinparty") == 0){
            if(args.length > 0){
               DomParty dp;
               
               dp = plugin.getPartyMap().get(args[0]);
               if(dp != null){
                  if(dp.isInvited(sender.getName())){
                     dp.addMember(sender.getName());
                     dp.removeInvitation(sender.getName());
                     plugin.getPlayerMap().get(sender.getName()).setParty(args[0]);
                     sender.sendMessage("Party joined");
                  }
                  else
                     sender.sendMessage("You have not been invited to that party"); 
               }
               else
                  sender.sendMessage("That party does not exist");
            }
            else
               sender.sendMessage("Not enough arguments");
         }
         /* Leave a party */
         else if(cmd.getName().compareToIgnoreCase("leaveparty") == 0){
            if(plugin.getPlayerMap().get(sender.getName()) != null){
               String party;
               
               party = plugin.getPlayerMap().get(sender.getName()).getParty();
               if(plugin.getPartyMap().get(party) != null){
                  plugin.getPartyMap().get(party).removeMember(sender.getName());
                  plugin.getPlayerMap().get(sender.getName()).setParty("");
                  sender.sendMessage("You left the party");
                  
                  /* Check if the player was the last one to leave */
                  if(plugin.getPartyMap().get(party).getMembers().size() == 0){
                     plugin.getPartyMap().remove(party);
                     sender.sendMessage("You were the last party member. Party disbanded");
                  }
               }
               else
                  sender.sendMessage("Y U NO HAVE PARTY?");
            }
         }
         /* Issue party invite(s) */
         else if(cmd.getName().compareToIgnoreCase("sendpartyinvite") == 0){
            if(args.length > 0){
               String party;
               
               party = plugin.getPlayerMap().get(sender.getName()).getParty();
               if(plugin.getPartyMap().get(party) != null)
                  sendInvite(args, plugin.getPartyMap().get(party), 0);
               else
                  sender.sendMessage("You must be in a party to invite someone");
            }
            else
               sender.sendMessage("Not enough arguments");
         }
         /* Remove party invite(s) */
         else if(cmd.getName().compareToIgnoreCase("removepartyinvite") == 0){
            if(args.length > 0){
               DomParty dp;
               String party;
               int i;
               
               party = plugin.getPlayerMap().get(sender.getName()).getParty();
               if(plugin.getPartyMap().get(party) != null){
                  dp = plugin.getPartyMap().get(party);
                  
                  for(i = 0; i < args.length; i++){
                     dp.removeInvitation(args[i]);
                     
                     /* Send invitation cancellation message to player if they are online */
                     if(plugin.getServer().getPlayer(args[i]) != null)
                        plugin.getServer().getPlayer(args[i]).sendMessage("Your invitation to join " + party + " has been revoked");
                  }
               }
               else
                  sender.sendMessage("You must be in a party to remove inviations");
            }
            else
               sender.sendMessage("Not enough arguments");
         }
      }
      return true;
   }
   
   private void sendInvite(String[] args, DomParty dp, int i){
      for(i = 0; i < args.length; i++){
         dp.addInvitation(args[i]);
         
         /* Send invitation message to player if they are online */
         if(plugin.getServer().getPlayer(args[i]) != null)
            plugin.getServer().getPlayer(args[i]).sendMessage("You have been invited to join " + dp.getName());
      }
   }
}
