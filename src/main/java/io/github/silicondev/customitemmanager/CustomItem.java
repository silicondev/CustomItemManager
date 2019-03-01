package io.github.silicondev.customitemmanager;

import org.bukkit.inventory.ItemStack;

public class CustomItem {
	String id;
	ItemStack item;
	
	public CustomItem() {
	}
	
	public String getId() {
		return id;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
	}
}