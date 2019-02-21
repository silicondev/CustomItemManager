package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandOut {
	String pluginBC;
	
	public CommandOut(String pluginBC) {
		this.pluginBC = pluginBC;
	}
	
	public void test(CommandSender sender, boolean hasArgs, String arg) {
		if (hasArgs) {
			sender.sendMessage(pluginBC + " Test command successful! Test arg: " + arg);
		} else {
			sender.sendMessage(pluginBC + " Test command successful with no arguments!");
		}
	}
	
	public void help(CommandSender sender, boolean hasArgs, String arg) {
		sender.sendMessage("CustomItemManager " + CustomItemManager.version);
		sender.sendMessage("Searched commands:");
		if (hasArgs) {
			boolean found = false;
			for (int i = 0; i < CustomItemManager.commands.size() && !found; i++) {
				if (CustomItemManager.commands.get(i).inputName.equalsIgnoreCase(arg)) {
					found = true;
					displayCommandHelp(sender, CustomItemManager.commands.get(i));
				}
			}
		} else {
			for (int i = 0; i < CustomItemManager.commands.size(); i++) {
				displayCommandHelp(sender, CustomItemManager.commands.get(i));
			}
		}
		sender.sendMessage("=====");
	}
	
	public void displayCommandHelp(CommandSender sender, CommandCIM cmd) {
		String output = "";
		if (cmd.hasParent) {
			boolean foundEnd = false;
			CommandCIM getCmd = cmd;
			while (!foundEnd) {
				output = getCmd.inputName + " " + output;
				if (getCmd.hasParent) {
					getCmd = getCmd.parent;
				} else {
					foundEnd = true;
				}
			}
		} else {
			output = cmd.inputName;
		}
		sender.sendMessage("> /" + output + " | " + cmd.description);
	}
	
	public void addItem(CommandSender sender, String id) {
		Player player = (Player)sender;
		ItemStack item = player.getInventory().getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if (meta.hasLore()) {
			lore = meta.getLore();
		}
		lore.add(id);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		CustomItem ci = new CustomItem();
		ci.setId(id);
		ci.setItem(item);
		CustomItemManager.savedItems.add(ci);
		player.getInventory().setItemInMainHand(item);
		sender.sendMessage(CustomItemManager.pluginBC + " Item saved!");
	}
	
	public void listItems(CommandSender sender) {
		sender.sendMessage(CustomItemManager.pluginBC + " All items:");
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			CustomItem ci = CustomItemManager.savedItems.get(i);
			String itemName = "NULL";
			if (ci.item.getItemMeta().hasDisplayName()) {
				itemName = ci.getItem().getItemMeta().getDisplayName();
			} else {
				itemName = ci.getItem().getType().name();
			}
			sender.sendMessage(CustomItemManager.pluginBC + " " + itemName +" (" + ci.getId() + ")");
		}
	}
	
	public void spawnItem(CommandSender sender, String id) {
		boolean found = false;
		Player player = (Player)sender;
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			if (id.equals(CustomItemManager.savedItems.get(i).id)) {
				found = true;
				player.getInventory().addItem(CustomItemManager.savedItems.get(i).getItem());
				sender.sendMessage(CustomItemManager.pluginBC + " Item spawned!");
			}
		}
		if (!found) {
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: Item not found!");
		}
	}
	
	public void deleteItem(CommandSender sender, String id) {
		boolean found = false;
		Player player = (Player)sender;
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			if (id.equals(CustomItemManager.savedItems.get(i).getId())) {
				found = true;
				CustomItemManager.savedItems.remove(i);
				sender.sendMessage(CustomItemManager.pluginBC + " Item removed!");
			}
		}
		if (!found) {
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: Item not found!");
		}
	}
}
