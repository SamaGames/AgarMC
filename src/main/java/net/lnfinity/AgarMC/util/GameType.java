package net.lnfinity.AgarMC.util;

public enum GameType
{
	TEAMS("Jeu en équipe"),
	//PARTY, ?
	//EXPERIMENTAL, ?
	FFA("Free for all");
	
	private String name;
	
	private GameType(String n)
	{
		name = n;
	}
	
	public String getDisplayName()
	{
		return name;
	}
	
	public static GameType getType(String name)
	{
		for (GameType type : values())
			if (type.toString().equals(name))
				return type;
		return null;
	}
}
