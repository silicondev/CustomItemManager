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
				if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_COMMANDFOUND.toString() + runCmd.inputName);}
				
				if (getCom[i].canChildren && getCom[i].children.size() != 0) {
					if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_COMMANDCHILDREN_1.toString() + runCmd.inputName + Lang.DEB_COMMANDCHILDREN_2.toString());}
					boolean found = false;
					int argsFrom = 0;
					
					List<CommandCIM> children = getCom[i].children;
					for (int a = 0; a < finArgs.size() && !found; a++) {
						if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_CHECKARG.toString() + "(" + finArgs.get(a) + ")");}
						
						boolean childFound = false;
						for (int c = 0; c < children.size() && !childFound; c++) {
							if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_CHECKCHILDREN.toString() + "(" + children.get(c).inputName + ")");}
							if (children.get(c).inputName.equalsIgnoreCase(finArgs.get(a))) {
								childFound = true;
								if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_MATCHFOUND.toString());}
								if (children.get(c).canChildren && children.get(c).children.size() != 0) {
									if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_GRANDCHILDREN.toString());}
									children = children.get(c).children;
								} else {
									found = true;
									if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_FINALCOMMAND.toString());}
									runCmd = children.get(c);
									if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_RUNCMDCHANGE.toString() + runCmd.inputName);}
									argsFrom = a + 1;
								}
							}
						}
					}
					
					if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_ARGNUM.toString() + Integer.toString(finArgs.size()) + Lang.DEB_REMARGNUM.toString() + Integer.toString(argsFrom));}
					if (argsFrom != 0) {
						for (int d = 0; d < argsFrom; d++) {
							if (plugin.debugMode) {plugin.getLogger().info(Lang.DEB_REMARG.toString() + "(" + Integer.toString(d) + ") " + finArgs.get(0));}
							finArgs.remove(0);
						}
						if (plugin.debugMode) {
							plugin.getLogger().info(Lang.DEB_POSTREMARGLIST.toString());
							for (int a = 0; a < finArgs.size(); a++) {
								plugin.getLogger().info(Lang.DEB_ARGPOST.toString() + "(" + Integer.toString(a) + ") " + finArgs.get(a));
							}
						}
					}
				}
				
				
				
				if (runCmd.playerOnly) {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (player.hasPermission(runCmd.permNode) || runCmd.permNode == "default") {
							commandHandle(runCmd.outputID, finArgs, sender);
						} else {
							sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOPERM_1.toString() + runCmd.permNode + Lang.ERR_NOPERM_2.toString());
						}
					} else {
						sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_PLAYERONLY.toString());
					}
				} else {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						if (player.hasPermission(runCmd.permNode) || runCmd.permNode == "default") {
							commandHandle(runCmd.outputID, finArgs, sender);
						} else {
							sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOPERM_1.toString() + runCmd.permNode + Lang.ERR_NOPERM_2.toString());
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public void commandHandle(int id, List<String> args, CommandSender sender) {
		boolean hasRun = true;
		
		if (args.size() < CustomItemManager.commands.get(id).reqParams) {     //Checks if command has correct amount of arguments, between the required and max. (Can allow for optional arguments)
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NEGARG.toString() + Integer.toString(args.size()) + "/" + Integer.toString(CustomItemManager.commands.get(id).reqParams));
		} else if ((!CustomItemManager.commands.get(id).noMaxParams) && args.size() > CustomItemManager.commands.get(id).maxParams) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_POSARG.toString() + Integer.toString(args.size()) + "/" + Integer.toString(CustomItemManager.commands.get(id).maxParams));
		} else {
			if (id == 0) {
				//sender.sendMessage("DEFAULT COMMAND.");
				if (args.size() > 0) {
					plugin.comOut.help(sender, true, args.get(0));
				} else {
					plugin.comOut.help(sender, false, null);
				}
			} else if (id == 1) {
				if (args.size() > 0) {
					for (int i = 0; i < args.size(); i++) {
						plugin.comOut.test(sender, true, args.get(i));
					}
				} else {
					plugin.comOut.test(sender, false, null);
				}
			} else if (id == 2) {
				if (args.size() > 0) {
					plugin.comOut.help(sender, true, args.get(0));
				} else {
					plugin.comOut.help(sender, false, null);
				}
			} else if (id == 3) {
				
			} else if (id == 4) {
				plugin.comOut.addItem(sender, args.get(0));
			} else if (id == 5) {
				plugin.comOut.spawnItem(sender, args.get(0));
			} else if (id == 6) {
				plugin.comOut.deleteItem(sender, args.get(0));
			} else if (id == 7) {
				plugin.comOut.listItems(sender);
			} else if (id == 8) {
				plugin.save();
			} else if (id == 9) {
				String cmd = "";
				for (int i = 1; i < args.size(); i++) {
					if (i > 1) {
						cmd = cmd + " ";
					}
					cmd = cmd + args.get(i);
				}
				plugin.comOut.addCommand(sender, args.get(0), cmd);
			} else if (id == 10) {
				String cmd = "";
				for (int i = 1; i < args.size(); i++) {
					if (i > 1) {
						cmd = cmd + " ";
					}
					cmd = cmd + args.get(i);
				}
				plugin.comOut.removeCommand(sender, args.get(0), cmd);
			} else if (id == 11) {
				plugin.comOut.clearCommands(sender, args.get(0));
			} else if (id == 12) {
				plugin.comOut.listCommands(sender, args.get(0));
			} else {
				hasRun = false;
			}
		}
		
		if (!hasRun) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_NOCOMMAND.toString() + "(" + Integer.toString(id) + ")");
		}
	}
}
