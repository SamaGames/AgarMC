package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.Utils;

import org.bukkit.util.Vector;

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
