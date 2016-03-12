package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.util.GameType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamSelectorGui
{
	public static final String INV_NAME = "Equipes";
	
	private TeamSelectorGui() {
	}
	
	public static void display(Player player)
	{
		Inventory inventory = Bukkit.createInventory(player, 18, INV_NAME);
		GameType type = AgarMC.get().getGame().getGameType();
		List<TeamColor> colors = getColors(type);
		for (int i = 0; i < colors.size(); i++)
		{
			TeamColor color = colors.get(i);
			ItemStack item = new ItemStack(Material.WOOL);
			item.setDurability(color.getData());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(color.getChatColor() + "" + ChatColor.BOLD + "Equipe " + color.getName());
			item.setItemMeta(meta);
			inventory.setItem(i, item);
		}
		ItemStack door = new ItemStack(Material.WOOD_DOOR);
		ItemMeta meta = door.getItemMeta();
		meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "Retour");
		door.setItemMeta(meta);
		inventory.setItem(17, door);
		player.openInventory(inventory);
	}
	
	public static List<TeamColor> getColors(GameType type)
	{
		List<TeamColor> list = new ArrayList<TeamColor>();
		if (type != GameType.TEAMS)
		{
			list.add(new TeamColor(ChatColor.WHITE, 0, "Blanche"));
			list.add(new TeamColor(ChatColor.GRAY, 8, "Gris clair"));
			list.add(new TeamColor(ChatColor.DARK_GRAY, 7, "Gris foncé"));
			list.add(new TeamColor(ChatColor.YELLOW, 4, "Jaune"));
		}
		list.add(new TeamColor(ChatColor.DARK_RED, 14, "Rouge"));
		if (type != GameType.TEAMS)
		{
			list.add(new TeamColor(ChatColor.LIGHT_PURPLE, 2, "Magenta"));
			list.add(new TeamColor(ChatColor.DARK_PURPLE, 10, "Violet"));
			list.add(new TeamColor(ChatColor.DARK_BLUE, 11, "Bleu foncé"));
			list.add(new TeamColor(ChatColor.DARK_AQUA, 9, "Cyan"));
			list.add(new TeamColor(ChatColor.AQUA, 3, "Bleu clair"));
		}	
		list.add(new TeamColor(ChatColor.GREEN, 5, "Vert"));
		if (type != GameType.TEAMS)
			list.add(new TeamColor(ChatColor.DARK_GREEN, 13, "Vert foncé"));
		
		return list;
	}

	public static void onClick(Player p, ItemStack item)
	{
		if (item.getType() == Material.WOOD_DOOR)
			p.closeInventory();
		if (item.getType() != Material.WOOL)
			return ;
		for (TeamColor color : getColors(AgarMC.get().getGame().getGameType()))
			if (color.getData() == item.getDurability())
			{
				CPlayer cplayer = AgarMC.get().getGame().getCPlayer(p);
				if (cplayer != null)
				{
					cplayer.setColor(color.getChatColor());
					cplayer.updateColor();
					p.sendMessage(ChatColor.YELLOW + "Vous êtes maintenant" + (AgarMC.get().getGame().getGameType() == GameType.TEAMS ? " dans l'équipe" : "") + " : " + color.getDisplayName());
					return ;
				}
			}
	}
	
	public static class TeamColor
	{
		private ChatColor chat;
		private byte data;
		private String name;
		
		public TeamColor(ChatColor c, int d, String s)
		{
			chat = c;
			data = (byte)d;
			name = s;
		}
		
		public ChatColor getChatColor()
		{
			return chat;
		}
		
		public byte getData()
		{
			return data;
		}
		
		public String getName()
		{
			return name;
		}

		public static TeamColor getTeam(ChatColor color)
		{
			for (TeamColor tcolor : TeamSelectorGui.getColors(AgarMC.get().getGame().getGameType()))
				if (tcolor.getChatColor() == color)
					return tcolor;
			return null;
		}

		public String getDisplayName()
		{			
			return chat + name;
		}

		public int getTotalMass()
		{
			int total = 0;
			for (CPlayer player : AgarMC.get().getGame().getInGamePlayers().values())
				if (player.isPlaying() && player.getColor() == chat)
					total += player.getTotalMass();
			return total;
		}
	}
}
