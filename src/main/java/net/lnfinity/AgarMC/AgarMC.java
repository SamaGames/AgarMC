package net.lnfinity.AgarMC;

import net.lnfinity.AgarMC.events.GameCommand;
import net.lnfinity.AgarMC.events.PlayerListener;
import net.lnfinity.AgarMC.events.WorldListener;
import net.lnfinity.AgarMC.game.AgarGame;
import net.lnfinity.AgarMC.game.CellSpawner;
import net.lnfinity.AgarMC.game.DecayLoop;
import net.lnfinity.AgarMC.game.GameLoop;
import net.lnfinity.AgarMC.game.InvisibleLoop;
import net.lnfinity.AgarMC.game.ScoreManager;
import net.lnfinity.AgarMC.game.VirusLoop;
import net.lnfinity.AgarMC.util.GameType;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class AgarMC extends JavaPlugin {
	
	public final static String NAME = "AgarMC";
	public final static String NAME_BICOLOR = ChatColor.GREEN + "" + ChatColor.BOLD + "Agar" + ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "MC";
	
	private static AgarMC instance;
	private AgarGame game;
	private ScoreManager scoreManager;

	public AgarMC() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new WorldListener(), this);
		
		this.getServer().getScheduler().runTaskTimer(this, new DecayLoop(), 20L, 20L);
		this.getServer().getScheduler().runTaskTimer(this, new GameLoop(), 5L, 3L);
		this.getServer().getScheduler().runTaskTimer(this, new VirusLoop(), 1L, 1L);
		this.getServer().getScheduler().runTaskTimer(this, new CellSpawner(), 20L, 20L);
		this.getServer().getScheduler().runTaskTimer(this, new InvisibleLoop(), 1L, 1L);
		
		GameType type;
		try {
			type = GameType.getType(SamaGamesAPI.get().getGameManager().getGameProperties().getOption("gameType", null).getAsString());
			Validate.notNull(type);
		} catch (IllegalArgumentException e) {
			Bukkit.getLogger().severe("[AgarMC] Invalid GameType in game.json ! /-- STOPPING SERVER --\\");
			Bukkit.getLogger().severe("[AgarMC] Possible values :");
			for (GameType t : GameType.values())
				Bukkit.getLogger().severe("[AgarMC]    - " + t);
			Bukkit.shutdown();
			return ;
		} catch (NullPointerException e) {
			Bukkit.getLogger().severe("[AgarMC] No GameType in game.json ! /-- STOPPING SERVER --\\");
			Bukkit.getLogger().severe("[AgarMC] Possible values :");
			for (GameType t : GameType.values())
				Bukkit.getLogger().severe("[AgarMC]    - " + t);
			Bukkit.shutdown();
			return ;
		}
		
		game = new AgarGame(type);
		scoreManager = new ScoreManager();
		
		for (int i = 0; i < 7; i++)
			for (int j = 0; j < 7; j++)
				this.getWorld().loadChunk(i, j);
		game.initialize();
		
		SamaGamesAPI.get().getGameManager().registerGame(game);
		SamaGamesAPI.get().getResourcePacksManager().forcePack("https://samagames.net/packs/AgarMC.zip", "4d06e751a6bcdaf1bb7e0ff35d22708f", null);
		
		this.getServer().getScheduler().runTaskTimer(this, scoreManager::update, 0L, 10L);
		
		game.setStatus(Status.WAITING_FOR_PLAYERS);
		game.getBeginTimer().cancel();
		
		Bukkit.getPluginCommand("game").setExecutor(new GameCommand());
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
	
	public AgarGame getGame() {
		return game;
	}
	
	public ScoreManager getScoreManager()
	{
		return scoreManager;
	}
}