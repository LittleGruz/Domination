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
      
      /* Just because I am lazy, delete later */
      if(event.getPlayer().getName().compareToIgnoreCase("little_gruz") == 0){
         plugin.getServer().dispatchCommand(event.getPlayer(), "adddomworld");
         plugin.getServer().dispatchCommand(event.getPlayer(), "addcapturepoint inner");
         plugin.getServer().dispatchCommand(event.getPlayer(), "addcapturepoint attack");
         plugin.getServer().dispatchCommand(event.getPlayer(), "addcapturepoint defend");
         plugin.getServer().dispatchCommand(event.getPlayer(), "addtownship world defend defend_town");
         plugin.getServer().dispatchCommand(event.getPlayer(), "addtownship world attack attack_town");
         plugin.getServer().dispatchCommand(event.getPlayer(), "setcapturebuff attack 0");
         plugin.getServer().dispatchCommand(event.getPlayer(), "setcapturebuff defend 1");
         plugin.getServer().dispatchCommand(event.getPlayer(), "createparty teh_4w3s0m3");
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event){
      plugin.getPlayerMap().remove(event.getPlayer().getName());
   }
}
