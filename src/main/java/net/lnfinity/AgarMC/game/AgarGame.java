package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.GameType;
import net.lnfinity.AgarMC.util.Utils;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Game;
import net.samagames.api.games.IGameProperties;
import net.samagames.api.games.Status;
import net.samagames.tools.Titles;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
public class AgarGame extends Game<CPlayer> {

    private final List<StaticCell> staticCells = Collections.synchronizedList(new ArrayList<StaticCell>());
    private final List<VirusCell> virus = Collections.synchronizedList(new ArrayList<VirusCell>());

    private Location origin;
    private int dimensions; // Side of the arena
    private int maxstatic; // 1 cell per 24 blocks
    private int maxvirus; // 1 virus per 1000 blocks
    private int maxcells; // 16 cells per player

    private GameType gameType;

    public AgarGame(GameType type) {
        super("agarmc", AgarMC.NAME, type.getDisplayName(), CPlayer.class);
        gameType = type;
        try
        {
            IGameProperties config = SamaGamesAPI.get().getGameManager().getGameProperties();
            origin = Utils.getLocation(config.getOption("origin", null));
            dimensions = config.getOption("dimensions", null).getAsInt();
            maxstatic = dimensions * dimensions / 24;
            maxvirus = dimensions * dimensions / 1000;
            maxcells = 16;
            Bukkit.getLogger().info("Arena : Origin = " + origin.toString() + ", Dimensions = " + dimensions);
        }
        catch(Exception e)
        {
            Bukkit.getLogger().severe("Error in game.json ! Stopping server !");
            e.printStackTrace();
            Bukkit.shutdown();
        }
    }

    public List<CPlayer> getPlayers() {
        List<CPlayer> list = new ArrayList<>();
        list.addAll(this.getInGamePlayers().values());
        return list;
    }

    public synchronized List<StaticCell> getStaticCells() {
        List<StaticCell> list = new ArrayList<>();
        synchronized (this.staticCells)
        {
            list.addAll(this.staticCells);
        }
        return list;
    }

    public List<VirusCell> getVirus() {
        List<VirusCell> list = new ArrayList<>();
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
        addStaticCell(new StaticCell(Utils.randomLocation(origin.getX(), dimensions), Utils.randomLocation(origin.getZ(), dimensions)));
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
        for (CPlayer p : getPlayers()) {
            if (p.getPlayer().equals(player)) {
                return p;
            }
        }
        return null;
    }

    public PlayerCell safeSpawn(CPlayer player) {
        PlayerCell cell = new PlayerCell(player, 10, Math.random() * (dimensions - 2) + 1 + origin.getX(), Math.random() * (dimensions - 2) + 1 + origin.getZ());
        player.addCell(cell);
        return cell;
    }

    public void initialize() {
        for (Entity e : AgarMC.get().getWorld().getEntities())
            if (!(e instanceof Player))
                e.remove();
        for (int i = 0; i < maxstatic; i++) {
            StaticCell cell = new StaticCell(Utils.randomLocation(origin.getX(), dimensions), Utils.randomLocation(origin.getZ(), dimensions));
            staticCells.add(cell);
        }

        for (int i = 0; i < maxvirus; i++) {
            VirusCell cell = new VirusCell(Utils.randomLocation(origin.getX(), dimensions), Utils.randomLocation(origin.getZ(), dimensions));
            virus.add(cell);
        }
    }

