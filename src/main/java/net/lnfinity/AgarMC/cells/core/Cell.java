package net.lnfinity.AgarMC.cells.core;

import net.lnfinity.AgarMC.AgarMC;
import net.minecraft.server.v1_9_R2.NBTTagCompound;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftArmorStand;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

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
public abstract class Cell {

    protected int mass;
    protected final ArmorStand armorStand;
    protected boolean invinsible = false;

    public Cell(int mass, double x, double y) {
        this.mass = mass;

        /** Armor Stand **/
        armorStand = AgarMC.get().getWorld().spawn(new Location(AgarMC.get().getWorld(), x, AgarMC.get().getGame().getOrigin().getY(), y), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setSmall(true);
    }

    public Cell(int mass, ArmorStand armorStand) {
        this.mass = mass;

        this.armorStand = armorStand;
    }

    public abstract void recalculateSize();

    public abstract Material getMaterial();

    public double getRadius() {
        return Math.cbrt(mass * 0.125);
    }

    public void remove() {
        armorStand.remove();
    }

    public void increaseMass(int mass) {
        this.mass += mass;
        recalculateSize();
    }

    public void setMass(int mass) {
        this.mass = mass;
        recalculateSize();
    }

    public void move(double x, double y) {
        Vector vector = new Vector(x, 0, y);
        addVelocity(vector);
    }

    public boolean isInvinsible() {
        return invinsible;
    }

    public void setInvinsible(boolean invinsible) {
        this.invinsible = invinsible;
    }

    public int getMass() {
        return mass;
    }

    public Vector getVelocity() {
        return armorStand.getVelocity();
    }

    public void setVelocity(Vector vector) {
        ((CraftArmorStand)armorStand).getHandle().motX = vector.getX();
        ((CraftArmorStand)armorStand).getHandle().motY = vector.getY();
        ((CraftArmorStand)armorStand).getHandle().motZ = vector.getZ();
    }

    public void addVelocity(Vector vector) {
        Vector vector2 = armorStand.getVelocity().add(vector);
        ((CraftArmorStand)armorStand).getHandle().motX = vector2.getX();
        ((CraftArmorStand)armorStand).getHandle().motY = vector2.getY();
        ((CraftArmorStand)armorStand).getHandle().motZ = vector2.getZ();
    }

    public double getX() {
        return armorStand.getLocation().getX();
    }

    public double getY() {
        return armorStand.getLocation().getZ();
    }

    protected void freezeEntity(Entity en) {
          net.minecraft.server.v1_9_R2.Entity nmsEn = ((CraftEntity) en).getHandle();
          NBTTagCompound compound = new NBTTagCompound();
          nmsEn.c(compound);
          compound.setByte("NoAI", (byte) 1);
          nmsEn.f(compound);
      }

    public Location getLocation()
    {
        return armorStand.getLocation();
    }
}
