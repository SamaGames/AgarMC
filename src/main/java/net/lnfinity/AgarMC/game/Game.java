package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.GameType;
import net.lnfinity.AgarMC.util.Utils;
import net.samagames.api.SamaGamesAPI;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Game extends net.samagames.api.games.Game<CPlayer> {

	private final List<StaticCell> staticCells = Collections.synchronizedList(new ArrayList<StaticCell>());
	private final List<VirusCell> virus = Collections.synchronizedList(new ArrayList<VirusCell>());
	//private final List<CPlayer> players = Collections.synchronizedList(new ArrayList<CPlayer>());
	
	public final static int DIMENSIONS = 100; // Side of the arena
	public final static int MAX_STATIC = DIMENSIONS * DIMENSIONS / 4; // 1 cell per 4 blocks
	public final static int MAX_MASS = MAX_STATIC * 8;
	public final static int MAX_VIRUS = DIMENSIONS * DIMENSIONS / 1000; // 1 virus per 1000 blocks
	public GameType gameType;
	
	public Game(GameType type) {
		super("agarmc", "AgarMC", "Cube cube cuuuuuuuuuuuuube", CPlayer.class);
		gameType = type;
	}
	
	public List<CPlayer> getPlayers() {
		List<CPlayer> list = new ArrayList<CPlayer>();
		list.addAll(this.getInGamePlayers().values());
		return list;
	}
	
	public synchronized List<StaticCell> getStaticCells() {
		List<StaticCell> list = new ArrayList<StaticCell>();
		synchronized (staticCells)
		{
			Iterator<StaticCell> i = staticCells.iterator();
			while (i.hasNext())
				list.add(i.next());
		}
		return list;
	}
	
	public List<VirusCell> getVirus() {
		List<VirusCell> list = new ArrayList<VirusCell>();
		list.addAll(virus);
		return list;
	}
	
	public void addStaticCell(StaticCell cell) {
		staticCells.add(cell);
	}
	
	public void addVirus(VirusCell virus) {
		this.virus.add(virus);
	}
	
	public void removeStaticCell(StaticCell cell) {
		cell.remove();
		staticCells.remove(cell);
	}
	
	public void removePlayer(CPlayer player) {
		player.remove();
	}
	
	public void removeVirus(VirusCell virus) {
		virus.remove();
		this.virus.remove(virus);
	}
	
	public void removePlayer(Player player) {
		removePlayer(player.getUniqueId());
	}
	
	public void removePlayer(UUID id) {
		CPlayer player = this.getPlayer(id);
		if (player != null)
		player.remove();
	}
	
	public CPlayer getCPlayer(Player player) {
		for(CPlayer p : getPlayers()) {
			if(p.getPlayer().equals(player)) {
				return p;
			}
		}
		return null;
	}
	
	public PlayerCell safeSpawn(CPlayer player) {
		PlayerCell cell = new PlayerCell(player, 100, Math.random() * 98 + 1, Math.random() * 98 + 1);
		player.addCell(cell);
		return cell;
	}
	
	public void initialize() {
		for (Entity e : AgarMC.get().getWorld().getEntities())
			if (!(e instanceof Player))
				e.remove();
		for(int i = 0; i < MAX_STATIC / 8; i++) {
			StaticCell cell = new StaticCell(Math.random() * DIMENSIONS, Math.random() * DIMENSIONS);
			staticCells.add(cell);
		}
		
		for(int i = 0; i < MAX_VIRUS / 8; i++) {
			VirusCell cell = new VirusCell(Math.random() * DIMENSIONS, Math.random() * DIMENSIONS);
			virus.add(cell);
		}
	}
	
	public void equipPlayingPlayer(final Player player) {
		player.setFlying(false);
		player.setAllowFlight(false);
		player.getInventory().clear();
		player.getInventory().setItem(0, constructItem(Material.MAGMA_CREAM, 1, (byte) 0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Se diviser", Arrays.asList(ChatColor.GRAY + "Divise toutes vos cellules en deux")));
		player.getInventory().setItem(1, constructItem(Material.SLIME_BALL, 1, (byte) 0, ChatColor.GREEN + "" + ChatColor.BOLD + "Ejecter de la Matière", Arrays.asList(ChatColor.GRAY + "Ejecte une cellule inerte dans votre direction")));
		player.getInventory().setItem(4, Utils.constructBook(constructItem(Material.WRITTEN_BOOK, 1, (byte) 0, ChatColor.LIGHT_PURPLE + "Règles", Arrays.asList(ChatColor.GRAY + "Visualiser les règles du jeu")), ChatColor.LIGHT_PURPLE + "Règles", "Infinity & Rigner", Utils.getRulesBookText()));
		player.getInventory().setItem(7, constructItem(Material.SNOW_BALL, 1, (byte) 0, ChatColor.AQUA + "Mode spectateur", Arrays.asList(ChatColor.GRAY + "Vous permet de visualiser la partie en cours")));
		player.getInventory().setItem(8, constructItem(Material.IRON_DOOR, 1, (byte) 0, ChatColor.RED + "Quitter", null));
	}

	public void equipSpectatingPlayer(final Player player) {
		player.setAllowFlight(true);
		player.setFlying(true);
		player.getInventory().clear();
		player.getInventory().setItem(0, constructItem(Material.NETHER_STAR, 1, (byte) 0, ChatColor.AQUA + "" + ChatColor.BOLD + "Jouer", null));
		player.getInventory().setItem(4, Utils.constructBook(constructItem(Material.WRITTEN_BOOK, 1, (byte) 0, ChatColor.LIGHT_PURPLE + "Règles", Arrays.asList(ChatColor.GRAY + "Visualiser les règles du jeu")), ChatColor.LIGHT_PURPLE + "Règles", "Infinity & Rigner", Utils.getRulesBookText()));
		player.getInventory().setItem(8, SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem());
	}

	public ItemStack constructItem(Material type, int amount, byte data, String display, List<String> lore) {
		ItemStack item = new ItemStack(type, amount, data);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(display);
		if (lore != null)
			meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public int getStaticMass() {
		int mass = 0;
		for(StaticCell cell : getStaticCells()) {
			mass += cell.getMass();
		}
		return mass;
	}
	
	public int getPlayersMass() {
		int mass = 0;
		for(CPlayer player : getPlayers()) {
			mass += player.getTotalMass();
		}
		return mass;
	}
	
	
	public int getTotalMass() {
		return getStaticMass() + getPlayersMass();
	}
	
	public int getVirusMass() {
		int mass = 0;
		for(VirusCell cell : getVirus()) {
			mass += cell.getMass();
		}
		return mass;
	}
	
	public void playerCellToStaticCell(PlayerCell playerCell) {
		StaticCell cell = playerCell.toStaticCell();
		addStaticCell(cell);
	}
	
	@Override
	public void handleLogin(Player p)
	{
		super.handleLogin(p);
		p.setGameMode(GameMode.ADVENTURE);
		p.setLevel(0);
		p.setExp(0);
		p.setFoodLevel(20);
		p.setSaturation(20);
		
		for(Player player : AgarMC.get().getServer().getOnlinePlayers()) {
			if(player.equals(p)) continue;
			player.hidePlayer(p);
			p.hidePlayer(player);
		}
		Location spec = new Location(AgarMC.get().getWorld(), DIMENSIONS / 2, 148, DIMENSIONS / 2);
		spec.setPitch(90);
		p.teleport(spec);
	}
	
	@Override
	public void handleLogout(Player p)
	{
		super.handleLogout(p);
		CPlayer player = getCPlayer(p);
		if (player == null)
			return ;
		for(PlayerCell cell : player.getCells()) {
			playerCellToStaticCell(cell);
		}
		if (this.gamePlayers.containsKey(p.getUniqueId()))
			this.gamePlayers.remove(p.getUniqueId());
	}
	
	@Override
	public Pair<Boolean, String> canJoinGame(UUID player, boolean reconnect)
	{
		if (this.getPlayers().size() >= SamaGamesAPI.get().getGameManager().getGameProperties().getMaxSlots())
			return new MutablePair<Boolean, String>(false, "Serveur plein");
		return new MutablePair<Boolean, String>(true, null);
	}
	
	public GameType getGameType()
	{
		return gameType;
	}
}