    public void equipPlayingPlayer(final Player player) {
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getInventory().clear();
        player.getInventory().setItem(0, constructItem(Material.MAGMA_CREAM, 1, (byte) 0, ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Se diviser", Arrays.asList(ChatColor.GRAY + "Divise toutes vos cellules en deux")));
        player.getInventory().setItem(1, constructItem(Material.SLIME_BALL, 1, (byte) 0, ChatColor.GREEN + "" + ChatColor.BOLD + "Ejecter de la Matière", Arrays.asList(ChatColor.GRAY + "Ejecte une cellule inerte dans votre direction")));
        player.getInventory().setItem(7, Utils.constructBook(constructItem(Material.WRITTEN_BOOK, 1, (byte) 0, ChatColor.LIGHT_PURPLE + "Règles", Arrays.asList(ChatColor.GRAY + "Visualiser les règles du jeu")), ChatColor.LIGHT_PURPLE + "Règles", "Infinity & Rigner", Utils.getRulesBookText()));
        player.getInventory().setItem(8, constructItem(Material.DIODE, 1, (byte) 0, ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Menu", Arrays.asList(ChatColor.GRAY + "Menu du jeu")));
    }

    public void equipSpectatingPlayer(final Player player) {
        player.setAllowFlight(true);
        player.setFlying(true);
        player.getInventory().clear();
        player.getInventory().setItem(0, constructItem(Material.NETHER_STAR, 1, (byte) 0, ChatColor.AQUA + "" + ChatColor.BOLD + "Jouer", null));
        player.getInventory().setItem(4, Utils.constructBook(constructItem(Material.WRITTEN_BOOK, 1, (byte) 0, ChatColor.LIGHT_PURPLE + "Règles", Arrays.asList(ChatColor.GRAY + "Visualiser les règles du jeu")), ChatColor.LIGHT_PURPLE + "Règles", "Infinity & Rigner", Utils.getRulesBookText()));
        player.getInventory().setItem(1, constructItem(Material.WOOL, 1, (byte) 14, AgarMC.get().getGame().getGameType() == GameType.TEAMS ? ChatColor.AQUA + "" + ChatColor.BOLD + "Choisir son équipe" : ChatColor.AQUA + "" + ChatColor.BOLD + "Choisir sa couleur", null));
        player.getInventory().setItem(8, SamaGamesAPI.get().getGameManager().getCoherenceMachine().getLeaveItem());
    }

    public static ItemStack constructItem(Material type, int amount, byte data, String display, List<String> lore) {
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
        for (StaticCell cell : getStaticCells()) {
            mass += cell.getMass();
        }
        return mass;
    }

    public int getPlayersMass() {
        int mass = 0;
        for (CPlayer player : getPlayers()) {
            mass += player.getTotalMass();
        }
        return mass;
    }

    public int getTotalMass() {
        return getStaticMass() + getPlayersMass();
    }

    public int getVirusMass() {
        int mass = 0;
        for (VirusCell cell : getVirus()) {
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

        Location spec = new Location(AgarMC.get().getWorld(), origin.getX() + dimensions / 2, origin.getY() + 20, origin.getZ() + dimensions / 2);
        spec.setPitch(90);
        p.teleport(spec);
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
        if (gamePlayers.size() >= SamaGamesAPI.get().getGameManager().getGameProperties().getMaxSlots())
            this.status = Status.IN_GAME;
        Titles.sendTabTitle(p, ChatColor.GREEN + "Bienvenue sur " + ChatColor.AQUA + "SamaGames" + ChatColor.GREEN + " !\n",
                ChatColor.AQUA + "\nTeamSpeak : " + ChatColor.GREEN + "ts.samagames.net");
    }

    @Override
    public void handleLogout(Player p)
    {
        CPlayer player = this.gamePlayers.get(p.getUniqueId());
        super.handleLogout(p);
        if (player != null)
            player.onDeath();
        if (this.gamePlayers.containsKey(p.getUniqueId()))
            this.gamePlayers.remove(p.getUniqueId());
        if (gamePlayers.size() < SamaGamesAPI.get().getGameManager().getGameProperties().getMaxSlots())
            this.status = Status.WAITING_FOR_PLAYERS;
    }

    @Override
    public Pair<Boolean, String> canJoinGame(UUID player, boolean reconnect)
    {
        if (this.getPlayers().size() >= SamaGamesAPI.get().getGameManager().getGameProperties().getMaxSlots())
            return new MutablePair<>(false, "Serveur plein");
        return new MutablePair<>(true, null);
    }

    public GameType getGameType()
    {
        return gameType;
    }

    @Override
    public void startGame()
    {
        this.getClass();//JTE BRAIN SONAR
    }

    public int getMaxCells()
    {
        return maxcells;
    }

    public Location getOrigin()
    {
        return origin.clone();
    }

    public int getMaxVirus()
    {
        return maxvirus;
    }

    public int getDimensions()
    {
        return dimensions;
    }
}
