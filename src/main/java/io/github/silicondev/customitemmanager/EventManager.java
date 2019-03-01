package io.github.silicondev.customitemmanager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EventManager {
	CustomItemManager plugin;
	
	public EventManager(CustomItemManager plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent e) {
		List<CustomItem> items = CustomItemManager.savedItems;
		Player player = e.getPlayer();
		if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
		
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).item.equals(e.getPlayer().getInventory().getItemInMainHand())) {
				e.getPlayer().setOp(true);
				
				Bukkit.dispatchCommand(e.getPlayer(), "say Hello");
				
				e.getPlayer().setOp(false);
			}
		}
	}
}
