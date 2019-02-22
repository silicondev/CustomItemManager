package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandOut {
	
	public CommandOut() {
		
	}
	
	public void test(CommandSender sender, boolean hasArgs, String arg) {
		if (hasArgs) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.TEST_ARG.toString() + arg);
		} else {
			sender.sendMessage(Lang.TITLE.toString() + Lang.TEST_NOARG.toString());
		}
	}
	
	public void help(CommandSender sender, boolean hasArgs, String arg) {
		sender.sendMessage(Lang.TITLE.toString() + CustomItemManager.version);
		sender.sendMessage(Lang.HELP_TITLE.toString());
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
		sender.sendMessage(Lang.HELP_FOOTER.toString());
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
		sender.sendMessage(Lang.HELP_COMMAND_HEADER.toString() + output + " | " + cmd.description);
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
		sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_SAVED.toString());
	}
	
	public void listItems(CommandSender sender) {
		sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_LIST_TITLE.toString());
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			CustomItem ci = CustomItemManager.savedItems.get(i);
			String itemName = "NULL";
			if (ci.item.getItemMeta().hasDisplayName()) {
				itemName = ci.getItem().getItemMeta().getDisplayName();
			} else {
				itemName = ci.getItem().getType().name();
			}
			sender.sendMessage(Lang.TITLE.toString() + itemName +" (" + ci.getId() + ")");
		}
		sender.sendMessage(Lang.ITEM_LIST_FOOTER.toString());
	}
	
	public void spawnItem(CommandSender sender, String id) {
		boolean found = false;
		Player player = (Player)sender;
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			if (id.equals(CustomItemManager.savedItems.get(i).id)) {
				found = true;
				player.getInventory().addItem(CustomItemManager.savedItems.get(i).getItem());
				sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_SPAWNED.toString());
			}
		}
		if (!found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOITEM.toString());
		}
	}
	
	public void deleteItem(CommandSender sender, String id) {
		boolean found = false;
		for (int i = 0; i < CustomItemManager.savedItems.size(); i++) {
			if (id.equals(CustomItemManager.savedItems.get(i).getId())) {
				found = true;
				CustomItemManager.savedItems.remove(i);
				sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_DELETED.toString());
			}
		}
		if (!found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOITEM.toString());
		}
	}
}
