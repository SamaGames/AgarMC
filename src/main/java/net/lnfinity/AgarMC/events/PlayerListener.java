package net.lnfinity.AgarMC.events;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.game.CPlayer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		e.setCancelled(true);
		e.getPlayer().updateInventory();
		
		ItemStack item = e.getItem();
		if(item == null) return;
		Player player = e.getPlayer();
		
		CPlayer cplayer = AgarMC.get().getGame().getCPlayer(player);
		
		if(!cplayer.isPlaying()) {
			if(item.getType() == Material.NETHER_STAR) {
				cplayer.play();
			}
		} else {
			if(item.getType() == Material.MAGMA_CREAM) {
				cplayer.split();
			} else if(item.getType() == Material.SLIME_BALL) {
				cplayer.ejectMass();
			}
		}
		
	}
}
