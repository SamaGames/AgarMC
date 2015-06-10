package net.lnfinity.AgarMC.cells;

import net.lnfinity.AgarMC.cells.core.GreenCell;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Slime;

public class StaticCell extends GreenCell {

	public StaticCell(int mass, double x, double y) {
		super(mass, x, y);
	}
	
	public StaticCell(double x, double y) {
		super(1, x, y);
	}
	
	public StaticCell(int mass, ArmorStand armorStand, Slime slime) {
		super(mass, armorStand, slime);
		
		recalculateSize();
	}

}
