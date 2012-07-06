package littlegruz.domination.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import littlegruz.domination.DomMain;
import littlegruz.domination.permissions.PermissionHandler;
import littlegruz.domination.permissions.PermissionNode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ScoreCommand implements CommandExecutor{
   private DomMain plugin;
   
   public ScoreCommand(DomMain instance){
      plugin = instance;
   }

   @Override
   public boolean onCommand(CommandSender sender, Command cmd,
         String commandLabel, String[] args){
      if(PermissionHandler.has(sender, PermissionNode.SCORES)){
         /* Displays scores in descending score order */
         if(cmd.getName().compareToIgnoreCase("domscores") == 0){
            Iterator<Map.Entry<String, Integer>> it = plugin.getScoreMap().entrySet().iterator();
            ArrayList<String> leaderBoard = new ArrayList<String>(plugin.getScoreMap().size());
            StringTokenizer st;
            int i, j;
            
            sender.sendMessage("Current party rankings:");
            
            for(i = 0; i < plugin.getScoreMap().size(); i++){
               while(it.hasNext()){
                  Entry<String, Integer> score = it.next();
                  
                  for(j = 0; j < leaderBoard.size(); j++){
                     st = new StringTokenizer(leaderBoard.get(j), "|");
                     /* If the score from the HashMap is smaller, continue the loop*/
                     if(st.nextToken().compareTo(Integer.toString(score.getValue())) <= 0)
                        break;
                  }
                  
                  leaderBoard.add(j, score.getValue() + "|" + score.getKey());
               }
            }
            
            /* Print out the top 10 scores */
            for(i = 0; i < leaderBoard.size() && i < 10; i++)
               sender.sendMessage(leaderBoard.get(i));
            
            return true;
         }
      }
      return false;
   }

}
