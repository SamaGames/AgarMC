package net.lnfinity.AgarMC.util;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class Utils {
	
	private final static Random random = new Random();
	
	private Utils() {
	}

	public static Vector getDirection(double x1, double y1, double x2, double y2) {
		return new Vector(x2 - x1, 0, y2 - y1).normalize();
	}
	
	public static Vector getDirection(Location loc1, Location loc2) {
		return getDirection(loc1.getX(), loc1.getZ(), loc2.getX(), loc2.getZ());
	}
	
	public static ChatColor getRandomColor() {
		int color = random.nextInt(16);
		switch(color) {
		case 0 : return ChatColor.AQUA;
		case 1 : return ChatColor.BLACK;
		case 2 : return ChatColor.BLUE;
		case 3 : return ChatColor.DARK_AQUA;
		case 4 : return ChatColor.DARK_BLUE;
		case 5 : return ChatColor.DARK_GRAY;
		case 6 : return ChatColor.DARK_GREEN;
		case 7 : return ChatColor.DARK_PURPLE;
		case 8 : return ChatColor.DARK_RED;
		case 9 : return ChatColor.GOLD;
		case 10 : return ChatColor.GRAY;
		case 11 : return ChatColor.GREEN;
		case 12 : return ChatColor.LIGHT_PURPLE;
		case 13 : return ChatColor.RED;
		case 14 : return ChatColor.WHITE;
		case 15 : return ChatColor.YELLOW;
		}
		return ChatColor.WHITE;
	}
	
}
