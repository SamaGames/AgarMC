package net.lnfinity.AgarMC.cells;

import net.lnfinity.AgarMC.cells.core.GreenCell;

import org.bukkit.entity.ArmorStand;

public class StaticCell extends GreenCell {

	public StaticCell(int mass, double x, double y) {
		super(mass, x, y, false);
	}
	
	public StaticCell(double x, double y) {
		super(Math.random() * 50 < 1 ? 2 : 1, x, y, false);
	}
	
	public StaticCell(int mass, ArmorStand armorStand) {
		super(mass, armorStand, false);
		
		recalculateSize();
	}

}
