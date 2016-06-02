package net.lnfinity.AgarMC.cells.core;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class RedCell extends Cell {

    protected final ItemStack block;
    private Material[] sizes = new Material[]{
            Material.REDSTONE_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.RED_SANDSTONE,
            Material.NETHERRACK
    };

    public RedCell(int mass, double x, double y) {
        super(mass, x, y);

        /** Red Cube **/
        block = new ItemStack(getMaterial());
        armorStand.setHelmet(block);

        recalculateSize();
    }

    @Override
    public void recalculateSize() {
        block.setType(getMaterial());
        armorStand.setHelmet(block);
    }

    @Override
    public Material getMaterial(){
        int size = (int) (Math.floor(Math.cbrt(this.mass)));
        if (size >= sizes.length)
            size = sizes.length - 1;
        return sizes[size];
    }
}
