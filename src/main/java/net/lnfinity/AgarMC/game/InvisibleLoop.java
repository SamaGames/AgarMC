package net.lnfinity.AgarMC.game;

import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

/*
 * This file is part of AgarMC.
 *
 * AgarMC is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AgarMC is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AgarMC.  If not, see <http://www.gnu.org/licenses/>.
 */
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
