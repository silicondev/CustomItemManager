package io.github.silicondev.customitemmanager;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class CustomItemManager extends JavaPlugin {
	public static String pluginName = "CustomItemManager";
	public static String pluginBC = "[" + pluginName + "]";
	public static boolean debugMode = true;
	public static String version = "inDev 0.0.2";
	
	static CommandOut comOut = new CommandOut(pluginBC);
	static List<CommandCIM> commands = new ArrayList<CommandCIM>();
	
	static List<CustomItem> savedItems = new ArrayList<CustomItem>();
	
	
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
			
			commands.add(new CommandCIM("item", 0, -1, true, true, true, 3, "customitemmanager.item", "Base command for item management."));
			commands.set(3, addParent(commands.get(3), commands.get(0)));
			commands.set(0, addChild(commands.get(0), commands.get(3)));
			
			commands.add(new CommandCIM("set", 1, 0, true, false, true, 4, "customitemmanager.item.set", "Saves a custom item to the database. Usage: /customitem item set <id>"));
			commands.set(4, addParent(commands.get(4), commands.get(3)));
			commands.set(3, addChild(commands.get(3), commands.get(4)));
			
			commands.add(new CommandCIM("spawn", 1, 0, true, false, true, 5, "customitemmanager.item.spawn", "Spawns a custom item from the database. Usage: /customitem item spawn <id>"));
			commands.set(5, addParent(commands.get(5), commands.get(3)));
			commands.set(3, addChild(commands.get(3), commands.get(5)));
			
			commands.add(new CommandCIM("delete", 1, 0, true, false, true, 6, "customitemmanager.item.delete", "Deletes a custom item from the database. Usage: /customitem item delete <id>"));
			commands.set(6, addParent(commands.get(6), commands.get(3)));
			commands.set(3, addChild(commands.get(3), commands.get(6)));
			
			commands.add(new CommandCIM("list", 0, 0, false, false, true, 7, "customitemmanager.item.list", "Lists all custom items in the database. Usage: /customitem item list"));
			commands.set(7, addParent(commands.get(7), commands.get(3)));
			commands.set(3, addChild(commands.get(3), commands.get(7)));
		} catch(NullPointerException e) {
			errNum++;
			getLogger().info("Error loading commands!");
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
