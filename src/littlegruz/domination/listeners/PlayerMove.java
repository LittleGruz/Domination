package littlegruz.domination.listeners;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;
import littlegruz.domination.entities.DomPlayer;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlayerMove implements Listener{
   private DomMain plugin;
   
   public PlayerMove(DomMain instance){
      plugin = instance;
   }
   
   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event){
      if(plugin.getRegManMap().get(event.getPlayer().getWorld().getName()) != null){
         Location loc;
         DomPlayer playa;
         RegionManager regMan;
         ProtectedRegion pr;
         CapturePoint cp;
         
         loc = event.getPlayer().getLocation();
         loc.setY(loc.getY() - 1);
         
         playa = plugin.getPlayerMap().get(event.getPlayer().getName());
         
         regMan = plugin.getRegManMap().get(loc.getWorld().getName());
         
         /* Point capture stuff here*/
         if((pr = plugin.getRegionByLocation(loc, regMan.getRegions())) != null){
            if((cp = plugin.getCapturePointMap().get(pr.getId())) != null){
               /* Check that there are no attackers */
               if(cp.getAttacker().compareTo("") == 0){
                  /* Check that the player is in a party */
                  if(playa.getParty().compareTo("") != 0){
                     /* Check if the player is one of the owners */
                     if(cp.getOwner().compareToIgnoreCase(playa.getParty()) != 0){
                        /* Check if any defender is nearby */
                        if(!plugin.nearbyPartyMember(cp, regMan)){
                           cp.setAttacker(playa.getParty());
                           
                           event.getPlayer().sendMessage(cp.getName() + " is being captured!");
            
                           plugin.pointCaptureTick(cp, playa, regMan);
                        }
                     }
                     else{
                        if(cp.getHealth() < plugin.getCaptureTime())
                           cp.setHealth(plugin.getCaptureTime());
                     }
                  }
               }
            }
         }
      }
   }
}
