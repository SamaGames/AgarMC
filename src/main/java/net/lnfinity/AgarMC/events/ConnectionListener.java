package net.lnfinity.AgarMC.events;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.game.CPlayer;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		AgarMC.get().getGame().addPlayer(new CPlayer(e.getPlayer()));
		
		e.getPlayer().setGameMode(GameMode.ADVENTURE);
		e.getPlayer().setLevel(0);
		e.getPlayer().setFoodLevel(20);
		e.getPlayer().setSaturation(20);
		
		for(Player player : AgarMC.get().getServer().getOnlinePlayers()) {
			if(player.equals(e.getPlayer())) continue;
			player.hidePlayer(e.getPlayer());
			e.getPlayer().hidePlayer(player);
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		CPlayer player = AgarMC.get().getGame().getCPlayer(e.getPlayer());
		for(PlayerCell cell : player.getCells()) {
			AgarMC.get().getGame().playerCellToStaticCell(cell);
		}
		
		AgarMC.get().getGame().removePlayer(player);
	}
	
}
