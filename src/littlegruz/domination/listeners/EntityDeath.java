package littlegruz.domination.listeners;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeath implements Listener{
   private DomMain plugin;

   public EntityDeath(DomMain instance){
      plugin = instance;
   }

   @EventHandler
   public void onEntityDeath(EntityDeathEvent event){
      if(event.getEntity().getKiller() instanceof Player
            && event.getEntity() instanceof Player){
         Player victim = (Player) event.getEntity();
         Player killa = event.getEntity().getKiller();

         /* Check if victim was killed by a projectile */
         if(event.getEntity().getLastDamageCause().getCause()
               .compareTo(DamageCause.PROJECTILE) == 0){
            /* TODO Check how far away the killer was from the victim */
            addPoints(killa, victim, 4);
         }
         /* Check if victim was killed by anything else a user can use */
         else{
            addPoints(killa, victim, 2);
         }
      }
      //plugin.getServer().broadcastMessage(event.getEntity().getLastDamageCause().getCause().toString());
   }

   private void addPoints(Player killa, Player victim, int type){
      if(plugin.getPlayerMap().get(killa.getName()) != null){
         if(plugin.getPartyMap().get(plugin.getPlayerMap().get(killa.getName()).getParty()) != null){
            Iterator<Map.Entry<String, CapturePoint>> it = plugin.getCapturePointMap().entrySet().iterator();
            String party = plugin.getPlayerMap().get(victim.getName()).getParty();
            
            plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                  plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                  + plugin.getPointsLegend().get(type));


            /* Check if killer killed a defender of their region */
            while(it.hasNext()){
               Entry<String, CapturePoint> cp = it.next();

               if(party.compareToIgnoreCase(cp.getValue().getOwner()) == 0){
                  plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                        plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                        + plugin.getPointsLegend().get(3));
               }
            }
         }
      }
   }
}
