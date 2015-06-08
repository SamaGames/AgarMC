package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;

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
		objective.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Agar" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MC");
		
		for(CPlayer player : AgarMC.get().getGame().getPlayers()) {
			if(!player.isPlaying()) continue;
			objective.getScore(ChatColor.GOLD + player.getNick()).setScore(player.getTotalMass());
		}
		
		for(Player player : AgarMC.get().getServer().getOnlinePlayers()) {
			player.setScoreboard(board);
		}
	}
}
