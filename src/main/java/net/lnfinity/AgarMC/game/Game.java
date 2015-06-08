package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.cells.StaticCell;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Game {

	private final List<StaticCell> staticCells = Collections.synchronizedList(new ArrayList<StaticCell>());
	private final List<CPlayer> players = Collections.synchronizedList(new ArrayList<CPlayer>());
	
	public Game() {
		//initialize();
	}
	
	public List<CPlayer> getPlayers() {
		List<CPlayer> players = new ArrayList<CPlayer>();
		for (CPlayer p : this.players) {
			players.add(p);
		}
		return players;
	}
	
	public synchronized List<StaticCell> getStaticCells() {
		List<StaticCell> statics = new ArrayList<StaticCell>();
		synchronized(staticCells) {
		      Iterator<StaticCell> i = staticCells.iterator(); // Must be in synchronized block
		      while (i.hasNext())
		          statics.add((StaticCell) i.next());
		 }
		/*for (StaticCell c : this.staticCells) {
			staticCells.add(c);
		}*/
		return statics;
	}
	
	public void addPlayer(CPlayer player) {
		players.add(player);
	}
	
	public void addStaticCell(StaticCell cell) {
		staticCells.add(cell);
	}
	
	public void removeStaticCell(StaticCell cell) {
		cell.remove();
		staticCells.remove(cell);
	}
	
	public void removePlayer(CPlayer player) {
		player.remove();
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		removePlayer(player.getUniqueId());
	}
	
	public void removePlayer(UUID id) {
		for(CPlayer player : players) {
			if(player.getPlayer().getUniqueId().equals(id)) {
				players.remove(player);
				return;
			}
		}
	}
	
	public CPlayer getCPlayer(Player player) {
		for(CPlayer p : getPlayers()) {
			if(p.getPlayer().equals(player)) {
				return p;
			}
		}
		return null;
	}
	
	public PlayerCell safeSpawn(CPlayer player) {
		PlayerCell cell = new PlayerCell(player, 100, Math.random() * 98 + 1, Math.random() * 98 + 1);
		player.addCell(cell);
		return cell;
	}
	
	public void initialize() {
		for(int i = 0; i < 1000; i++) {
			StaticCell cell = new StaticCell(Math.random() * 100, Math.random() * 100);
			staticCells.add(cell);
		}
	}
	
	public void equipPlayingPlayer(final Player player) {
		player.getInventory().clear();
		player.getInventory().setItem(0, constructItem(Material.MAGMA_CREAM, 1, (byte) 0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Se diviser", Arrays.asList(ChatColor.GRAY + "Divise toutes vos cellules en deux")));
		player.getInventory().setItem(1, constructItem(Material.SLIME_BALL, 1, (byte) 0, ChatColor.GREEN + "" + ChatColor.BOLD + "Ejecter de la Matière", Arrays.asList(ChatColor.GRAY + "Ejecte une cellule inerte dans votre direction")));
		player.getInventory().setItem(7, constructItem(Material.SNOW_BALL, 1, (byte) 0, ChatColor.AQUA + "Mode spectateur", Arrays.asList(ChatColor.GRAY + "Vous permet de visualiser la partie en cours")));
		player.getInventory().setItem(8, constructItem(Material.IRON_DOOR, 1, (byte) 0, ChatColor.RED + "Quitter", Arrays.asList("")));
	}

	public void equipSpectatingPlayer(final Player player) {
		player.getInventory().clear();
		player.getInventory().setItem(0, constructItem(Material.NETHER_STAR, 1, (byte) 0, ChatColor.AQUA + "" + ChatColor.BOLD + "Jouer", Arrays.asList("")));
		player.getInventory().setItem(4, constructItem(Material.BOOK, 1, (byte) 0, ChatColor.LIGHT_PURPLE + "Règles", Arrays.asList(ChatColor.GRAY + "Visualiser les règles du jeu")));
		player.getInventory().setItem(8, constructItem(Material.IRON_DOOR, 1, (byte) 0, ChatColor.RED + "Quitter", Arrays.asList("")));
	}

	public ItemStack constructItem(Material type, int amount, byte data, String display, List<String> lore) {
		ItemStack item = new ItemStack(type, amount, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
