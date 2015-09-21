package net.lnfinity.AgarMC.events;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.game.CPlayer;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
		
		if(cplayer == null) return;
		
		 if(item.getType() == Material.WRITTEN_BOOK) {
			e.setCancelled(false);
			return ;
		}
		
		if(!cplayer.isPlaying()) {
			if(item.getType() == Material.NETHER_STAR) {
				player.sendMessage(ChatColor.DARK_GREEN + "Vous entrez dans le jeu, bonne chance !");
				cplayer.play();
			}
		} else {
			if(item.getType() == Material.MAGMA_CREAM) {
				cplayer.split();
			} else if(item.getType() == Material.SLIME_BALL) {
				cplayer.ejectMass();
			} else if(item.getType() == Material.SNOW_BALL) {
				cplayer.onDeath();
			}
		}
	}
	
	@EventHandler
	public void onPlayerEntityInteract(PlayerInteractEntityEvent e)
	{
		PlayerInteractEvent ev = new PlayerInteractEvent(e.getPlayer(), Action.RIGHT_CLICK_AIR, e.getPlayer().getItemInHand(), null, null);
		onPlayerInteract(ev);
		e.setCancelled(ev.isCancelled());
	}
	
	@EventHandler
	public void onFoodChange(FoodLevelChangeEvent e){
		e.setCancelled(true);
		e.setFoodLevel(20);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ev)
	{
		ev.setCancelled(true);
	}
}
