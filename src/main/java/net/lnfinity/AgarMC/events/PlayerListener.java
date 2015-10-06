package net.lnfinity.AgarMC.events;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.game.CPlayer;
import net.lnfinity.AgarMC.util.AgarTeams;
import net.lnfinity.AgarMC.util.GameType;
import net.samagames.api.SamaGamesAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
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
		} else if(item.getType() == SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem().getType()) {
			SamaGamesAPI.get().getGameManager().kickPlayer(player, ChatColor.RED + "Vous avez quitté la partie");
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
	public void onFoodChange(FoodLevelChangeEvent e){
		e.setCancelled(true);
		e.setFoodLevel(20);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent ev)
	{
		ev.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent ev)
	{
		if (ev.isCancelled())
			return ;
		ev.setCancelled(true);
		CPlayer cplayer = AgarMC.get().getGame().getCPlayer(ev.getPlayer());
		if (cplayer == null)
			return ;
		String msg = "";
		if (AgarMC.get().getGame().getGameType() == GameType.TEAMS)
		{
			AgarTeams team = AgarTeams.getTeam(cplayer.getColor());
			msg += ChatColor.DARK_GRAY + "[" + (team == null ? ChatColor.DARK_RED + "ERROR" : team.getDisplayName()) + ChatColor.DARK_GRAY + "]";
		}
		if (cplayer.isPlaying())
			msg += ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + cplayer.getTotalMass() + ChatColor.DARK_GRAY + "]";
		if (!msg.isEmpty())
			msg += " ";
		msg += ChatColor.GRAY + ev.getPlayer().getName() + ChatColor.WHITE + ": " + ev.getMessage();
		for (Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(msg);
		Bukkit.getConsoleSender().sendMessage(msg);
	}
	
	@EventHandler
	public void onPlayerArmorStandClick(PlayerArmorStandManipulateEvent ev)
	{
		ev.setCancelled(true);
	}
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent ev)
	{
		ev.setCancelled(true);
	}
}
