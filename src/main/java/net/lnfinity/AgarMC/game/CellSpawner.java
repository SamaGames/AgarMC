package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.GameType;
import net.lnfinity.AgarMC.util.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CellSpawner implements Runnable {

	private int iterations = 1;
	
	@Override
	public void run() {
		if (AgarMC.get().getGame().getPlayers().size() == 0)
			return ;
		iterations++;
		
		int staticMass = AgarMC.get().getGame().getStaticMass();
		int playersMass = AgarMC.get().getGame().getPlayersMass();
		if(playersMass + staticMass < Game.MAX_MASS && staticMass < Game.MAX_STATIC) {
			int max = AgarMC.get().getGame().getPlayers().size() * 3 + 1;
			for(int i = 0; i < max; i++) {
				AgarMC.get().getGame().addStaticCell(new StaticCell(Utils.randomLocation(Game.ORIGIN.getX(), Game.DIMENSIONS), Utils.randomLocation(Game.ORIGIN.getZ(), Game.DIMENSIONS)));
			}
		}
		
		if(AgarMC.get().getGame().getVirus().size() < Game.MAX_VIRUS) {
			VirusCell virus = new VirusCell(Utils.randomLocation(Game.ORIGIN.getX(), Game.DIMENSIONS), Utils.randomLocation(Game.ORIGIN.getZ(), Game.DIMENSIONS));
			AgarMC.get().getGame().addVirus(virus);
		}
		
		if(iterations > 30 && AgarMC.get().isDebug()) {
			int sm = AgarMC.get().getGame().getStaticMass();
			int pm = AgarMC.get().getGame().getPlayersMass();
			System.out.println("{\"players\":\"" + AgarMC.get().getGame().getPlayers().size() + "\",\"staticCells\":\"" + sm + "\",\"playersCells\":\"" + pm + "\",\"total\":\"" + (sm + pm) + "\",\"virus\":\"" + AgarMC.get().getGame().getVirus().size() + "\"}");
			iterations = 1;
		}
		
		for (CPlayer player : AgarMC.get().getGame().getPlayers())
		{
			Inventory i = player.getPlayer().getInventory();
			ItemStack item = i.getItem(6);
			if (item != null && item.getType() == Material.WOOL)
			{
				short data = item.getDurability();
				if (AgarMC.get().getGame().getGameType() == GameType.TEAMS)
					switch (data)
					{
					case 3:
						data = 14;
						break ;
					case 5:
						data = 3;
						break ;
					case 14:
						data = 5;
						break ;
					}
				else
					data = (short)((data + 1) % 16);
				item.setDurability(data);
				i.setItem(6, item);
			}
		}
	}

}
