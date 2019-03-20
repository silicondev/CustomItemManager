# CustomItemManager

A custom item management tool, including functions for item runnable commands.

A Minecraft Plugin that allows admins to save custom made items, including all MetaData such as coloured names and lore, 
and spawn them back in at will; and to assign commands to multiple commands to saved items 
and have them be able to be run by any player that holds the custom item.
Perfect for RPG servers.

Download/Bug Reporting: https://www.spigotmc.org/resources/customitemmanager.65202/

## Wiki
### Saving items:
 
Items can be saved to the items.yml file with an id, this will save all data about the item, including name, enchantments, lore, etc.

### Adding commands:

Multiple commands can be added to the item, this will allow any player that holds the item to run all commands sequentially regardless of permission level.

THIS IS A DANGEROUS TOOL AND SHOULD BE USED WITH CAUTION.

But, this is also an incredibly useful tool. If you want special effects to be applied with a swing of a sword, or a teleport, or ANYTHING that can be run by a command, that can be added to be used with ease.
Warning: Command block operators (@a, @p, etc) cannot be used with these commands currently. This may change in a future update.
_However @s can be used to signify the holder's username._

## Commands

> /customitem OR /customitem help

Displays the help text.

> /customitem help page <page #>

Displays a certain page of help.

> /customitem save <id>

Saves a custom item to the database.

> /customitem reload

Reloads the plugin's configuration files.

> /customitem spawn <id>

Spawns a custom item from the database.

> /customitem delete <id>

Deletes a custom item from the database.

> /customitem list

Lists all custom items in the database.

> /customitem addcommand <id> <command>

Adds a runnable command to the item.

> /customitem removecommand <id> <command>

Removes a command from the item.

> /customitem listcommands <id>

Lists all commands from the item.

> /customitem clearcommands <id>

Clears all commands from the item.

## Alias: 
/ci >> /customitem (eg. /ci item set instead of /customitem item set)

## Permissions:

> customitemmanager.*
Permission for all commands.

> customitemmanager.help
Permission to use /customitem help.

> customitemmanager.save
Permission to use /customitem save.

> customitemmanager.spawn
Permission to use/customitem item spawn.

> customitemmanager.delete
Permission to use /customitem item delete.

> customitemmanager.list
Permission to use /customitem item list.

> customitemmanager.command.*
Permission to use all command based commands.

> customitemmanager.command.add
Permission to use /customitem addcommand.

> customitemmanager.command.remove
Permission to use /customitem removecommand.

> customitemmanager.command.list
Permission to use /customitem listcommands.

> customitemmanager.command.clear
Permission to use /customitem clearcommands.

## End of file.
Updated: 13/03/2019
