package io.github.silicondev.customitemmanager;

import java.util.ArrayList;
import java.util.List;

public class CommandCIM {
	int outputID;     //What output path to activate.
	String inputName;     //Command name, the string will be used to check against the users input.
	String description;
	int reqParams = 0;     //Required number of arguments for the command.
	int optParams = 0;     //Optional number of arguments for the command.
	int maxParams = 0;     //Maximum number of arguments for the command.
	boolean noMaxParams = false;
	boolean playerOnly = false;
	boolean hasParent = false;
	String permNode;
	List<CommandCIM> children = new ArrayList<CommandCIM>();
	CommandCIM parent;
	boolean canChildren = false;
	
	public CommandCIM(String i, int rNum, int oNum, boolean playOnly, boolean cchildren, boolean hParent, int outID, String perm, String desc) {
		inputName = i;
		description = desc;
		reqParams = rNum;
		if (oNum == -1) {
			noMaxParams = true;
		}
		optParams = oNum;
		outputID = outID;
		playerOnly = playOnly;
		permNode = perm;
		canChildren = cchildren;
		hasParent = hParent;
		
		if (!noMaxParams) {
			maxParams = reqParams + optParams;     //Max arguments is the sum of optional and required arguments.
		} else {
			maxParams = -1;
		}
	}
}