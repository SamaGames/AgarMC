package net.lnfinity.AgarMC.util;

import org.bukkit.ChatColor;

public enum AgarTeams
{
	RED(ChatColor.RED, "Rouge"),
	BLUE(ChatColor.AQUA, "Bleu"),
	GREEN(ChatColor.GREEN, "Vert");
	
	private ChatColor color;
	private String name;
	
	private AgarTeams(ChatColor c, String n)
	{
		color = c;
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ChatColor getColor()
	{
		return color;
	}
	
	public String getDisplayName()
	{
		return (color + name);
	}

	public static AgarTeams getTeam(ChatColor c)
	{
		for (AgarTeams tmp : values())
			if (tmp.getColor() == c)
				return tmp;
		return null;
	}
}
