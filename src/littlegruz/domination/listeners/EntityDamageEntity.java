package littlegruz.domination.listeners;

import java.util.Iterator;
import java.util.Map.Entry;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class EntityDamageEntity implements Listener{
   private DomMain plugin;
   
   public EntityDamageEntity(DomMain instance){
      plugin = instance;
   }

   @EventHandler
   public void onEntityDamageEntity(EntityDamageByEntityEvent event){
      if(plugin.getRegManMap().get(event.getEntity().getWorld().getName()) != null){
         /* If a player damages anything inside their township increase the
          * damage delt by half a heart*/
         if(event.getDamager() instanceof Player)
            townBuffAlloc(event, plugin.getPlayerMap().get(((Player) event.getDamager()).getName()).getParty(), true);
         /* If a player damages anything inside their township decrease the
          * damage delt by half a heart*/
         else if(event.getEntity() instanceof Player)
            townBuffAlloc(event, plugin.getPlayerMap().get(((Player) event.getEntity()).getName()).getParty(), false);
      }
   }
   
   private void townBuffAlloc(EntityDamageByEntityEvent event, String party, boolean attack){
      if(party.compareTo("") != 0){
         Location loc;
         RegionManager rm;
         ProtectedRegion town;
         
         loc = event.getDamager().getLocation();
         loc.setY(loc.getY() - 1);

         rm = plugin.getWorldGuard().getRegionManager(event.getDamager().getWorld());
         town = plugin.getTownshipByLocation(loc, rm.getRegions());
         if(town != null){
            Iterator<Entry<String, CapturePoint>> it = plugin.getCapturePointMap().entrySet().iterator();
            
            while(it.hasNext()){
               Entry<String, CapturePoint> cp = it.next();

               /* If the players party owns the capture point in the
                * town then give them the buff */
               if(attack){
                  if(cp.getValue().getOwner().compareTo(party) == 0
                        && cp.getValue().getTownship().compareTo(town.getId()) == 0){
                     /* Increase damage */
                     if(cp.getValue().getBuff() == 0){
                        event.setDamage(event.getDamage() + 1);
                        break;
                     }
                  }
               }
               else{
                  if(cp.getValue().getOwner().compareTo(party) == 0
                        && cp.getValue().getTownship().compareTo(town.getId()) == 0){
                     /* Decrease damage */
                     if(cp.getValue().getBuff() == 1){
                        event.setDamage(event.getDamage() - 1);
                        break;
                     }
                  }
               }
            }
         }
      }
   }
}
