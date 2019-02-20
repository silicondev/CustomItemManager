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
}
