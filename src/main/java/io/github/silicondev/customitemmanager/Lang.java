package io.github.silicondev.customitemmanager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;

public enum Lang {
	
	TITLE("plugin-name", "&f[&eCustomItemManager&f] &r"),
	TEST_ARG("test-arg", "Test Command successful! Test arg: "),
	TEST_NOARG("test-noarg", "Test command successful with no arguments!"),
	FOOTER("help-footer", "========="),
	HEADER("help-command-header", "> "),
	HELP_TITLE("help-title", "Searched Commands:"),
	HELP_JSON_PAGESELECT_PREVNEXT("help-json-pageselect-prevnext", "[\"\",{\"text\":\"Page: \"},{\"text\":\"[<]\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/customitem help page %prevPage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Previous Page\"}},{\"text\":\" %currentPage%\",\"color\":\"green\"},{\"text\":\"/%totalPage%\",\"color\":\"dark_green\"},{\"text\":\" \"},{\"text\":\"[>]\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/customitem help page %nextPage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Next Page\"}}]"),
	HELP_JSON_PAGESELECT_NEXT("help-json-pageselect-prevnext", "[\"\",{\"text\":\"Page: \"},{\"text\":\"[<]\",\"color\":\"gray\"},{\"text\":\" %currentPage%\",\"color\":\"green\"},{\"text\":\"/%totalPage%\",\"color\":\"dark_green\"},{\"text\":\" \"},{\"text\":\"[>]\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/customitem help page %nextPage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Next Page\"}}]"),
	HELP_JSON_PAGESELECT_PREV("help-json-pageselect-prevnext", "[\"\",{\"text\":\"Page: \"},{\"text\":\"[<]\",\"color\":\"yellow\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/customitem help page %prevPage%\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Previous Page\"}},{\"text\":\" %currentPage%\",\"color\":\"green\"},{\"text\":\"/%totalPage%\",\"color\":\"dark_green\"},{\"text\":\" \"},{\"text\":\"[>]\",\"color\":\"gray\"}]"),
	ITEM_SAVED("item-saved", "Item saved!"),
	ITEM_LIST_TITLE("item-list-title", "All items:"),
	ITEM_COMMAND_LIST_TITLE("item-command-list-title", "All commands:"),
	ITEM_SPAWNED("item-spawned", "Item spawned!"),
	ITEM_DELETED("item-deleted", "Item removed!"),
	ITEM_COMMAND_HEADER("item-command-header", "Command {"),
	ITEM_COMMANDADDED("item-commandadded", "} has been added to the item "),
	ITEM_COMMANDREMOVED("item-commandremoved", "} has been removed from the item "),
	ITEM_COMMAND_FOOTER("item-commandadded-footer", "!"),
	ITEM_COMMANDSCLEARED("item-commandscleared", "All commands have been removed from the item "),
	ITEM_LORE_PREFIX("item-lore-prefix", "&8"),
	/*ERRORS*/
	ERR_NOPERM_1("err-noperm-1", "ERR: You need the perm node "),
	ERR_NOPERM_2("err-noperm-2", " to run that command! Contact an admin if you think this is a mistake."),
	ERR_PLAYERONLY("err-playeronly", "ERR: Command can only be run as a player!"),
	ERR_NEGARG("err-negarg", "ERR: Not enough arguments: "),
	ERR_POSARG("err-posarg", "ERR: Too many arguments: "),
	ERR_INVARG("err-invarg", "ERR: Invalid argument!"),
	ERR_NOCOMMAND("err-nocommand", "ERR: Unknown command!"),
	ERR_ITEM_NOCOMMAND("err-item-nocommand", "ERR: Command not found on specified item!"),
	ERR_NOITEM("err-noitem", "ERR: 404 Item not found!"),
	ERR_ITEM_EXISTS("err-item-exists", "ERR: Item with that id already exists!"),
	ERR_HELP_NOPAGE("err-help-nopage", "ERR: That page doesn't exist!"),
	/*DEBUGS*/
	DEB_COMMANDFOUND("deb-commandfound", "DEBUG: Command found: "),
	DEB_COMMANDCHILDREN_1("deb-commandchildren-1", "DEBUG: Command "),
	DEB_COMMANDCHILDREN_2("deb-commandchildren-2", " has children commands!"),
	DEB_CHECKARG("deb-checkarg", "DEBUG: Checking argument: "),
	DEB_CHECKCHILDREN("deb-checkchildren", "DEBUG: Checking children: "),
	DEB_MATCHFOUND("deb-matchfound", "DEBUG: Match found!"),
	DEB_GRANDCHILDREN("deb-grandchildren", "DEBUG: Child has children! Running through..."),
	DEB_FINALCOMMAND("deb-finalcommand", "DEBUG: Final command found! Setting argument placeholder..."),
	DEB_RUNCMDCHANGE("deb-runcmdchange", "DEBUG: runCmd is now: "),
	DEB_ARGNUM("deb-argnum", "DEBUG: Number of arguments: "),
	DEB_REMARGNUM("deb-remargnum", ", number of arguments to remove: "),
	DEB_REMARG("deb-remarg", "DEBUG: Removed argument: "),
	DEB_POSTREMARGLIST("deb-postremarglist", "DEBUG: Remaining arguments: "),
	DEB_ARGPOST("deb-argpost", "DEBUG: Arg: ");
	
	
	private String path;
	private String def;
	private static FileConfiguration LANG;
	
	Lang(String path, String def) {
		this.path = path;
		this.def = def;
	}
	
	public static void setFile(FileConfiguration langConfig) {
		LANG = langConfig;
	}
	
	@Override
	public String toString() {
		if (LANG.getString(this.path, def).contains("&")) {
			return String.valueOf(ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, def)));
		} else {
			return LANG.getString(this.path, def);
		}
	}
	
	public String getDefault() {
		return this.def;
	}
	
	public String getPath() {
		return this.path;
	}
	
	public String getString() {
		return LANG.getString(this.path, def);
	}
	
	public String getDefaultString() {
		return this.def;
	}
	
	public String runOperators(boolean useDefault) {
		String out;
		if (useDefault) {
			out = this.def;
		} else {
			out = LANG.getString(this.path, def);
		}
		
		for (int i = 0; i < Operators.values().length; i++) {
			if (out.contains(Operators.values()[i].getOperator())) {
				out = out.replace(Operators.values()[i].getOperator(), Operators.values()[i].getReturn());
			}
		}
		
		return out;
	}
	
	/*
# For the next JSON string, please include all operators:
# %nextPage%
# %prevPage%
# %currentPage%
# %totalPage%
# And please remember to put a  before every " (if the string begins and ends with " then ignore these when placing )
help-json-pageselect: "["",{"text":"Page: "},{"text":"[<]","color":"yellow","clickEvent":{"action":"run_command","value":"customitem help page %prevPage%"},"hoverEvent":{"action":"show_text","value":"Previous Page"}},{"text":" %currentPage%","color":"green"},{"text":"/%totalPage%","color":"dark_green"},{"text":" "},{"text":"[>]","color":"yellow","clickEvent":{"action":"run_command","value":"customitem help page %nextPage%"},"hoverEvent":{"action":"show_text","value":"Next Page"}}]"
	*/
	
}
