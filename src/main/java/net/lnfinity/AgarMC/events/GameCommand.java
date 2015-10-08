package net.lnfinity.AgarMC.events;

import java.util.List;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.PlayerCell;
import net.lnfinity.AgarMC.cells.StaticCell;
import net.lnfinity.AgarMC.game.CPlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameCommand implements CommandExecutor
{
	public static final String VERSION = "Pre-V3 - DoingTeamsSelector";
	
	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
	{
		if (arg3.length == 0)
		{
			arg0.sendMessage("AgarMC by Rigner & 6infinity8 ! Version : ");
			return true;
		}
		if (arg3.length == 1 && arg3[0].equalsIgnoreCase("cells") && arg0.hasPermission("agarmc.debug.cells"))
		{
			List<StaticCell> list =  AgarMC.get().getGame().getStaticCells();
			arg0.sendMessage(list.size() + " cellules");
			for (StaticCell cell : list)
				arg0.sendMessage(cell.getLocation().toString());
		}
		if (arg3.length != 3 || !arg3[1].equalsIgnoreCase("boost") || !arg0.hasPermission("agarmc.debug.boost"))
			return true;
		List<CPlayer> players = AgarMC.get().getGame().getPlayers();
		for (CPlayer c : players)
		{
			if (c.getPlayer().getName().equalsIgnoreCase(arg3[0]))
			{
				for (PlayerCell cell : c.getCells())
				{
					try{
						if (cell.isDriving())
							cell.increaseMass(Integer.parseInt(arg3[2]));
						return true;
					} catch (NumberFormatException e) {}
				}
				return true;
			}
		}
		return true;
	}
}
