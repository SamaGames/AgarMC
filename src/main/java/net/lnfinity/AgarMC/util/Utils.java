package net.lnfinity.AgarMC.util;

import java.util.List;
import java.util.Random;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.game.TeamSelectorGui;
import net.lnfinity.AgarMC.game.TeamSelectorGui.TeamColor;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.util.Vector;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class Utils {
	
	private final static Random random = new Random();
	
	private Utils() {
	}

	public static Vector getDirection(double x1, double y1, double x2, double y2) {
		Vector vec = new Vector(x2 - x1, 0, y2 - y1);
		if (vec.length() < 0.001)
			return vec;
		return vec.normalize();
	}
	
	public static Vector getDirection(Location loc1, Location loc2) {
		return getDirection(loc1.getX(), loc1.getZ(), loc2.getX(), loc2.getZ());
	}
	
	public static ChatColor getRandomColor() {
		GameType type = AgarMC.get().getGame().getGameType();
		List<TeamColor> list = TeamSelectorGui.getColors(type);
		return list.get(random.nextInt(list.size())).getChatColor();
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
				
				"\n       §lObjectifs §0" +
				"\n    §lMode §6§lHardcore §0 \n\n" +
				" Vous voulez un peu\n de challenge ? Alors\n retrouvez" +
				" le FFA\n avec le mode\n   §lHardcore§0 !",
				
				"\n\nJeu développé par :" +
				"\n\n - §l6infinity8§0" +
				"\n - §lRigner§0" +
				"\n - §lAmauryPi§0" +
				"\n\n\n\n\n      SamaGames" + 
				"\n Tout droits réservés."
		};
		return raw;
	}
	
	public static double randomLocation(double origin, double dimensions)
	{
		return origin + (0.3D + Math.random() * (dimensions - 0.6D));
	}

	public static Location getLocation(JsonElement object)
    {
        JsonObject json = object.getAsJsonObject();
        World world = AgarMC.get().getWorld();
        double x = json.get("x").getAsDouble() + 0.5;
        double y = json.get("y").getAsDouble();
        double z = json.get("z").getAsDouble() + 0.5;
        try
        {
            float yaw = (float)json.get("yaw").getAsDouble();
            float pitch = (float)json.get("pitch").getAsDouble();
            return new Location(world, x, y, z, yaw, pitch);
        }
        catch (UnsupportedOperationException | NullPointerException ex)
        {
            return new Location(world, x, y, z);
        }
    }
}
