package net.lnfinity.AgarMC.cells.core;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/*
 * This file is part of AgarMC.
 *
 * AgarMC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgarMC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgarMC.  If not, see <http://www.gnu.org/licenses/>.
 */
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
