package net.lnfinity.AgarMC;

import net.lnfinity.AgarMC.events.ConnectionListener;
import net.lnfinity.AgarMC.events.PlayerListener;
import net.lnfinity.AgarMC.events.WorldListener;
import net.lnfinity.AgarMC.game.CellSpawner;
import net.lnfinity.AgarMC.game.DecayLoop;
import net.lnfinity.AgarMC.game.Game;
import net.lnfinity.AgarMC.game.GameLoop;
import net.lnfinity.AgarMC.game.ScoreManager;
import net.lnfinity.AgarMC.game.VirusLoop;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class AgarMC extends JavaPlugin {
	
	private static AgarMC instance;
	private Game game;
	private ScoreManager scoreManager;
	
	public final static String NAME = "CubeWars";
	public final static String NAME_BICOLOR = ChatColor.GREEN + "" + ChatColor.BOLD + "Cube" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Wars";

	@Override
	public void onEnable() {
		instance = this;
		
		this.getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
		
		this.getServer().getScheduler().runTaskTimer(this, new GameLoop(), 5L, 3L);
		this.getServer().getScheduler().runTaskTimer(this, new VirusLoop(), 1L, 1L);
		this.getServer().getScheduler().runTaskTimer(this, new DecayLoop(), 3 * 20L, 3 * 20L);
		this.getServer().getScheduler().runTaskTimer(this, new CellSpawner(), 20L, 20L);
		
		game = new Game();
		
		scoreManager = new ScoreManager();
		
		game.initialize();

		this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				scoreManager.update();
			}	
		}, 0L, 10L);
		
		for(Player player : this.getServer().getOnlinePlayers()) {
			new ConnectionListener().onPlayerJoin(new PlayerJoinEvent(player, null));
		}
	}
	
	@Override
	public void onDisable() {
		for(Entity entity : this.getServer().getWorlds().get(0).getEntities()) {
			if(!(entity instanceof Player))
				entity.remove();
		}
	}
	
	public static AgarMC get() {
		return instance;
	}
	
	public World getWorld() {
		return this.getServer().getWorlds().get(0);
	}
	
	public Game getGame() {
		return game;
	}
}
