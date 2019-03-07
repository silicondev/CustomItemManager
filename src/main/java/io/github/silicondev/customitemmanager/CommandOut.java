package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class CommandOut {
	
	CustomItemManager plugin;
	
	public CommandOut(CustomItemManager plugin) {
		this.plugin = plugin;
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
		boolean found = false;
		for (int i = 0; i < CustomItemManager.savedItems.size() && !found; i++) {
			if (id.equalsIgnoreCase(CustomItemManager.savedItems.get(i).id)) {
				found = true;
			}
		}
		
		if (found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_ITEM_EXISTS.toString());
		} else {
			Player player = (Player)sender;
			ItemStack item = player.getInventory().getItemInMainHand();
			ItemMeta meta = item.getItemMeta();
			List<String> lore = new ArrayList<String>();
			if (meta.hasLore()) {
				lore = meta.getLore();
			}
			lore.add(String.valueOf(ChatColor.translateAlternateColorCodes('&', Lang.ITEM_LORE_PREFIX + id)));
			meta.setLore(lore);
			item.setItemMeta(meta);
			
			CustomItem ci = new CustomItem();
			ci.setId(id);
			ci.setItem(item);
			CustomItemManager.savedItems.add(ci);
			player.getInventory().setItemInMainHand(item);
			sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_SAVED.toString());
		}
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
			sender.sendMessage(String.valueOf(ChatColor.translateAlternateColorCodes('&', Lang.TITLE.toString() + itemName + "&r (" + ci.getId() + ")")));
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
		for (int i = 0; i < CustomItemManager.savedItems.size() && !found; i++) {
			if (id.equals(CustomItemManager.savedItems.get(i).getId())) {
				found = true;
				CustomItemManager.savedItems.remove(i);
				
				if (sender instanceof Player) {
					
					Player player = (Player)sender;
					
					PlayerInventory inv = player.getInventory();
					
					ItemStack[] items = inv.getContents();
					for (int item = 0; item < items.length; item++) {
						if (plugin.debugMode) {plugin.getLogger().info("DEBUG: Testing item: " + Integer.toString(item));}
						ItemStack currentItem = items[item];
						if (currentItem != null) {
							if (currentItem.hasItemMeta()) {
								ItemMeta meta = currentItem.getItemMeta();
								if (meta.hasLore()) {
									if (plugin.debugMode) {plugin.getLogger().info("DEBUG: Item has lore.");}
									List<String> lore = meta.getLore();
									for (int loreNum = 0; loreNum < lore.size(); loreNum++) {
										if (plugin.debugMode) {plugin.getLogger().info("DEBUG: Testing lore line " + Integer.toString(loreNum) + ": " + lore.get(loreNum) + " contains " + id + "?");}
										if (String.valueOf(lore.get(loreNum)).contains(id)) {
											if (plugin.debugMode) {plugin.getLogger().info("DEBUG: Lore matches ID");}
											lore.remove(loreNum);
											if (plugin.debugMode) {plugin.getLogger().info("DEBUG: Lore removed.");}
										}
									}
									meta.setLore(lore);
									items[item].setItemMeta(meta);
									player.getInventory().setContents(items);
									if (plugin.debugMode) {plugin.getLogger().info("DEBUG: Changed inventory.");}
								}
							}
						}
					}
				}
				
				sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_DELETED.toString());
			}
		}
		if (!found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOITEM.toString());
		}
	}
	
	public void addCommand(CommandSender sender, String id, String cmd) {
		List<CustomItem> items = CustomItemManager.savedItems;
		boolean found = false;
		int idFound = 0;
		for (int i = 0; i < items.size() && !found; i++) {
			if (items.get(i).id.equalsIgnoreCase(id)) {
				idFound = i;
				found = true;
				items.get(i).addCommand(cmd);
			}
		}
		if (!found) {
			sender.sendMessage(Lang.HELP_TITLE.toString() + Lang.ERR_NOITEM.toString());
		} else {
			CustomItemManager.savedItems = items;
			sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_COMMAND_HEADER.toString() + cmd + Lang.ITEM_COMMANDADDED.toString() + items.get(idFound).id + Lang.ITEM_COMMAND_FOOTER.toString());
		}
	}
	
	public void removeCommand(CommandSender sender, String id, String cmd) {
		List<CustomItem> items = CustomItemManager.savedItems;
		boolean found = false;
		for (int i = 0; i < items.size() && !found; i++) {
			if (items.get(i).id.equalsIgnoreCase(id)) {
				found = true;
				if (!items.get(i).removeCommand(cmd)) {
					sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_ITEM_NOCOMMAND.toString());
				} else {
					sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_COMMAND_HEADER.toString() + cmd + Lang.ITEM_COMMANDREMOVED.toString() + items.get(i).item.getItemMeta().getDisplayName() + Lang.ITEM_COMMAND_FOOTER.toString());
				}
			}
		}
		if (!found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOITEM.toString());
		} else {
			CustomItemManager.savedItems = items;
		}
	}
	
	public void clearCommands(CommandSender sender, String id) {
		List<CustomItem> items = CustomItemManager.savedItems;
		boolean found = false;
		for (int i = 0; i < items.size() && !found; i++) {
			if (items.get(i).id.equalsIgnoreCase(id)) {
				found = true;
				items.get(i).clearCommands();
				sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_COMMANDSCLEARED.toString() + items.get(i).item.getItemMeta().getDisplayName() + Lang.ITEM_COMMAND_FOOTER.toString());
			}
		}
		if (!found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOITEM);
		}
		CustomItemManager.savedItems = items;
	}
	
	public void listCommands(CommandSender sender, String id) {
		List<CustomItem> items = CustomItemManager.savedItems;
		boolean found = false;
		for (int i = 0; i < items.size() && !found; i++) {
			if (items.get(i).id.equalsIgnoreCase(id)) {
				found = true;
				sender.sendMessage(Lang.TITLE.toString() + Lang.ITEM_COMMAND_LIST_TITLE.toString());
				for (int c = 0; c < items.get(i).commands.size(); c++) {
					sender.sendMessage(Lang.HELP_COMMAND_HEADER + items.get(i).commands.get(c));
				}
				sender.sendMessage(Lang.ITEM_LIST_FOOTER.toString());
			}
		}
		if (!found) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOITEM);
		}
	}
}
