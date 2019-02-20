package io.github.silicondev.customitemmanager;

import org.bukkit.command.CommandSender;

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
}
