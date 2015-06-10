package net.lnfinity.AgarMC.cells.core;

import net.lnfinity.AgarMC.AgarMC;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Slime;

public abstract class GreenCell extends Cell {
	
	protected final Slime slime;
	
	public GreenCell(int mass, double x, double y) {
		super(mass, x, y);
		
		/** Slime **/
		slime = AgarMC.get().getWorld().spawn(new Location(AgarMC.get().getWorld(), x, 128, y), Slime.class);
		slime.setRemoveWhenFarAway(false);
		armorStand.setPassenger(slime);
		
		recalculateSize();
	}

	public GreenCell(int mass, ArmorStand armorStand, Slime slime) {
		super(mass, armorStand);
		
		this.slime = slime;
	}
	
	@Override
	public void recalculateSize() {
		slime.setSize((int) (Math.floor(Math.cbrt(this.mass))));
	}
	
	@Override
	public void remove() {
		slime.remove();
		
		super.remove();
	}
}
