package net.lnfinity.AgarMC;

import java.lang.reflect.Field;

import net.lnfinity.AgarMC.events.PlayerListener;
import net.lnfinity.AgarMC.events.WorldListener;
import net.lnfinity.AgarMC.game.CellSpawner;
import net.lnfinity.AgarMC.game.Game;
import net.lnfinity.AgarMC.game.GameLoop;
import net.lnfinity.AgarMC.game.ScoreManager;
import net.lnfinity.AgarMC.game.VirusLoop;
import net.lnfinity.AgarMC.util.AgarJoinHandler;
import net.lnfinity.AgarMC.util.GameType;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.api.network.IJoinManager;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
		
		//this.getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
		
		this.getServer().getScheduler().runTaskTimer(this, new GameLoop(), 5L, 3L);
		this.getServer().getScheduler().runTaskTimer(this, new VirusLoop(), 1L, 1L);
		this.getServer().getScheduler().runTaskTimer(this, new CellSpawner(), 20L, 20L);
		
		GameType type;
		try {
			type = GameType.getType(SamaGamesAPI.get().getGameManager().getGameProperties().getConfig("gameType", null).getAsString());
			Validate.notNull(type);
			game = new Game(type);	
		} catch (Exception e) {
			Bukkit.getLogger().severe("No GameType provided or invalid one ! /-- STOPPING SERVER --\\");
			Bukkit.shutdown();
			return ;
		}
		
		scoreManager = new ScoreManager();
		
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 7; j++)
				this.getWorld().loadChunk(i, j);
		game.initialize();
		
		SamaGamesAPI.get().getGameManager().registerGame(game);
		
		this.getServer().getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				scoreManager.update();
			}
		}, 0L, 10L);
		
		try {
			Field f = SamaGamesAPI.get().getClass().getDeclaredField("joinManager");
			f.setAccessible(true);
			IJoinManager joinManager = (IJoinManager)f.get(SamaGamesAPI.get());
			joinManager.registerHandler(new AgarJoinHandler(SamaGamesAPI.get().getGameManager()), 100);
			game.getBeginTimer().cancel();
			game.setStatus(Status.IN_GAME);
		} catch(Exception e) {
			e.printStackTrace();
			Bukkit.shutdown();
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
