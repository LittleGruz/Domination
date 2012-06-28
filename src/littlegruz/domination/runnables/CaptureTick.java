package littlegruz.domination.runnables;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;
import littlegruz.domination.entities.DomPlayer;

import org.bukkit.Location;

import com.sk89q.worldguard.protection.managers.RegionManager;

/*TODO
 * Capturing:
 * - Allow for non-captured points to slowly decrease health if no-one is on the point*/

public class CaptureTick implements Runnable{
   private DomMain plugin;
   private RegionManager regMan;
   private DomPlayer dp;
   private CapturePoint cp;

   public CaptureTick(DomMain instance, CapturePoint cp, DomPlayer dp, RegionManager regMan){
      plugin = instance;
      this.cp = cp;
      this.dp = dp;
      this.regMan = regMan;
   }
   
   @Override
   public void run(){
      if(regMan.getRegion(cp.getName()) != null
            && cp.getOwner().compareToIgnoreCase(dp.getParty()) != 0){
         Location loc;

         loc = plugin.getServer().getPlayer(dp.getName()).getLocation().clone();
         loc.setY(loc.getY() - 1);
         
         /* If there are nearby owners, then stop the capture */
         if(plugin.nearbyPartyMember(cp, regMan)){
            plugin.getServer().broadcastMessage("Capture of " + cp.getName() + " has been stopped");
            cp.setAttacker("");
            return;
         }
         /* If attacker is still in the area, do another tick */
         else if(regMan.getRegion(cp.getName()).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())){
            plugin.pointCaptureTick(cp, dp, regMan);
         }
         /* Else reset timer and stop */
         else{
            cp.setAttacker("");
            return;
         }
         
         /* If the attacking group are capturing the point */
         if(cp.getOwner().compareTo("") == 0){
            cp.setHealth(cp.getHealth() + 1);
            
            /* Change ownership if point is held for long enough */
            if(cp.getHealth() >= plugin.getCaptureTime()){
               cp.setOwner(dp.getParty());
               cp.setAttacker("");
               plugin.getServer().broadcastMessage("Capture point " + cp.getName() +
                     " has been captured by " + dp.getParty());
               return;
            }

            plugin.getServer().broadcastMessage((plugin.getCaptureTime() - cp.getHealth()) +
                  " seconds until " + dp.getParty() + " capture " + cp.getName());
         }
         /* If the attacking group are neutralising the owners' point */
         else{
            cp.setHealth(cp.getHealth() - 1);
            
            if(cp.getHealth() <= 0){
               cp.setOwner("");
            }
            
            if(cp.getHealth() != 0){
               plugin.getServer().broadcastMessage((plugin.getCaptureTime() -
                     (plugin.getCaptureTime() - cp.getHealth())) + " seconds left until " +
                     dp.getParty() + " neutralise " + cp.getName());
            }
            else
               plugin.getServer().broadcastMessage(dp.getParty() + " have neutralised " + cp.getName());
         }
      }
   }

}
