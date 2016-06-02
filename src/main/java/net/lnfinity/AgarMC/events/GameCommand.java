package net.lnfinity.AgarMC.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameCommand implements CommandExecutor
{
    public static final String VERSION = "V3";

    @Override
    public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3)
    {
        arg0.sendMessage("AgarMC by Rigner & 6infinity8 ! Version : " + VERSION);
        return true;
    }
}
