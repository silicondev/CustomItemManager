package io.github.silicondev.customitemmanager;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomItemManager extends JavaPlugin {
	public static String pluginName = "Custom Item Manager";
	public static boolean debugMode = false;
	public static String version = "Beta 0.2.6";
	private FileConfiguration itemConfig;
	public static YamlConfiguration langConfig;
	private File itemFile = new File(getDataFolder(), "items.yml");
	private static File langFile;
	
	CommandOut comOut = new CommandOut(this);
	static List<CommandCIM> commands = new ArrayList<CommandCIM>();
	
	static List<CustomItem> savedItems = new ArrayList<CustomItem>();
	
	Help helpPage;
	
	@Override
	public void onEnable() {
		getLogger().info("Startup Initialized!");
		
		getServer().getPluginManager().registerEvents(new EventManager(), this);
		
		int errNum = 0;
		try {
			this.getCommand("customitem").setExecutor(new CommandExec(this));
			
			//command, required params, optional params, player only, has children, has parent, id, perm node, description.
			commands.add(new CommandCIM("customitem", 0, -1, false, true, false, 0, "default", " | Base command for " + pluginName));
			//CHILDREN
			commands.add(new CommandCIM("test", 0, -1, false, false, true, 1, "customitemmanager.test", "<testArg> | Tests the plugin."));
			addHierarchy(1, 0);
			
			commands.add(new CommandCIM("help", 0, -1, false, true, true, 2, "customitemmanager.help", "<page #> | Displays the help text."));
			addHierarchy(2, 0);
			//CHILD
			commands.add(new CommandCIM("page", 1, 0, false, false, true, 3, "customitemmanager.help", "<page #> | Displays the help text."));
			addHierarchy(3, 2);
			
			commands.add(new CommandCIM("save", 1, 0, true, false, true, 4, "customitemmanager.save", "<id> | Saves a custom item to the database."));
			addHierarchy(4, 0);
			
			commands.add(new CommandCIM("spawn", 1, 0, true, false, true, 5, "customitemmanager.spawn", "<id> | Spawns a custom item from the database."));
			addHierarchy(5, 0);
			
			commands.add(new CommandCIM("delete", 1, 0, true, false, true, 6, "customitemmanager.delete", "<id> | Deletes a custom item from the database."));
			addHierarchy(6, 0);
			
			commands.add(new CommandCIM("list", 0, 0, false, false, true, 7, "customitemmanager.list", "| Lists all custom items in the database."));
			addHierarchy(7, 0);
			
			commands.add(new CommandCIM("addcommand", 2, -1, false, false, true, 8, "customitemmanager.command.add", "<id> <command (without slash)> | Adds a runnable command to the item."));
			addHierarchy(8, 0);
			
			commands.add(new CommandCIM("removecommand", 2, -1, false, false, true, 9, "customitemmanager.command.remove", "<id> <command (without slash)> | Removes a command from the item."));
			addHierarchy(9, 0);
			
			commands.add(new CommandCIM("clearcommands", 1, 0, false, false, true, 10, "customitemmanager.command.clear", "<id> | Clears all commands from the item."));
			addHierarchy(10, 0);
			
			commands.add(new CommandCIM("listcommands", 1, 0, false, false, true, 11, "customitemmanager.command.list", "<id> | Lists all commands from the item."));
			addHierarchy(11, 0);
			
			commands.add(new CommandCIM("reload", 0, 0, false, false, true, 12, "customitemmanager.reload", " | Reloads the plugin's configuration files."));
			addHierarchy(12, 0);
		} catch(NullPointerException e) {
			errNum++;
			getLogger().info("Error loading commands!");
		}
		
		helpPage = new Help(commands);
		
		errNum += load();
		loadLang();
		
		getLogger().info("Initialization complete with " + Integer.toString(errNum) + " errors.");
		
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Shutting Down!");
		getLogger().info("Saving items!");
		save();
	}
	
	public void addHierarchy(int child, int parent) {
		commands.set(child, addParent(commands.get(child), commands.get(parent)));
		commands.set(parent, addChild(commands.get(parent), commands.get(child)));
	}
	
	public CommandCIM addChild(CommandCIM parent, CommandCIM child) {
		parent.children.add(child);
		return parent;
	}
	
	public CommandCIM addParent(CommandCIM child, CommandCIM parent) {
		child.parent = parent;
		return child;
	}
	
	public void save() {
		
		if (itemFile.exists()) {
			getLogger().info("Existing item file found! Deleting...");
			itemFile.delete();
			getLogger().info("Item file deleted! Saving blank file...");
			try {
				itemFile.createNewFile();
				getLogger().info("Blank file saved and ready for editing!");
			} catch (IOException e) {
				getLogger().info("ERR: Error creatine new file!");
				e.printStackTrace();
			}
		}
		
		Map<String, Object> itemConfigVals = itemConfig.getValues(false);
		
		for (Map.Entry<String, Object> entry : itemConfigVals.entrySet()) {
			itemConfig.set(entry.getKey(), null);
		}
		
		for (int i = 0; i < savedItems.size(); i++) {
			getLogger().info("Saving item: (" + Integer.toString(i) + ") " + savedItems.get(i).id); 
			itemConfig.set(Integer.toString(i) + ".id", savedItems.get(i).id);
			itemConfig.set(Integer.toString(i) + ".item", savedItems.get(i).item);
			if (!savedItems.get(i).commands.isEmpty()) {
				for (int c = 0; c < savedItems.get(i).commands.size(); c++) {
					itemConfig.set(Integer.toString(i) + ".commands." + Integer.toString(c), savedItems.get(i).commands.get(c));
				}
			}
			getLogger().info("Item saved!");
		}
		
		try {
			itemConfig.save(itemFile);
			getLogger().info("Items saved!");
		} catch (IOException e) {
			getLogger().info("Error saving file! STACKTRACE BELOW");
			e.printStackTrace();
		}
	}
	
	public int load() {
		int errNum = 0;
		
		if (!itemFile.exists()) {
			itemFile.getParentFile().mkdirs();
			saveResource("items.yml", false);
		}

		itemConfig = new YamlConfiguration();
		try {
			itemConfig.load(itemFile);
		} catch (InvalidConfigurationException inv) {
			inv.printStackTrace();
			errNum++;
		} catch (IOException io) {
			io.printStackTrace();
			errNum++;
		}
		
		savedItems.clear();
		try {
			boolean end = false;
			int i = 0;
			while (!end) {
				CustomItem ci = new CustomItem();
				
				getLogger().info("Attempting to load id: " + Integer.toString(i));
				if (itemConfig.getString(Integer.toString(i) + ".id") != null) {
					ci.setId(itemConfig.getString(Integer.toString(i) + ".id"));
					getLogger().info("Found id: " + ci.getId());
				} else {
					getLogger().info("Cannot find id, end of file.");
					end = true;
				}
				
				getLogger().info("Attempting to load item: " + Integer.toString(i));
				if (itemConfig.get(Integer.toString(i) + ".item") != null) {
					ci.setItem((ItemStack)itemConfig.get(Integer.toString(i) + ".item"));
					getLogger().info("Found item: " + ci.getId());
				} else {
					getLogger().info("Cannot find item, end of file.");
					end = true;
				}
				
				getLogger().info("Attempting to load commands: " + Integer.toString(i));
				boolean cmdEnd = false;
				List<String> cmds = new ArrayList<String>();
				for (int id = 0; cmdEnd == false; id++) {
					if (itemConfig.get(Integer.toString(i) + ".commands." + Integer.toString(id)) != null) {
						String add = itemConfig.getString(Integer.toString(i) + ".commands." + Integer.toString(id));
						cmds.add(add);
						getLogger().info("Found command: " + add);
					} else {
						cmdEnd = true;
						getLogger().info("Cannot find commands, end of file.");
					}
				}
				if (!cmds.isEmpty()) {
					ci.setCommands(cmds);
				}
				
				if (!end) {
					savedItems.add(ci);
					getLogger().info("Added item: " + ci.getId());
				}
				i++;
			}
		} catch (NullPointerException e) {
			getLogger().info("Error reading from file. Is it empty?");
		}
		
		return errNum;
	}
	
	public void loadLang() {
		
		File lang = new File(getDataFolder(), "lang.yml");
	    if (!lang.exists()) {
	        try {
	            getDataFolder().mkdir();
	            lang.createNewFile();
	            URL url = this.getClass().getResource("lang.yml");
	            if (url != null) {
	            	try {
	            		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new File(url.toURI()));
	            		defConfig.save(lang);
		                Lang.setFile(defConfig);
		                //return defConfig; //FIX THIS
	            	} catch (URISyntaxException u) {
	            		u.printStackTrace();
	            		getLogger().warning("Couldn't create language file.");
	    	            getLogger().warning("This is a fatal error. Now disabling.");
	    	            this.setEnabled(false); // Without it loaded, we can't send them messages
	            	}
	            }
	        } catch(IOException e) {
	            e.printStackTrace(); // So they notice
	            getLogger().warning("Couldn't create language file.");
	            getLogger().warning("This is a fatal error. Now disabling.");
	            this.setEnabled(false); // Without it loaded, we can't send them messages
	        }
	    }
	    YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
	    for(Lang item:Lang.values()) {
	        if (conf.getString(item.getPath()) == null) {
	            conf.set(item.getPath(), item.getDefault());
	        }
	    }
	    Lang.setFile(conf);
	    CustomItemManager.langConfig = conf;
	    CustomItemManager.langFile = lang;
	    try {
	        conf.save(getLangFile());
	    } catch(IOException e) {
	    	getLogger().warning("Failed to save lang.yml.");
	    	getLogger().warning("Report this stack trace to <your name>.");
	        e.printStackTrace();
	    }
	}
	
	public YamlConfiguration getLang() {
	    return langConfig;
	}
	
	public File getLangFile() {
		return langFile;
	}
}
