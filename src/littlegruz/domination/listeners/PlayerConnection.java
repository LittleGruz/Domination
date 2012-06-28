package littlegruz.domination.listeners;

import littlegruz.domination.DomMain;
import littlegruz.domination.entities.DomPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnection implements Listener{
   private DomMain plugin;
   
   public PlayerConnection(DomMain instance){
      plugin = instance;
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event){
      plugin.getPlayerMap().put(event.getPlayer().getName(), new DomPlayer(event.getPlayer().getName()));
      plugin.getRegManMap().put("world", plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld("world")));
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event){
      plugin.getPlayerMap().remove(event.getPlayer().getName());
   }
}
