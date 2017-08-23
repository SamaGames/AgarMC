package net.lnfinity.AgarMC.util;

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
