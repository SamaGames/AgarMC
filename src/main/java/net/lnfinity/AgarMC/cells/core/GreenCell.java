package net.lnfinity.AgarMC.cells.core;

import net.lnfinity.AgarMC.AgarMC;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
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
            AgarMC plugin = AgarMC.get();
            slime = (Slime)plugin.getWorld().spawnEntity(new Location(plugin.getWorld(), x, plugin.getGame().getOrigin().getY(), y), EntityType.SLIME);
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
    public Material getMaterial(){
        int size = (int) (Math.floor(Math.cbrt(this.mass)));
        if (size >= sizes.length)
            size = sizes.length - 1;
        return sizes[size];
    }
}
