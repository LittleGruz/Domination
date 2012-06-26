package littlegruz.domination;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.domination.commands.CPCommand;
import littlegruz.domination.entities.CapturePoint;
import littlegruz.domination.entities.DomPlayer;
import littlegruz.domination.listeners.PlayerConnection;
import littlegruz.domination.listeners.PlayerInteract;
import littlegruz.domination.listeners.PlayerMove;
import littlegruz.domination.runnables.CaptureTick;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class DomMain extends JavaPlugin{
   private WorldGuardPlugin worldGuard;
   private HashMap<String, DomPlayer> playerMap;
   private HashMap<String, CapturePoint> capturePointMap;
   private int captureTime;
   
   public void onEnable(){
      /* Get the WorldGuard plugin */
      worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");
      
      /* Get config.yml data */
      if(getConfig().isInt("capture_time"))
         captureTime = getConfig().getInt("capture_time");
      else
         captureTime = 5;

      /* Set up HashMaps */
      playerMap = new HashMap<String, DomPlayer>();
      capturePointMap = new HashMap<String, CapturePoint>();

      /* Register listeners */
      getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
      getServer().getPluginManager().registerEvents(new PlayerConnection(this), this);
      getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);

      /* Register commands */
      getCommand("addcapturepoint").setExecutor(new CPCommand(this));
      getCommand("removecapturepoint").setExecutor(new CPCommand(this));
      
      getLogger().info(this.toString() + " enabled");
   }

   public void onDisable(){
      saveConfig();
      getLogger().info(this.toString() + " disabled");
   }
   
   public WorldGuardPlugin getWorldGuard(){
      return worldGuard;
   }

   public HashMap<String, DomPlayer> getPlayerMap(){
      return playerMap;
   }

   public HashMap<String, CapturePoint> getCapturePointMap(){
      return capturePointMap;
   }
   
   public int getCaptureTime(){
      return captureTime;
   }
   
   public void setCaptureTime(int captureTime){
      this.captureTime = captureTime;
   }
   
   /* Find a ProtectedRegion from a given location */
   public ProtectedRegion getRegionByLocation(Location loc, Map<String, ProtectedRegion> regionsMap){
      Iterator<Map.Entry<String, ProtectedRegion>> it = regionsMap.entrySet().iterator();
      
      /* Search through every region until the desired one is reached */
      while(it.hasNext()){
         Entry<String, ProtectedRegion> region = it.next();
         
         if(region.getValue().contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())){
            return region.getValue();
         }
      }
      
      return null;
   }
   
   /* Sets the interval of the capturing check */
   public void pointCaptureTick(final CapturePoint cp, final String name, final RegionManager regMan){
      getServer().getScheduler().scheduleSyncDelayedTask(this, new CaptureTick(this, cp, name, regMan), 20L);
   }
}
