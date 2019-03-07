package io.github.silicondev.customitemmanager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventManager implements Listener {
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (!player.getInventory().getItemInMainHand().hasItemMeta()) return;
		
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			if (CustomItemManager.savedItems.get(i).item.equals(e.getPlayer().getInventory().getItemInMainHand())) {
				boolean wasOp = e.getPlayer().isOp();
				e.getPlayer().setOp(true);
				
				for (int c = 0; c < CustomItemManager.savedItems.get(i).commands.size(); c++) {
					String cmd = CustomItemManager.savedItems.get(i).commands.get(c);
					
					cmd = cmd.replace("@s", e.getPlayer().getName());
					
					if (CustomItemManager.debugMode) {
						e.getPlayer().sendMessage(Lang.TITLE.toString() + "Running command: " + cmd);
					}
					Bukkit.dispatchCommand(e.getPlayer(), cmd);
				}
				
				if (!wasOp) {
					e.getPlayer().setOp(false);
				}
			}
		}
	}
}
