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
		int color = random.nextInt(14);
		switch(color) {
		case 0 : return ChatColor.AQUA;
		case 1 : return ChatColor.BLUE;
		case 2 : return ChatColor.DARK_AQUA;
		case 3 : return ChatColor.DARK_BLUE;
		case 4 : return ChatColor.DARK_GREEN;
		case 5 : return ChatColor.DARK_PURPLE;
		case 6 : return ChatColor.DARK_RED;
		case 7 : return ChatColor.GOLD;
		case 8 : return ChatColor.GRAY;
		case 9 : return ChatColor.GREEN;
		case 10 : return ChatColor.LIGHT_PURPLE;
		case 11 : return ChatColor.RED;
		case 12 : return ChatColor.WHITE;
		case 13 : return ChatColor.YELLOW;
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
				"\n         " + AgarMC.NAME_BICOLOR + "§0" +
				"\n     par §lSamaGames§0" +
				"\n   ]--------------[" +
				"\n" +
				"\n" +
				"\n §11.§0 Comment jouer ?" +
				"\n" +
				"\n §12.§0 Objectifs",
				
				"\n §lComment jouer ?§0\n" +
				"\n Vous incarnez une\n petite" +
				" cellule qui doit\n grandir" +
				" avec le temps\n\n Mangez d'autres\n" + 
				" cellules plus petites\n pour" +
				" augmenter votre\n taille !",
				
				"\n §lComment jouer ?§0\n" +
				"\n Mais attention à vos\n adversaires" +
				" qui\n peuvent vous manger\n\n Soyez" +
				" intelligents et\n restez gros !",
				
				"\n       §lObjectifs §0 " +
				"\n       §lMode §6§lFFA §0 \n\n" +
				" Chacun pour soi,\n devenez le" +
				" meilleur\n de tous !",
				
				"\n       §lObjectifs §0" +
				"\n      §lMode §6§lTeams §0 \n\n" +
				" Gagnez le plus de\n points" +
				" pour votre\n équipe (§c§lRouge§0," +
				" §2§lVert§0\n ou §1§lBleu§0) !",
				
				"\n\nJeu développé par :" +
				"\n\n - §l6infinity8§0" +
				"\n - §lRigner§0" +
				"\n - §lAmauryPi§0" +
				"\n\n\n\n\n      SamaGames" + 
				"\n Tout droits réservés."
		};
		return raw;
	}
	
	public static double randomLocation(double dimensions)
	{
		return (0.3D + Math.random() * (dimensions - 0.6D));
	}
}
