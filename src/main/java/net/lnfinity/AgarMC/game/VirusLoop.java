package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.Utils;

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
public class VirusLoop implements Runnable {
    /**
     * This method needs to be called very often
     */
    @Override
    public void run() {

        // ** Virus Cells **//
        for (VirusCell virus : AgarMC.get().getGame().getVirus()) {
            if (virus.isInvinsible()) continue;
            for (StaticCell cell : AgarMC.get().getGame().getStaticCells()) {
                if (cell.getMass() > 1 && virus.getMass() > cell.getMass() && Math.sqrt(Math.pow(virus.getX() - cell.getX(), 2) + Math.pow(virus.getY() - cell.getY(), 2)) < virus.getRadius() - cell.getRadius()) {
                    virus.increaseMass(cell.getMass());
                    AgarMC.get().getGame().removeStaticCell(cell);
                    if (virus.getMass() > 60) {
                        virus.setMass(27);
                        Vector vector = cell.getVelocity().normalize().multiply(3);
                        if (vector.getX() < 0.001F && vector.getZ() < 0.001F) {
                            vector = Utils.getDirection(cell.getX(), cell.getY(), virus.getX(), virus.getY()).multiply(3);
                        }
                        final VirusCell newVirus = new VirusCell(virus.getX(), virus.getY());
                        newVirus.setInvinsible(true);
                        AgarMC.get().getServer().getScheduler().runTaskLater(AgarMC.get(), () -> newVirus.setInvinsible(false), 10L);
                        newVirus.setVelocity(vector);
                        AgarMC.get().getGame().addVirus(newVirus);
                    }
                }
            }
        }
    }
}
