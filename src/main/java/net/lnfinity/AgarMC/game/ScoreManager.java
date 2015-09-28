package net.lnfinity.AgarMC.game;

import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.util.AgarTeams;
import net.lnfinity.AgarMC.util.GameType;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreManager {

	public void update() {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective;
		
		objective = board.registerNewObjective("mass", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(AgarMC.NAME_BICOLOR);
		
		if (AgarMC.get().getGame().getGameType() == GameType.TEAMS){
			for (AgarTeams team : AgarTeams.values())
				objective.getScore(team.getDisplayName()).setScore(team.getTotalMass());
		}
		else
		{
			List<CPlayer> list = AgarMC.get().getGame().getPlayers();
			if (list.size() > 10)
			{
				CPlayer[] newlist = new CPlayer[list.size()];
				newlist = list.toArray(newlist);
				for (int j = 0; j < list.size() - 1; j++)
					for (int i = 0; i < list.size() - 1; i++)
						if (newlist[i].getTotalMass() < newlist[i + 1].getTotalMass())
						{
							CPlayer tmp = newlist[i];
							newlist[i] = newlist[i + 1];
							newlist[i + 1] = tmp;
						}
				for (int i = 0; i < 10; i++)
					objective.getScore(ChatColor.GOLD + newlist[i].getNick()).setScore(newlist[i].getTotalMass());
				return ;
			}
			for(CPlayer player : list) {
				if(!player.isPlaying()) continue;
				objective.getScore(ChatColor.GOLD + player.getNick()).setScore(player.getTotalMass());
			}
		}
		
		for(Player player : AgarMC.get().getServer().getOnlinePlayers()) {
			player.setScoreboard(board);
		}
	}
}
