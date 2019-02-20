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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandCIM[] getCom = new CommandCIM[CustomItemManager.commands.size()];
		CustomItemManager.commands.toArray(getCom);
		
		for (int i = 0; i < getCom.length; i++) {     //Tests first word for command initialiser, then the rest as multi args.
			String currentCommand = getCom[i].inputName;
			
			if (cmd.getName().equalsIgnoreCase(currentCommand)) {     //Correct command found.
				List<String> finArgs = new ArrayList<String>(Arrays.asList(args));
				
				CommandCIM runCmd = getCom[i];
				if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Command found: " + runCmd.inputName);}
				
				if (getCom[i].canChildren && getCom[i].children.size() != 0) {
					if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Command " + runCmd.inputName + " has children commands!");}
					boolean found = false;
					int argsFrom = 0;
					for (int a = 0; a < finArgs.size() && !found; a++) {
						if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Checking argument (" + finArgs.get(a) + ")");}
						List<CommandCIM> children = new ArrayList<CommandCIM>();
						List<CommandCIM> newChildren = new ArrayList<CommandCIM>();
						
						if (a == 0) {
							children = getCom[i].children;
						} else {
							children = newChildren;
						}
						
						for (int c = 0; c < children.size() && !found; c++) {
							if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Checking children: (" + children.get(c).inputName + ")");}
							if (children.get(c).inputName.equalsIgnoreCase(finArgs.get(a))) {
								if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Match found!");}
								if (children.get(c).canChildren && children.get(c).children.size() != 0) {
									if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Child has children, running through.");}
									newChildren = children.get(c).children;
								} else {
									if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Final command found, setting argument placeholder.");}
									found = true;
									runCmd = children.get(c);
									if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: runCmd is now " + runCmd.inputName);}
									argsFrom = a + 1;
								}
							}
						}
					}
					
					if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Num of arguments: " + Integer.toString(finArgs.size()) + ". Num of arguments to remove: " + Integer.toString(argsFrom));}
					if (argsFrom != 0) {
						for (int d = 0; d < argsFrom; d++) {
							if (CustomItemManager.debugMode) {sender.sendMessage(CustomItemManager.pluginBC + " DEBUG: Removed argument " + Integer.toString(d));}
							finArgs.remove(d);
						}
					}
				}
				
				if (runCmd.playerOnly) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (player.hasPermission(runCmd.permNode) || runCmd.permNode == "default") {
							commandHandle(runCmd.outputID, finArgs, sender);
						} else {
							sender.sendMessage(CustomItemManager.pluginBC + "ERR: You need the perm node " + runCmd.permNode + " to run that command. Contact an admin if you think you should have this.");
						}
					} else {
						sender.sendMessage(CustomItemManager.pluginBC + "ERR: Command needs to be run as player!");
					}
				} else {
					commandHandle(runCmd.outputID, finArgs, sender);
				}
				return true;
			}
		}
		return false;
	}
	
	public static void commandHandle(int id, List<String> args, CommandSender sender) {
		boolean hasRun = false;
		
		if (args.size() < CustomItemManager.commands.get(id).reqParams) {     //Checks if command has correct amount of arguments, between the required and max. (Can allow for optional arguments)
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: Not enough arguments! " + Integer.toString(args.size()) + "/" + Integer.toString(CustomItemManager.commands.get(id).reqParams) + "!\n");
		} else if (args.size() > CustomItemManager.commands.get(id).maxParams && !CustomItemManager.commands.get(id).noMaxParams) {
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: Too many arguments! " + Integer.toString(args.size()) + "/" + Integer.toString(CustomItemManager.commands.get(id).maxParams) + "!\n");
		} else {
			if (id == 0) {
				sender.sendMessage("DEFAULT COMMAND.");
				hasRun = true;
				if (args.size() > 0) {
					CustomItemManager.comOut.help(sender, true, args.get(0));
				} else {
					CustomItemManager.comOut.help(sender, false, null);
				}
			} else if (id == 1) {
				hasRun = true;
				if (args.size() > 0) {
					for (int i = 0; i < args.size(); i++) {
						CustomItemManager.comOut.test(sender, true, args.get(i));
					}
				} else {
					CustomItemManager.comOut.test(sender, false, null);
				}
			} else if (id == 2) {
				hasRun = true;
				if (args.size() > 0) {
					CustomItemManager.comOut.help(sender, true, args.get(0));
				} else {
					CustomItemManager.comOut.help(sender, false, null);
				}
			}
		}
		
		if (!hasRun) {
			sender.sendMessage(CustomItemManager.pluginBC + " ERR: No command found! (" + Integer.toString(id) + ")");
		}
	}
}
