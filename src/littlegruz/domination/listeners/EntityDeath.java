package littlegruz.domination.listeners;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;

import org.bukkit.Location;
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
      if(plugin.getRegManMap().get(event.getEntity().getWorld().getName()) != null){
         if(event.getEntity().getKiller() instanceof Player
               && event.getEntity() instanceof Player){
            Player victim = (Player) event.getEntity();
            Player killa = event.getEntity().getKiller();
      
            /* Check if victim was killed by a projectile */
            if(event.getEntity().getLastDamageCause().getCause()
                  .compareTo(DamageCause.PROJECTILE) == 0){
               Location loc;
               
               /* Determine if the victim was far enough away for a long kill */
               loc = killa.getLocation().subtract(victim.getLocation());
               
               if((loc.getBlockX() > 15 && loc.getBlockX() < -15)
                     && (loc.getBlockY() > 15 && loc.getBlockY() < -15)
                     && (loc.getBlockZ() > 15 && loc.getBlockZ() < -15)){
                  /* Add long range bow kill points */
                  plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                        plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                        + plugin.getPointsLegend().get(5));
               }
               
               addPoints(killa, victim, 4);
            }
            else if(event.getEntity().getLastDamageCause().getCause()
                  .compareTo(DamageCause.SUICIDE) == 0){
               /* Add points for suicide */
               plugin.getScoreMap().put(plugin.getPlayerMap().get(victim.getName()).getParty(),
                     plugin.getScoreMap().get(plugin.getPlayerMap().get(victim.getName()).getParty())
                     + plugin.getPointsLegend().get(7));
            }
            /* Check if victim was killed by anything else a user can use */
            else{
               addPoints(killa, victim, 2);
            }
            
            /* Add points for player death */
            plugin.getScoreMap().put(plugin.getPlayerMap().get(victim.getName()).getParty(),
                  plugin.getScoreMap().get(plugin.getPlayerMap().get(victim.getName()).getParty())
                  + plugin.getPointsLegend().get(6));
         }
      }
   }

   private void addPoints(Player killa, Player victim, int type){
      if(plugin.getPlayerMap().get(killa.getName()) != null){
         if(plugin.getPartyMap().get(plugin.getPlayerMap().get(killa.getName()).getParty()) != null){
            Iterator<Map.Entry<String, CapturePoint>> it = plugin.getCapturePointMap().entrySet().iterator();
            String party = plugin.getPlayerMap().get(victim.getName()).getParty();
            
            /* Add points for the specific kill type */
            plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                  plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                  + plugin.getPointsLegend().get(type));

            /* Add base kill points if the kill type is not the base score */
            if(type != 2){
               plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                     plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                     + plugin.getPointsLegend().get(2));
            }

            /* Check if killer killed a defender of their region */
            while(it.hasNext()){
               Entry<String, CapturePoint> cp = it.next();

               if(party.compareToIgnoreCase(cp.getValue().getOwner()) == 0){
                  plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                        plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                        + plugin.getPointsLegend().get(3));
                  return;
               }
            }
         }
      }
   }
}
