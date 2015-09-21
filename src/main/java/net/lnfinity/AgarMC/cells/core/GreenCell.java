package net.lnfinity.AgarMC.cells.core;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public abstract class GreenCell extends Cell {
	
	protected final ItemStack block;
	private Material[] sizes = new Material[]{
			Material.SLIME_BLOCK,
			Material.SLIME_BLOCK,
			Material.EMERALD_BLOCK,
			Material.EMERALD_ORE,
			Material.GRASS,
			Material.CACTUS,
			Material.MELON_BLOCK
	};
	
	public GreenCell(int mass, double x, double y) {
		super(mass, x, y);
		
		/** Green Cube **/
		int size = (int) (Math.floor(Math.cbrt(this.mass)));
		if (size >= sizes.length)
			size = sizes.length - 1;
		block = new ItemStack(sizes[size]);
		armorStand.setHelmet(block);
	}

	public GreenCell(int mass, ArmorStand armorStand) {
		super(mass, armorStand);
		
		block = new ItemStack(getMaterial());
		armorStand.setHelmet(block);
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
