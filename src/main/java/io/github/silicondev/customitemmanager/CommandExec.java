package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandExec implements CommandExecutor {
	private final CustomItemManager plugin;
	
	public CommandExec(CustomItemManager plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandCIM[] getCom = new CommandCIM[CustomItemManager.commands.size()];
		CustomItemManager.commands.toArray(getCom);
		
		for (int i = 0; i < getCom.length; i++) {     //Tests first word for command initialiser, then the rest as multi args.
			String currentCommand = getCom[i].inputName;
			
			if (cmd.getName().equalsIgnoreCase(currentCommand)) {     //Correct command found.
				List<String> finArgs = new ArrayList<String>(Arrays.asList(args));
				
				if (getCom[i].canChildren) { //JOSH FIX FROM HERE
					
				}
				
				for (int a = 0; a < finArgs.size(); a++) {
					for (int c = 0; c < getCom[i].children.size(); c++) {
						if (finArgs.get(0).equals(getCom[i].children.get(c))) {
							
						}
					}
				} //TO HERE
				
				if (getCom[i].playerOnly) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (player.hasPermission(getCom[i].permNode) || getCom[i].permNode == "default") {
							commandHandle(getCom[i].outputID, finArgs, sender);
						} else {
							sender.sendMessage(CustomItemManager.pluginBC + "ERR: You need the perm node " + getCom[i].permNode + " to run that command. Contact an admin if you think you should have this.");
						}
					} else {
						sender.sendMessage(CustomItemManager.pluginBC + "ERR: Command needs to be run as player!");
					}
				} else {
					commandHandle(getCom[i].outputID, finArgs, sender);
				}
				return true;
			}
		}
		return false;
	}
	
	public static void commandHandle(int id, List<String> args, CommandSender sender) {
		if (args.size() < CustomItemManager.commands.get(id).reqParams) {     //Checks if command has correct amount of arguments, between the required and max. (Can allow for optional arguments)
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: Not enough arguments! " + Integer.toString(args.size()) + "/" + Integer.toString(CustomItemManager.commands.get(id).reqParams) + "!\n");
		} else if (args.size() > CustomItemManager.commands.get(id).maxParams) {
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: Too many arguments! " + Integer.toString(args.size()) + "/" + Integer.toString(CustomItemManager.commands.get(id).maxParams) + "!\n");
		} else {
			if (id == 0) {
				CustomItemManager.comOut.test(sender);
			}
		}
	}
}
