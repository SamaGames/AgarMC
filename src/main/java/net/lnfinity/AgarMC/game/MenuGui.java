package net.lnfinity.AgarMC.game;

import java.util.Arrays;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.util.GameType;
import net.samagames.api.SamaGamesAPI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
public class MenuGui
{
    public static final String INV_NAME = "Menu";

    private MenuGui() {
    }

    public static void display(Player player)
    {
        Inventory inv = Bukkit.createInventory(player, 9, INV_NAME);
        if (AgarMC.get().getGame().getGameType() != GameType.TEAMS)
            inv.setItem(0, AgarGame.constructItem(Material.WOOL, 1, (byte) 14, ChatColor.AQUA + "" + ChatColor.BOLD + "Choisir sa couleur", null));
        inv.setItem(4, AgarGame.constructItem(Material.SNOW_BALL, 1, (byte) 0, ChatColor.AQUA + "Mode spectateur", Arrays.asList(ChatColor.GRAY + "Vous permet de visualiser la partie en cours")));
        inv.setItem(8, SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem());
        player.openInventory(inv);
    }

    public static void onClick(Player p, ItemStack item)
    {
        CPlayer cplayer = AgarMC.get().getGame().getCPlayer(p);
        if (cplayer == null)
            return;

        if (cplayer.isPlaying() && item.getType() == Material.SNOW_BALL)
            cplayer.onDeath();

        if (item.getType() == SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem().getType()) {
            SamaGamesAPI.get().getGameManager().kickPlayer(p, ChatColor.RED + "Vous avez quitté la partie");
        } else if(item.getType() == Material.WOOL) {
            TeamSelectorGui.display(p);
        }
    }
}
