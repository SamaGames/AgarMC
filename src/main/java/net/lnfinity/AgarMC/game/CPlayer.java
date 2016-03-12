package net.lnfinity.AgarMC.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.util.Utils;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.PlayerInfoData;
import net.samagames.api.games.GamePlayer;
import net.samagames.tools.Reflection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CPlayer extends GamePlayer {

	private final Player player;
	private final List<PlayerCell> cells = new ArrayList<PlayerCell>();
	private boolean isPlaying = false;
	private ChatColor color;

	public CPlayer(Player player) {
		super(player);
		this.player = player;
		
		color = Utils.getRandomColor();
		player.setDisplayName(color + player.getName());
		
		updateColor();
		AgarMC.get().getGame().equipSpectatingPlayer(player);
	}

	public Player getPlayer() {
		return player;
	}
	
	public void reinit() {
		for (PlayerCell cell : cells) {
			cell.remove();
			cells.remove(cell);
		}
	}

	public void removeCell(PlayerCell cell) {
		cells.remove(cell);
		if (!cells.isEmpty()) {
			if(cell.isDriving()) {
				PlayerCell max = null;
				for (PlayerCell c : getCells()) {
					if((max == null || c.getMass() > max.getMass()) && !c.equals(cell)) {
						max = c;
					}
				}
				max.setDriving(true);
			}
		} else {
			onDeath();
		}
		cell.remove();
	}
	
	public void addCell(PlayerCell cell) {
		cells.add(cell);
	}

	public List<PlayerCell> getCells() {
		List<PlayerCell> list = new ArrayList<PlayerCell>();
		for (PlayerCell c : this.cells) {
			list.add(c);
		}
		return list;
	}

	public void onDeath() {
		player.sendMessage(ChatColor.RED + "Vous n'avez plus de cellules en vie, vous avez perdu !");
		setPlaying(false);
		for(PlayerCell cell : cells)
			AgarMC.get().getGame().playerCellToStaticCell(cell);
		cells.clear();
	}

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
		if(isPlaying) {
			AgarMC.get().getGame().equipPlayingPlayer(player);
		} else {
			AgarMC.get().getGame().equipSpectatingPlayer(player);
		}
	}
	
	public void remove() {
		// Add static cells
		
		setPlaying(false);
	}
	
	public PlayerCell getDriver() {
		for(PlayerCell cell : cells) {
			if(cell.isDriving()) return cell;
		}
		return null;
	}
	
	public void play() {
		PlayerCell base = AgarMC.get().getGame().safeSpawn(this);
		base.setDriving(true);
		setPlaying(true);
		massChanged();
		AgarMC.get().getScoreManager().update();
		updateColor();
	}
	
	public void split() {
		for(PlayerCell cell : getCells()) {
			if (cells.size() >= AgarMC.get().getGame().getMaxCells())
				break ;
			cell.split();
		}
	}
	
	public void ejectMass() {
		for(PlayerCell cell : getCells()) {
			if (cell.isDriving())
				cell.ejectMass();
		}
	}
	
	public String getNick() {
		return color + player.getName();
	}
	
	public int getTotalMass() {
		int total = 0;
		for(PlayerCell cell : getCells()) 
			total += cell.getMass();
		return total;
	}
	
	public void massChanged() {
		player.getPlayer().setLevel(getTotalMass());
	}
	
	public ChatColor getColor()
	{
		return color;
	}
	
	public void updateColor()
	{
		PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo();
		try {
			EntityPlayer entity = ((CraftPlayer)player).getHandle();
			Reflection.setValue(info, "a", EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
			PlayerInfoData data = info.new PlayerInfoData(entity.getProfile(), entity.ping, entity.playerInteractManager.getGameMode(), ChatSerializer.a(color + player.getName()));
			Reflection.setValue(info, "b", Arrays.asList(data));
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}
		for (Player p : Bukkit.getOnlinePlayers())
		{
			((CraftPlayer)p).getHandle().playerConnection.sendPacket(info);
			CPlayer cplayer = AgarMC.get().getGame().getCPlayer(p);
			if (cplayer == null) continue ;
			PacketPlayOutPlayerInfo info2 = new PacketPlayOutPlayerInfo();
			try {
				EntityPlayer entity = ((CraftPlayer)p).getHandle();
				Reflection.setValue(info2, "a", EnumPlayerInfoAction.UPDATE_DISPLAY_NAME);
				PlayerInfoData data = info2.new PlayerInfoData(entity.getProfile(), entity.ping, entity.playerInteractManager.getGameMode(), ChatSerializer.a(cplayer.getColor() + p.getName()));
				Reflection.setValue(info2, "b", Arrays.asList(data));
			} catch (NoSuchFieldException | IllegalAccessException e) {
				e.printStackTrace();
			}
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(info2);
		}
	}
	
	public int getCellsCount()
	{
		return cells.size();
	}
	
	public void setColor(ChatColor newcolor)
	{
		color = newcolor;
		updateColor();
		for (PlayerCell cell : cells)
			cell.updateColor();
	}
}
