package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class CustomItem {
	String id;
	ItemStack item;
	List<String> commands = new ArrayList<String>();
	
	public CustomItem() {
	}
	
	public String getId() {
		return id;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public List<String> getCommands() {
		return commands;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
	
	public void addCommand(String cmd) {
		commands.add(cmd);
	}
	
	public boolean removeCommand(String cmd) {
		boolean found = false;
		for (int i = 0; i < commands.size() && !found; i++) {
			if (commands.get(i).equalsIgnoreCase(cmd)) {
				found = true;
				commands.remove(i);
			}
		}
		return found;
	}
	
	public void clearCommands() {
		commands.clear();
	}
}