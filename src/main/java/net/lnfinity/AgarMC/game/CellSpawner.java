package net.lnfinity.AgarMC.game;

import net.lnfinity.AgarMC.AgarMC;
import net.lnfinity.AgarMC.cells.VirusCell;
import net.lnfinity.AgarMC.util.GameType;
import net.lnfinity.AgarMC.util.Utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CellSpawner implements Runnable {

    @Override
    public void run() {
        AgarMC plugin = AgarMC.get();
        if (plugin.getGame().getPlayers().isEmpty())
            return ;

        if(plugin.getGame().getVirus().size() < plugin.getGame().getMaxVirus()) {
            VirusCell virus = new VirusCell(Utils.randomLocation(plugin.getGame().getOrigin().getX(), plugin.getGame().getDimensions()), Utils.randomLocation(plugin.getGame().getOrigin().getZ(), plugin.getGame().getDimensions()));
            plugin.getGame().addVirus(virus);
        }

        for (CPlayer player : plugin.getGame().getPlayers())
        {
            player.getPlayer().getInventory().setItem(1, updateColorBlock(player.getPlayer().getInventory().getItem(1)));
            InventoryView iv = player.getPlayer().getOpenInventory();
            if (iv == null)
                continue ;
            Inventory i = iv.getTopInventory();
            if (i == null || !i.getName().equals(MenuGui.INV_NAME))
                continue ;
            i.setItem(0, updateColorBlock(i.getItem(0)));
        }
    }

    private ItemStack updateColorBlock(ItemStack item)
    {
        if (item != null && item.getType() == Material.WOOL)
        {
            short data = item.getDurability();
            if (AgarMC.get().getGame().getGameType() == GameType.TEAMS)
                switch (data)
                {
                case 3:
                    data = 14;
                    break ;
                case 5:
                    data = 3;
                    break ;
                case 14:
                    data = 5;
                    break ;
                default:
                    break ;
                }
            else
                data = (short)((data + 1) % 16);
            item.setDurability(data);
        }
        return item;
    }
}
