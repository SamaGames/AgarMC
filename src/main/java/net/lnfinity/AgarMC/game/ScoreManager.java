package net.lnfinity.AgarMC.game;

import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.util.GameType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

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
public class ScoreManager {

    public void update() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective;

        objective = board.registerNewObjective("mass", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(AgarMC.NAME_BICOLOR);

        if (AgarMC.get().getGame().getGameType() == GameType.TEAMS){
            for (TeamSelectorGui.TeamColor team : TeamSelectorGui.getColors(GameType.TEAMS))
                objective.getScore(team.getDisplayName()).setScore(team.getTotalMass());
        }
        else
        {
            List<CPlayer> list = AgarMC.get().getGame().getPlayers();
            if (list.size() > 10)
            {
                CPlayer[] newlist = new CPlayer[10];
                for (int i = 0; i < list.size(); i++)
                {
                    if (i < 10)
                        newlist[i] = list.get(i);
                    else
                    {
                        int smallest = 0;
                        for (int j = 1; j < 10; j++)
                        {
                            if (newlist[j].getTotalMass() < newlist[smallest].getTotalMass())
                                smallest = j;
                        }
                        if (newlist[smallest].getTotalMass() < list.get(i).getTotalMass())
                            newlist[smallest] = list.get(i);
                    }
                }
                for (int i = 0; i < 10; i++) {
                    int mass = newlist[i].getTotalMass();
                    objective.getScore(ChatColor.GOLD + newlist[i].getNick()).setScore(mass);
                }
                for(Player player : AgarMC.get().getServer().getOnlinePlayers()) {
                    player.setScoreboard(board);
                }
                return ;
            }
            for(CPlayer player : list) {
                if(!player.isPlaying()) continue;
                int mass = player.getTotalMass();
                objective.getScore(ChatColor.GOLD + player.getNick()).setScore(mass);
            }
        }

        for(Player player : AgarMC.get().getServer().getOnlinePlayers()) {
            player.setScoreboard(board);
        }
    }
}
