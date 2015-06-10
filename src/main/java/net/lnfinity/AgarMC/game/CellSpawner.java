package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.StaticCell;

public class CellSpawner implements Runnable {

	private int iterations = 1;
	
	@Override
	public void run() {
		iterations++;
		
		int staticMass = AgarMC.get().getGame().getStaticMass();
		int playersMass = AgarMC.get().getGame().getPlayersMass();
		if(playersMass + staticMass < Game.MAX_MASS && staticMass < Game.MAX_STATIC) {
			for(int i = 0; i < AgarMC.get().getGame().getPlayers().size() * 2 + 1; i++) {
				AgarMC.get().getGame().addStaticCell(new StaticCell(Math.random() * Game.DIMENSIONS, Math.random() * Game.DIMENSIONS));
			}
		}
		
		if(iterations >= 30) {
			System.out.println("{\"players\":\"" + AgarMC.get().getGame().getPlayers().size() + "\",\"staticCells\":\"" + AgarMC.get().getGame().getStaticMass() + "\",\"playersCells\":\"" + AgarMC.get().getGame().getPlayersMass() + "\",\"total\":\"" + AgarMC.get().getGame().getTotalMass() + "\"}");
			iterations = 1;
		}
	}

}
