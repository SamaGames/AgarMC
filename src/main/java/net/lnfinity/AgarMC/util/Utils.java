package net.lnfinity.AgarMC.util;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class Utils {
	private Utils() {
	}

	public static Vector getDirection(double x1, double y1, double x2, double y2) {
		return new Vector(x2 - x1, 0, y2 - y1).normalize();
	}
	
	public static Vector getDirection(Location loc1, Location loc2) {
		return getDirection(loc1.getX(), loc1.getZ(), loc2.getX(), loc2.getZ());
	}
	
}
