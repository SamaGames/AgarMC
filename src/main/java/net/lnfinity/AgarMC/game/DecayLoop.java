package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;

public class DecayLoop implements Runnable {

	@Override
	public void run() {
		for(CPlayer player : AgarMC.get().getGame().getPlayers()) {
			if(!player.isPlaying()) continue;
			
			for(PlayerCell playerCell : player.getCells()) {
				if(playerCell.getMass() < 500) continue;
				
				playerCell.increaseMass((int) - Math.floor(playerCell.getMass() * 0.002));
			}
		}
	}

}