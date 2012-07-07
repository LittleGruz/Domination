package littlegruz.domination.listeners;

import java.util.Iterator;
import java.util.Map.Entry;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener{
   private DomMain plugin;

   public PlayerRespawn(DomMain instance){
      plugin = instance;
   }

   @EventHandler
   public void onPlayerRespawn(PlayerRespawnEvent event){
      if(plugin.getRegManMap().get(event.getPlayer().getWorld().getName()) != null){
         String party;
         
         party = plugin.getPlayerMap().get(event.getPlayer().getName()).getParty();
         
         /* Check all capture points for a township that matches one in the
          * respawn HashMap*/
         /*TODO This may need to be edited to allow for the 'respawn wait' locations */
         Iterator<Entry<String, CapturePoint>> it = plugin.getCapturePointMap().entrySet().iterator();
         while(it.hasNext()){
            Entry<String, CapturePoint> cp = it.next();
            
            if(cp.getValue().getOwner().compareToIgnoreCase(party) == 0){
               event.setRespawnLocation(plugin.getSpawnWaitMap().get(cp.getValue().getTownship()));
               return;
            }
         }
         
         if(plugin.getSpawnWaitMap().get("default") != null)
            event.setRespawnLocation(plugin.getSpawnWaitMap().get("default"));
      }
   }
}
