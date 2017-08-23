package net.lnfinity.AgarMC.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
public class GameCommand implements CommandExecutor
{
    public static final String VERSION = "V3";

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
    {
        arg0.sendMessage("AgarMC by Rigner & 6infinity8 ! Version : " + VERSION);
        return true;
    }
}
