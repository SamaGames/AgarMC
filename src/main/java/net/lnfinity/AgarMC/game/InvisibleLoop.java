package net.lnfinity.AgarMC.game;

import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class InvisibleLoop implements Runnable
{
	/**
	 * To be used for hiding players but not in tab, and looped to avoid entity
	 * respawn on view when coming back from away
	 */
	
	@Override
	public void run()
	{
		for (Player p1 : Bukkit.getOnlinePlayers())
			for (Player p2 : Bukkit.getOnlinePlayers())
				if (!p1.equals(p2))
					((CraftPlayer)p1).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(p2.getEntityId()));
	}

}
