package net.lnfinity.AgarMC.util;

import java.util.Random;

import net.lnfinity.AgarMC.AgarMC;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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
		GameType type = AgarMC.get().getGame().getGameType();
		if (type == GameType.TEAMS)
		{
			int color = random.nextInt(AgarTeams.values().length);
			return AgarTeams.values()[color].getColor();
		}
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
	
	public static ItemStack constructBook(ItemStack item, String title, String owner, String[] pages)
	{
		if (item.getItemMeta() instanceof BookMeta)
		{
			BookMeta meta = (BookMeta)item.getItemMeta();
			if (title != null)
				meta.setTitle(title);
			if (owner != null)
				meta.setAuthor(owner);
			if (pages != null)
				meta.addPage(pages);
			item.setItemMeta(meta);
		}
		return item;
	}
	
	public static String[] getRulesBookText()
	{
		String[] raw = new String[]{
				"\n   ]--------------[" +
				"\n         &6&lAgarMC&0" +
				"\n     par SamaGames" +
				"\n   ]--------------[" +
				"\n" +
				"\n" +
				"\n &11.&0 Comment jouer ?" +
				"\n &12.&0 Objectifs"
		};
		String[] colored = new String[raw.length];
		for (int i = 0; i < raw.length; i++)
			colored[i] = replaceColors(raw[i]);
		return colored;
	}
	
	public static String replaceColors(String message)
	{
		String s = message;
		for (ChatColor color : ChatColor.values()) {
			s = s.replaceAll("(?i)&" + color.getChar(), "" + color);
		}
		return s;
	}
	
	public static double randomLocation(double dimensions)
	{
		return (0.3D + Math.random() * (dimensions - 0.6D));
	}
}
