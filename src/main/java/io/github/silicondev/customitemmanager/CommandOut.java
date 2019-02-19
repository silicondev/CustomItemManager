package io.github.silicondev.customitemmanager;

import org.bukkit.command.CommandSender;

public class CommandOut {
	String pluginBC;
	
	public CommandOut(String pluginBC) {
		this.pluginBC = pluginBC;
	}
	
	public void test(CommandSender sender) {
		sender.sendMessage(pluginBC + " Test command successful!");
	}
}
