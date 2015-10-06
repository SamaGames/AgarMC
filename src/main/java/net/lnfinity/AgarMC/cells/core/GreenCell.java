package net.lnfinity.AgarMC.cells.core;

import net.lnfinity.AgarMC.AgarMC;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.inventory.ItemStack;

public abstract class GreenCell extends Cell {
	
	protected final ItemStack block;
	protected final Slime slime;
	
	private Material[] sizes = new Material[]{
			Material.LAPIS_BLOCK,
			Material.LAPIS_BLOCK,
			Material.EMERALD_BLOCK,
			Material.EMERALD_ORE
	};
	
	public GreenCell(int mass, double x, double y, boolean isplayer) {
		super(mass, x, y);
		
		/** Green Cube **/
		if (!isplayer)
		{
			block = new ItemStack(getMaterial());
			armorStand.setHelmet(block);
			slime = null;
		}
		else
		{
			block = null;
			slime = (Slime)AgarMC.get().getWorld().spawnEntity(new Location(AgarMC.get().getWorld(), x, 128, y), EntityType.SLIME);
			freezeEntity(slime);
			armorStand.setPassenger(slime);
		}
	}

	public GreenCell(int mass, ArmorStand armorStand, boolean isplayer) {
		super(mass, armorStand);
		
		if (!isplayer)
		{
			block = new ItemStack(getMaterial());
			armorStand.setHelmet(block);
			slime = null;
		}
		else
		{
			block = null;
			slime = (Slime)AgarMC.get().getWorld().spawnEntity(armorStand.getLocation(), EntityType.SLIME);
			armorStand.setPassenger(slime);
		}
	}
	
	@Override
	public void recalculateSize() {
		block.setType(getMaterial());
		armorStand.setHelmet(block);
	}
	
	@Override
	public void remove() {
		super.remove();
	}
	
	@Override
	public Material getMaterial(){
		int size = (int) (Math.floor(Math.cbrt(this.mass)));
		if (size >= sizes.length)
			size = sizes.length - 1;
		return sizes[size];
	}
}
