package io.github.silicondev.customitemmanager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;

public enum Lang {
	
	TITLE("plugin-name", "&f[&eCustomItemManager&f] &r"),
	TEST_ARG("test-arg", "Test Command successful! Test arg: "),
	TEST_NOARG("test-noarg", "Test command successful with no arguments!"),
	HELP_TITLE("help-title", "Searched Commands:"),
	HELP_FOOTER("help-footer", "========="),
	HELP_COMMAND_HEADER("help-command-header", "> /"),
	ITEM_SAVED("item-saved", "Item saved!"),
	ITEM_LIST_TITLE("item-list-title", "All items:"),
	ITEM_LIST_FOOTER("item-list-footer", "========="),
	ITEM_SPAWNED("item-spawned", "Item spawned!"),
	ITEM_DELETED("item-deleted", "Item removed!"),
	/*ERRORS*/
	ERR_NOPERM_1("err-noperm-1", "ERR: You need the perm node "),
	ERR_NOPERM_2("err-noperm-2", " to run that command! Contact an admin if you think this is a mistake."),
	ERR_PLAYERONLY("err-playeronly", "ERR: Command can only be run as a player!"),
	ERR_NEGARG("err-negarg", "ERR: Not enough arguments: "),
	ERR_POSARG("err-posarg", "ERR: Too many arguments: "),
	ERR_NOCOMMAND("err-nocommand", "ERR: Unknown command!"),
	ERR_NOITEM("err-noitem", "ERR: 404 Item not found!"),
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
	
}
