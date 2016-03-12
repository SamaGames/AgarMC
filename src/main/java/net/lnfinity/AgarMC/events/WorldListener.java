package net.lnfinity.AgarMC.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntityDamageByBlock(EntityDamageByBlockEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onWeatherChange(WeatherChangeEvent e){
		if (e.toWeatherState())
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent ev)
	{
		switch (ev.getEntityType())
		{
		case ARMOR_STAND:
		case PLAYER:
		case SLIME:
		case MAGMA_CUBE:
			return ;
		default:
			ev.setCancelled(true);
		}
	}
}
