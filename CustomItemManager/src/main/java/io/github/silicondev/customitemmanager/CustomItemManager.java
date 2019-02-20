package io.github.silicondev.customitemmanager;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class CustomItemManager extends JavaPlugin {
	public static String pluginName = "CustomItemManager";
	public static String pluginBC = "[" + pluginName + "]";
	public static boolean debugMode = false;
	public static String version = "inDev 0.0.2";
	
	static CommandOut comOut = new CommandOut(pluginBC);
	static List<CommandCIM> commands = new ArrayList<CommandCIM>();
	
	
	@Override
	public void onEnable() {
		getLogger().info("Startup Initialized!");
		int errNum = 0;
		try {
			this.getCommand("customitem").setExecutor(new CommandExec(this));
			commands.add(new CommandCIM("customitem", 0, -1, false, true, false, 0, "default", "Base command for " + pluginName));
			//CHILDREN
			commands.add(new CommandCIM("test", 0, -1, false, false, true, 1, "customitemmanager.test", "Tests the plugin. Usage: /customitem test <testArg>"));
			commands.set(1, addParent(commands.get(1), commands.get(0)));
			commands.set(0, addChild(commands.get(0), commands.get(1)));
			commands.add(new CommandCIM("help", 0, 1, false, false, true, 2, "customitemmanager.help", "Displays the help text. Usage: /customitem help <command>"));
			commands.set(2, addParent(commands.get(2), commands.get(0)));
			commands.set(0, addChild(commands.get(0), commands.get(2)));
		} catch(NullPointerException e) {
			errNum++;
			getLogger().info("Error loading command: customitem test");
		}
		getLogger().info("Initialization complete with " + Integer.toString(errNum) + " errors.");
		
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Shutting Down!");
	}
	
	public CommandCIM addChild(CommandCIM parent, CommandCIM child) {
		parent.children.add(child);
		return parent;
	}
	
	public CommandCIM addParent(CommandCIM child, CommandCIM parent) {
		child.parent = parent;
		return child;
	}
}
