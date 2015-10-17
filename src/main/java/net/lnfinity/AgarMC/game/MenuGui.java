package net.lnfinity.AgarMC.game;

import java.util.Arrays;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.util.GameType;
import net.samagames.api.SamaGamesAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuGui
{
	public static final String INV_NAME = "Menu";
	
	public static void display(Player player)
	{
		Inventory inv = Bukkit.createInventory(player, 9, INV_NAME);
		if (AgarMC.get().getGame().getGameType() != GameType.TEAMS)
			inv.setItem(0, Game.constructItem(Material.WOOL, 1, (byte) 14, ChatColor.AQUA + "" + ChatColor.BOLD + "Choisir sa couleur", null));
		inv.setItem(4, Game.constructItem(Material.SNOW_BALL, 1, (byte) 0, ChatColor.AQUA + "Mode spectateur", Arrays.asList(ChatColor.GRAY + "Vous permet de visualiser la partie en cours")));
		inv.setItem(8, SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem());
		player.openInventory(inv);
	}
	
	public static void onClick(Player p, ItemStack item)
	{
		CPlayer cplayer = AgarMC.get().getGame().getCPlayer(p);
		if(cplayer == null) return;
		
		if(cplayer.isPlaying())
		{
			if(item.getType() == Material.SNOW_BALL) {
				cplayer.onDeath();
			}
		}
		
		if(item.getType() == SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem().getType()) {
			SamaGamesAPI.get().getGameManager().kickPlayer(p, ChatColor.RED + "Vous avez quitté la partie");
		} else if(item.getType() == Material.WOOL) {
			TeamSelectorGui.display(p);
		}
	}
}
