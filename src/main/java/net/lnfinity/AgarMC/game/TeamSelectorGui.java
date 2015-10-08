package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.util.GameType;
import net.samagames.api.gui.AbstractGui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TeamSelectorGui extends AbstractGui
{

	@Override
	public void display(Player arg0)
	{
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
			this.setSlotData(item, i, "select");
		}
	}
	
	public static List<TeamColor> getColors(GameType type)
	{
		List<TeamColor> list = new ArrayList<TeamColor>();
		int i = 0;
		if (type != GameType.TEAMS)
		{
			list.add(new TeamColor(ChatColor.WHITE, i++, "Blanche"));
			list.add(new TeamColor(ChatColor.GRAY, i++, "Gris clair"));
			list.add(new TeamColor(ChatColor.DARK_GRAY, i++, "Gris foncé"));
			list.add(new TeamColor(ChatColor.BLACK, i++, "Noire"));
		}
		list.add(new TeamColor(ChatColor.DARK_RED, i++, "Rouge"));
		if (type != GameType.TEAMS)
			list.add(new TeamColor(ChatColor.YELLOW, i++, "Jaune"));
		list.add(new TeamColor(ChatColor.GREEN, i++, "Vert"));
		if (type != GameType.TEAMS)
			list.add(new TeamColor(ChatColor.DARK_GREEN, i++, "Vert foncé"));
		list.add(new TeamColor(ChatColor.AQUA, i++, "Bleu clair"));
		if (type != GameType.TEAMS)
		{
			list.add(new TeamColor(ChatColor.DARK_AQUA, i++, "Cyan"));
			list.add(new TeamColor(ChatColor.DARK_BLUE, i++, "Bleu foncé"));
			list.add(new TeamColor(ChatColor.LIGHT_PURPLE, i++, "Magenta"));
			list.add(new TeamColor(ChatColor.DARK_PURPLE, i++, "Violet"));
		}
		return list;
	}

	@Override
	public void onClick(Player p, ItemStack item, String action)
	{
		
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
			return (chat + name);
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
