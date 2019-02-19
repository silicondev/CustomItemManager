package io.github.silicondev.customitemmanager;

import org.bukkit.inventory.ItemStack;

public class CustomItem {
	String id;
	ItemStack item;
	
	public CustomItem(String i, ItemStack itm) {
		item = itm;
		id = i;
	}
}