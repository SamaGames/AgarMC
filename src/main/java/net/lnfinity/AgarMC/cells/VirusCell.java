package net.lnfinity.AgarMC.cells;

import net.lnfinity.AgarMC.cells.core.RedCell;

public class VirusCell extends RedCell {

    public VirusCell(double x, double y) {
        super(27, x, y);
    }

    public VirusCell(int mass, double x, double y) {
        super(mass, x, y);
    }

    public void onEat(PlayerCell playerCell) {
        playerCell.getClass();//JTE BRAIN SONAR
    }
}
