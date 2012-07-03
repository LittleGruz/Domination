package littlegruz.domination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import littlegruz.domination.commands.CPCommand;
import littlegruz.domination.commands.PartyCommand;
import littlegruz.domination.commands.TownCommand;
import littlegruz.domination.commands.WorldCommand;
import littlegruz.domination.entities.CapturePoint;
import littlegruz.domination.entities.DomParty;
import littlegruz.domination.entities.DomPlayer;
import littlegruz.domination.listeners.EntityDeath;
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
   private HashMap<String, DomParty> partyMap;
   private HashMap<String, RegionManager> regManMap;
   private HashMap<String, Integer> scoreMap;
   private List<Integer> pointsLegend;
   private int captureTime;
   
   public void onEnable(){
      /* Get the WorldGuard plugin */
      worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

      /* Set up HashMaps */
      playerMap = new HashMap<String, DomPlayer>();
      capturePointMap = new HashMap<String, CapturePoint>();
      partyMap = new HashMap<String, DomParty>();
      regManMap = new HashMap<String, RegionManager>();
      scoreMap = new HashMap<String, Integer>(); // The string is the party name
      pointsLegend = new ArrayList<Integer>(8);
      
      /* Get config.yml data */
      loadConfig();

      /* Register listeners */
      getServer().getPluginManager().registerEvents(new PlayerMove(this), this);
      getServer().getPluginManager().registerEvents(new PlayerConnection(this), this);
      getServer().getPluginManager().registerEvents(new PlayerInteract(this), this);
      getServer().getPluginManager().registerEvents(new EntityDeath(this), this);

      /* Register capture point commands */
      getCommand("addcapturepoint").setExecutor(new CPCommand(this));
      getCommand("removecapturepoint").setExecutor(new CPCommand(this));

      /* Register township commands */
      getCommand("addtownship").setExecutor(new TownCommand(this));
      getCommand("removetownship").setExecutor(new TownCommand(this));

      /* Register party commands */
      getCommand("createparty").setExecutor(new PartyCommand(this));
      getCommand("joinparty").setExecutor(new PartyCommand(this));
      getCommand("leaveparty").setExecutor(new PartyCommand(this));
      getCommand("sendpartyinvite").setExecutor(new PartyCommand(this));
      getCommand("removepartyinvite").setExecutor(new PartyCommand(this));

      /* Register world commands */
      getCommand("adddomworld").setExecutor(new WorldCommand(this));
      getCommand("removedomworld").setExecutor(new WorldCommand(this));
      getCommand("displaydomworlds").setExecutor(new WorldCommand(this));
      
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

   public HashMap<String, DomParty> getPartyMap(){
      return partyMap;
   }

   public HashMap<String, Integer> getScoreMap(){
      return scoreMap;
   }

   public void setRegManMap(HashMap<String, RegionManager> regManMap){
      this.regManMap = regManMap;
   }

   public HashMap<String, RegionManager> getRegManMap(){
      return regManMap;
   }

   public int getCaptureTime(){
      return captureTime;
   }
   
   public void setCaptureTime(int captureTime){
      this.captureTime = captureTime;
   }
   
   public List<Integer> getPointsLegend(){
      return pointsLegend;
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
   public void pointCaptureTick(final CapturePoint cp, final DomPlayer dp, final RegionManager regMan){
      getServer().getScheduler().scheduleSyncDelayedTask(this, new CaptureTick(this, cp, dp, regMan), 20L);
   }
   
   /* Determine if a capture points owner is in its region */
   public boolean nearbyPartyMember(CapturePoint cp, RegionManager regMan){
      Location loc;
      
      if(partyMap.get(cp.getOwner()) != null){
         /* Get all the owning party's members */
         Iterator<Map.Entry<String, String>> it = partyMap.get(cp.getOwner()).getMembers().entrySet().iterator();
         while(it.hasNext()){
            Entry<String, String> player = it.next();
            loc = getServer().getPlayer(player.getKey()).getLocation().clone();
            loc.setY(loc.getY() - 1);
            
            /* Check if there is a member of the owning party on the point */
            if(regMan.getRegion(cp.getName()).contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()))
               return true;
         }
      }
      
      return false;
   }
   
   /* Load data from the config.yml file */
   private void loadConfig(){
      if(getConfig().isInt("capture_time"))
         captureTime = getConfig().getInt("capture_time");
      else{
         captureTime = 5;
         getConfig().set("capture_time", 5);
      }
      
      /* List position | Description
       * 0 | Point capture
       * 1 | Point neutralise
       * 2 | Enemy kill
       * 3 | Enemy kill in hostile region
       * 4 | Bow kill
       * 5 | Long range bow kill
       * 6 | Death
       * 7 | Suicide */ 
      if(getConfig().isList("points"))
         pointsLegend = getConfig().getIntegerList("points");
      else{
         pointsLegend.add(0, 150);
         pointsLegend.add(1, 75);
         pointsLegend.add(2, 25);
         pointsLegend.add(3, 40);
         pointsLegend.add(4, 15);
         pointsLegend.add(5, 30);
         pointsLegend.add(6, 0);
         pointsLegend.add(7, -100);
         
         getConfig().set("points", pointsLegend);
      }
   }
}
