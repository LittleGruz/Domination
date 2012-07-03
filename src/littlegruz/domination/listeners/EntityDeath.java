package littlegruz.domination.listeners;

import littlegruz.domination.DomMain;

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
   public void onEnemyDeath(EntityDeathEvent event){
      if(event.getEntity().getKiller() instanceof Player){
         Player killa = event.getEntity().getKiller();

         /* Check if victim was killed by a projectile */
         if(event.getEntity().getLastDamageCause().getCause()
               .compareTo(DamageCause.PROJECTILE) == 0){
            /* TODO Check how far away the killer was from the victim */
            addPoints(killa, 4);
         }
         /* Check if victim was killed by anything else a user can use */
         else{
            addPoints(killa, 2);
         }
      }
   }

   private void addPoints(Player killa, int type){
      if(plugin.getPlayerMap().get(killa.getName()) != null){
         if(plugin.getPartyMap().get(plugin.getPlayerMap().get(killa.getName()).getParty()) != null){
            
            plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                  plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                  + plugin.getPointsLegend().get(type));

            /* TODO Check if in hostile region
               plugin.getScoreMap().put(plugin.getPlayerMap().get(killa.getName()).getParty(),
                     plugin.getScoreMap().get(plugin.getPlayerMap().get(killa.getName()).getParty())
                     + plugin.getPointsLegend().get(3)); */
                  
         }
         
      }
      
   }
}
