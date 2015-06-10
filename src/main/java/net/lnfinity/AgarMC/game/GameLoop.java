package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.util.Utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GameLoop implements Runnable {
	
	@Override
	public void run() {
		for(CPlayer player : AgarMC.get().getGame().getPlayers()) {
			if(!player.isPlaying()) continue;

			Vector vector = player.getPlayer().getLocation().getDirection().setY(0).normalize();
			Location to = player.getPlayer().getLocation().add(vector);
			
			for(PlayerCell playerCell : player.getCells()) {
				
				//### Moving players ###//
				
				Vector direction = Utils.getDirection(playerCell.getX(), playerCell.getY(), to.getX(), to.getZ()).multiply(1 / Math.log(playerCell.getMass()));
				Vector dir = new Vector();
				if(!player.getDriver().equals(playerCell))
					dir = player.getPlayer().getLocation().getDirection().setY(0).normalize().multiply(1 / Math.log(playerCell.getMass())).multiply(0.8);
				direction.add(dir);
				playerCell.move(direction.getX(), direction.getZ());
				
				//### Eating tests ###//
				
				//** Static Cells **//
				for(StaticCell staticCell : AgarMC.get().getGame().getStaticCells()) {
					if(playerCell.getMass() > staticCell.getMass() && Math.sqrt(Math.pow(playerCell.getX() - staticCell.getX(), 2) + Math.pow(playerCell.getY() - staticCell.getY(), 2)) < playerCell.getRadius() - staticCell.getRadius()) {
						playerCell.increaseMass(staticCell.getMass());
						AgarMC.get().getGame().removeStaticCell(staticCell);
					}
				}
				
				//** Players Cells **//
				for(CPlayer opponent : AgarMC.get().getGame().getPlayers()) {
					if(!opponent.isPlaying() || opponent.equals(player)) continue;
					for(PlayerCell opponentCell : opponent.getCells()) {
						if(playerCell.getMass() > opponentCell.getMass() && Math.sqrt(Math.pow(playerCell.getX() - opponentCell.getX(), 2) + Math.pow(playerCell.getY() - opponentCell.getY(), 2)) < playerCell.getRadius() - opponentCell.getRadius()) {
							playerCell.increaseMass(opponentCell.getMass());
							opponent.removeCell(opponentCell);
						}
					}
				}
				
				//** Owner Cells (merge) **//
				for(PlayerCell other : player.getCells()) {
					if(other.equals(playerCell) || playerCell.getMass() < other.getMass() || !other.canMerge()) continue;
					if(playerCell.getMass() > other.getMass() && Math.sqrt(Math.pow(playerCell.getX() - other.getX(), 2) + Math.pow(playerCell.getY() - other.getY(), 2)) < playerCell.getRadius() - other.getRadius()) {
						playerCell.increaseMass(other.getMass());
						player.removeCell(other);
					}
				}
			}
		}
	}

}
