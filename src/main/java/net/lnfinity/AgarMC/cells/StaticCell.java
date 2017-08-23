package net.lnfinity.AgarMC.cells;

import net.lnfinity.AgarMC.cells.core.GreenCell;

import org.bukkit.entity.ArmorStand;

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
