package net.lnfinity.AgarMC.cells;

import net.lnfinity.AgarMC.cells.core.Cell;

public class StaticCell extends Cell {

	public StaticCell(int mass, double x, double y) {
		super(mass, x, y);
	}
	
	public StaticCell(double x, double y) {
		super(1, x, y);
	}

}
