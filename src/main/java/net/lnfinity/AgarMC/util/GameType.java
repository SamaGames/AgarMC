package net.lnfinity.AgarMC.util;

public enum GameType
{
	TEAMS,
	//PARTY, ?
	//EXPERIMENTAL, ?
	FFA;
	
	public static GameType getType(String name)
	{
		for (GameType type : values())
			if (type.toString().equals(name))
				return type;
		return null;
	}
}
