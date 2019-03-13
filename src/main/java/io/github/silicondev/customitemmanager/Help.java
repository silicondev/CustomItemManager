package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help {
	List<List<CommandCIM>> items = new ArrayList<List<CommandCIM>>();
	int maxItems = 6;
	int pages = 0;
	
	public Help(List<CommandCIM> items) {
		int page = 0;
		int item = 0;
		boolean end = false;
		while (!end) {
			this.items.add(new ArrayList<CommandCIM>());
			for (int i = page * this.maxItems; i < (page * this.maxItems) + this.maxItems && !end; i++) {
				if (item == items.size() - 1) {
					end = true;
				}
				this.items.get(page).add(items.get(i));
				item++;
			}
			page++;
		}
		this.pages = page;
	}
	
	public void display(CommandSender sender, int page) {
		
		if (page > items.size() || page < 0) {
			sender.sendMessage(Lang.TITLE.toString() + Lang.ERR_HELP_NOPAGE.toString());
		} else {
			sender.sendMessage(Lang.TITLE.toString() + CustomItemManager.version + " " + Lang.HELP_TITLE.toString());
			
			for (int i = 0; i < this.items.get(page).size(); i++) {
				CommandCIM cmd = this.items.get(page).get(i);
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
				
				sender.sendMessage(Lang.HEADER.toString() + "/" + output + cmd.description);
			}
			
			Player player = (Player) sender;
			String username = player.getName();
			int realPage = page + 1;
			boolean hasPrev = true;
			boolean hasNext = true;
			
			Operators.HELP_CURRENTPAGE.addReturn(Integer.toString(realPage));
			Operators.HELP_TOTALPAGE.addReturn(Integer.toString(this.pages));
			if (page < 1) {
				hasPrev = false;
				Operators.HELP_PREVPAGE.addReturn(Integer.toString(0));
			} else {
				Operators.HELP_PREVPAGE.addReturn(Integer.toString(page - 1));
			}
			
			if (page >= this.pages - 1) {
				hasNext = false;
				Operators.HELP_NEXTPAGE.addReturn(Integer.toString(this.pages));
			} else {
				Operators.HELP_NEXTPAGE.addReturn(Integer.toString(page + 1));
			}
			
			String json = "NULL";
			if (hasNext && hasPrev) {
				json = Lang.HELP_JSON_PAGESELECT_PREVNEXT.runOperators(true);
			} else if (hasNext && !hasPrev) {
				json = Lang.HELP_JSON_PAGESELECT_NEXT.runOperators(true);
			} else if (!hasNext && hasPrev) {
				json = Lang.HELP_JSON_PAGESELECT_PREV.runOperators(true);
			}
			Bukkit.dispatchCommand(sender, "tellraw " + username + " " + json);
			sender.sendMessage(Lang.FOOTER.toString());
		}
	}
}
