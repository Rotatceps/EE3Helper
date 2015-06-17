package com.rota.ee3help;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CommandEE3H extends CommandBase
{
	private static ArrayList<CommandBase> subCommands = new ArrayList<CommandBase>();
	private static List<String> commands = new ArrayList<String>();
	
	@Override
	public String getCommandName()
	{
		return "ee3h";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "ee3h";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		// copyOfRange is exclusive for the second parameter.
		
        if (args.length >= 1)
        {
            for (CommandBase command : subCommands)
            {
                if (command.getCommandName().equalsIgnoreCase(args[0]) && command.canCommandSenderUseCommand(cs))
                {
                	if(args.length > 1)
                	{
                        command.processCommand(cs, Arrays.copyOfRange(args, 1, args.length));
                        return;
                	}
                	else
                	{
                		command.processCommand(cs, new String [] {});
                		return;
                	}
                }
            }
        }
        Helper.toChatErr(cs, "Invalid, or no sub-submmand provided.");
        Helper.toChat(cs, EnumChatFormatting.GOLD + "Execute a sub-command with no arguments for instructions");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "add-item: "+EnumChatFormatting.BLUE+"Adds a single item.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "add-item-range: "+EnumChatFormatting.BLUE+"Adds a range of items by their damage value.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "add-ore: "+EnumChatFormatting.BLUE+"Adds the given ore name if it exists.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "add-ore-range: "+EnumChatFormatting.BLUE+"Adds all the ore names of the given item.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "remove: "+EnumChatFormatting.BLUE+"Removes the specified entry from the list.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "regen: "+EnumChatFormatting.BLUE+"Forces EE3 to run Dynamic EMC.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "list: "+EnumChatFormatting.BLUE+"List all entries in the values file. by page.");
        Helper.toChat(cs, EnumChatFormatting.AQUA + "iditem: "+EnumChatFormatting.BLUE+"Outputs all relevant information about the given item.");
	}
	
	@Override
	public List addTabCompletionOptions(ICommandSender commandSender, String[] args)
	{
		if (args.length == 1)
		{
			return getListOfStringsFromIterableMatchingLastWord(args, commands);
		} else if (args.length >= 2)
		{
			for (CommandBase command : subCommands)
			{
				if (command.getCommandName().equalsIgnoreCase(args[0]))
				{
					return command.addTabCompletionOptions(commandSender, args);
				}
			}
		}

		return null;
	}

	    static
	    {
	    	subCommands.add(new CommandAddItem());
	    	subCommands.add(new CommandAddItemRange());
	    	subCommands.add(new CommandAddOre());
	    	subCommands.add(new CommandAddOreRange());
	    	
	    	subCommands.add(new CommandRemove());
	    	
	    	subCommands.add(new CommandList());
	    	subCommands.add(new CommandForceRegen());
	    	subCommands.add(new CommandIDItem());

	        for (CommandBase commandBase : subCommands)
	        {
	            commands.add(commandBase.getCommandName());
	        }
	    }
}