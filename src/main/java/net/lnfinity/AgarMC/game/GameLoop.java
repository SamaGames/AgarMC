package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.GameType;
import net.lnfinity.AgarMC.util.Utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class GameLoop implements Runnable {
	
	@Override
	public void run() {
		for(CPlayer player : AgarMC.get().getGame().getPlayers()) {
			if(!player.isPlaying()) continue;

			Vector vector = player.getPlayer().getLocation().getDirection().setY(0).normalize().multiply(2);
			Location to = player.getPlayer().getLocation().add(vector);
			
			for(final PlayerCell playerCell : player.getCells()) {
				
				//### Moving players ###//
				
				Vector direction = Utils.getDirection(playerCell.getX(), playerCell.getY(), to.getX(), to.getZ()).multiply(1 / Math.log(playerCell.getMass()));
				playerCell.move(direction.getX(), direction.getZ());
				
				//### Eating tests ###//
				
				//** Static Cells **//
				for(StaticCell staticCell : AgarMC.get().getGame().getStaticCells()) {
					if(playerCell.getMass() > staticCell.getMass() && Math.sqrt(Math.pow(playerCell.getX() - staticCell.getX(), 2) + Math.pow(playerCell.getY() - staticCell.getY(), 2)) < playerCell.getRadius() - staticCell.getRadius() && !staticCell.isInvinsible()) {
						playerCell.increaseMass(staticCell.getMass());
						AgarMC.get().getGame().removeStaticCell(staticCell);
					}
				}
				
				//** Players Cells **//
				for(CPlayer opponent : AgarMC.get().getGame().getPlayers()) {
					if(!opponent.isPlaying() || opponent.equals(player)) continue;
					if(AgarMC.get().getGame().getGameType() == GameType.TEAMS && player.getColor().equals(opponent.getColor())) continue ;
					for(PlayerCell opponentCell : opponent.getCells()) {
						if(playerCell.getMass() > opponentCell.getMass() && Math.sqrt(Math.pow(playerCell.getX() - opponentCell.getX(), 2) + Math.pow(playerCell.getY() - opponentCell.getY(), 2)) < playerCell.getRadius() - opponentCell.getRadius()) {
							playerCell.increaseMass(opponentCell.getMass());
							opponent.removeCell(opponentCell);
						}
					}
				}
				
				//** Owner Cells (merge) **//
				for(PlayerCell other : player.getCells()) {
					if(other.equals(playerCell) || !playerCell.canMerge() || !other.canMerge()) continue;
					if(Math.sqrt(Math.pow(playerCell.getX() - other.getX(), 2) + Math.pow(playerCell.getY() - other.getY(), 2)) < playerCell.getRadius() + other.getRadius()) { // (interlocation)
						playerCell.increaseMass(other.getMass());
						player.removeCell(other);
						playerCell.setCanMerge(false);
						AgarMC.get().getServer().getScheduler().runTaskLater(AgarMC.get(), new Runnable() {
							@Override
							public void run() {
								playerCell.setCanMerge(true);
							}
						}, (long) ((playerCell.getMass() * 0.2) + 20) * 20L);
					}
				}
				
				//** Virus Cells **//
				for(VirusCell virus : AgarMC.get().getGame().getVirus()) {
					if(playerCell.getMass() < virus.getMass()) continue;
					if(playerCell.getMass() > virus.getMass() && Math.sqrt(Math.pow(playerCell.getX() - virus.getX(), 2) + Math.pow(playerCell.getY() - virus.getY(), 2)) < playerCell.getRadius() - virus.getRadius()) {
						AgarMC.get().getGame().removeVirus(virus);
						int n = Game.MAX_CELL - player.getCellsCount();
						if (n == 0)
						{
							playerCell.increaseMass(virus.getMass());
							continue ;
						}
						Location loc = new Location(AgarMC.get().getWorld(), virus.getX(), 128, virus.getY()); // Using bukkit's location class
						n++;
						for(int i = 0; i < 5 && i < n; i++) {
							loc.setYaw((float) (Math.random() * 360));
							loc.setPitch(0);
							PlayerCell cell = new PlayerCell(player, playerCell.getMass() / n, loc.getX(), loc.getZ());
							cell.setVelocity(loc.getDirection().normalize().multiply(2));
							player.addCell(cell);
						}
						
						player.removeCell(playerCell);
					}
				}
				
				//** Collisions **//
				for(PlayerCell other : player.getCells()) {
					if(other.equals(playerCell)) continue;
					if(Math.sqrt(Math.pow(playerCell.getX() - other.getX(), 2) + Math.pow(playerCell.getY() - other.getY(), 2)) < playerCell.getRadius() + other.getRadius()) { // (interlocation)
						if(playerCell.isDriving()) {
							other.addVelocity(Utils.getDirection(playerCell.getX(), playerCell.getY(), other.getX(), other.getY()).multiply(0.1));
						} else {
							playerCell.addVelocity(Utils.getDirection(other.getX(), other.getY(), playerCell.getX(), playerCell.getY()).multiply(0.1));
						}
					}
				}
			}
		}
	}

}
