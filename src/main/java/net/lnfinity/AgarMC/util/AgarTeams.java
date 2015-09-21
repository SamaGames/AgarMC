package net.lnfinity.AgarMC.util;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.game.CPlayer;

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
	
	public int getTotalMass()
	{
		int total = 0;
		for(CPlayer player : AgarMC.get().getGame().getPlayers()) {
			if(!player.isPlaying()) continue;
			if(player.getColor() != color) continue;
			total += player.getTotalMass();
		}
		return total;
	}
}
