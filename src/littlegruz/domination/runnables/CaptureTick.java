package littlegruz.domination.runnables;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.CapturePoint;

import org.bukkit.Location;

import com.sk89q.worldguard.protection.managers.RegionManager;

/*TODO
 * Capturing:
 * - Stop when owners are on the point
 * - Allow for non-captured points to slowly decrease health if no-one is on the point*/

public class CaptureTick implements Runnable{
   private DomMain plugin;
   private RegionManager regMan;
   private String name;
   private CapturePoint cp;

   public CaptureTick(DomMain instance, CapturePoint cp, String name, RegionManager regMan){
      plugin = instance;
      this.cp = cp;
      this.name = name;
      this.regMan = regMan;
   }
   
   @Override
   public void run(){
      if(regMan.getRegion(cp.getName()) != null
            && cp.getOwner().compareToIgnoreCase(name) != 0){
         Location loc;

         // TODO Change from player when parties are added
         loc = plugin.getServer().getPlayer(name).getLocation();
         loc.setY(loc.getY() - 1);
         
         /* If attacker is still in the area, do another tick */
         if(regMan.getRegion(cp.getName()).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())){
            plugin.pointCaptureTick(cp, name, regMan);
         }
         /* Else reset timer and stop */
         else{
            if(cp.getOwner().compareTo("") == 0)
               cp.setHealth(0);
            else
               cp.setHealth(plugin.getCaptureTime());
            
            cp.setAttacker("");
            return;
         }
         
         /* If the attacking group are capturing the point */
         if(cp.getOwner().compareTo("") == 0){
            cp.setHealth(cp.getHealth() + 1);
            
            /* Change ownership if point is held for long enough */
            if(cp.getHealth() >= plugin.getCaptureTime()){
               cp.setOwner(name);
               cp.setAttacker("");
               plugin.getServer().broadcastMessage("Capture point " + cp.getName() +
                     " has been captured by " + name);
               return;
            }

            plugin.getServer().broadcastMessage((plugin.getCaptureTime() - cp.getHealth()) +
                  " seconds until " + name + " capture " + cp.getName());
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
                     name + " neutralise " + cp.getName());
            }
            else
               plugin.getServer().broadcastMessage(name + " has neutralised " + cp.getName());
         }
      }
   }

}
