package littlegruz.domination.listeners;

import littlegruz.domination.DomMain;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener{
   private DomMain plugin;
   
   public PlayerInteract(DomMain instance){
      plugin = instance;
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event){
      if(plugin.getRegManMap().get(event.getPlayer().getWorld().getName()) != null){
         if(event.getPlayer().getItemInHand().getType().compareTo(Material.STICK) == 0){
            Location loc;
            
            loc = event.getClickedBlock().getLocation().clone();
            loc.setY(loc.getY() + 1);
            
            /*TODO This will need to change to include the 'respawn wait' locations */
            plugin.getSpawnWaitMap().put(plugin.getSpawnPlace(), loc);
            plugin.setSpawnPlace("");
            event.getPlayer().sendMessage("Location set");
         }
      }
   }
}
