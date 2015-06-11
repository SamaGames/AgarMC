package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.util.Utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CPlayer {

	private final Player player;
	private final List<PlayerCell> cells = new ArrayList<PlayerCell>();
	private boolean isPlaying = false;
	private final ChatColor color;

	public CPlayer(Player player) {
		this.player = player;
		
		color = Utils.getRandomColor();
		
		AgarMC.get().getGame().equipSpectatingPlayer(player);
	}

	public Player getPlayer() {
		return player;
	}
	
	public void reinit() {
		for (PlayerCell cell : cells) {
			cell.remove();
			cells.remove(cell);
		}
	}

	public void removeCell(PlayerCell cell) {
		cells.remove(cell);
		if (cells.size() >= 1) {
			if(cell.isDriving()) {
				PlayerCell max = null;
				for (PlayerCell c : getCells()) {
					if((max == null || c.getMass() > max.getMass()) && !c.equals(cell)) {
						max = c;
					}
				}
				max.setDriving(true);
			}
		} else {
			onDeath();
		}
		cell.remove();
	}
	
	public void addCell(PlayerCell cell) {
		cells.add(cell);
	}

	public List<PlayerCell> getCells() {
		List<PlayerCell> cells = new ArrayList<PlayerCell>();
		for (PlayerCell c : this.cells) {
			cells.add(c);
		}
		return cells;
	}

	public void onDeath() {
		player.sendMessage(ChatColor.RED + "Vous n'avez plus de cellules en vie, vous avez perdu !");
		setPlaying(false);
		for(PlayerCell cell : cells)
			AgarMC.get().getGame().playerCellToStaticCell(cell);
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
		if(isPlaying) {
			AgarMC.get().getGame().equipPlayingPlayer(player);
		} else {
			AgarMC.get().getGame().equipSpectatingPlayer(player);
		}
	}
	
	public void remove() {
		// Add static cells
		
		setPlaying(false);
	}
	
	public PlayerCell getDriver() {
		for(PlayerCell cell : cells) {
			if(cell.isDriving()) return cell;
		}
		return null;
	}
	
	public void play() {
		PlayerCell base = AgarMC.get().getGame().safeSpawn(this);
		base.setDriving(true);
		setPlaying(true);
	}
	
	public void split() {
		for(PlayerCell cell : getCells()) {
			cell.split();
		}
	}
	
	public void ejectMass() {
		for(PlayerCell cell : getCells()) {
			cell.ejectMass();
		}
	}
	
	public String getNick() {
		return color + player.getName();
	}
	
	public int getTotalMass() {
		int total = 0;
		for(PlayerCell cell : getCells()) 
			total += cell.getMass();
		return total;
	}
	
	public void massChanged() {
		player.getPlayer().setLevel(getTotalMass());
	}
}
