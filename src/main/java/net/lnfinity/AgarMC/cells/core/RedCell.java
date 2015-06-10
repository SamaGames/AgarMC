package net.lnfinity.AgarMC.cells.core;

import net.lnfinity.AgarMC.AgarMC;

import org.bukkit.Location;
import org.bukkit.entity.MagmaCube;

public class RedCell extends Cell {

	protected final MagmaCube magma;
	
	public RedCell(int mass, double x, double y) {
		super(mass, x, y);
		
		/** Magma Cube **/
		magma = AgarMC.get().getWorld().spawn(new Location(AgarMC.get().getWorld(), x, 128, y), MagmaCube.class);
		magma.setRemoveWhenFarAway(false);
		armorStand.setPassenger(magma);
		
		recalculateSize();
	}

	@Override
	public void recalculateSize() {
		magma.setSize((int) (Math.floor(Math.cbrt(this.mass))));
	}
	
	@Override
	public void remove() {
		magma.remove();
		
		super.remove();
	}
}
