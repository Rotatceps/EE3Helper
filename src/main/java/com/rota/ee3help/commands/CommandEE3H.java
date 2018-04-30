package com.rota.ee3help.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rota.ee3help.Helper;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;

public class CommandEE3H extends CommandBase
{
	private static ArrayList<CommandBase> subCommands = new ArrayList<CommandBase>();
	private static List<String> commands = new ArrayList<String>();
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender cs)
	{
		return true;
	}
	
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
		boolean singlePlayer = MinecraftServer.getServer().isSinglePlayer();
		
        if (args.length >= 1)
        {
            for (CommandBase command : subCommands)
            {
                if (command.getCommandName().equalsIgnoreCase(args[0]))
                {
                	if(singlePlayer || command.canCommandSenderUseCommand(cs))
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
                	else
                	{
                		Helper.toChatErr(cs, "You do not have permission to use this sub-command.");
                		return;
                	}
                }
            }
        }
        
        Helper.toChatErr(cs, "Invalid, or no sub-command provided.");
        Helper.toChat(cs, EnumChatFormatting.GOLD + "Execute a sub-command with no arguments for instructions");
        Helper.toChat(cs, EnumChatFormatting.GOLD + "Commands are not case-sensitive");
        
        // Op only.
        if(singlePlayer || cs.canCommandSenderUseCommand(2, ""))
        {
            Helper.toChat(cs, EnumChatFormatting.AQUA + "Regen: "		+EnumChatFormatting.BLUE+"Forces EE3 to recalculate EMC values.");
            Helper.toChat(cs, EnumChatFormatting.AQUA + "AddItem: "		+EnumChatFormatting.BLUE+"Adds a single item.");
            Helper.toChat(cs, EnumChatFormatting.AQUA + "AddItemRange: "+EnumChatFormatting.BLUE+"Adds a range of items by their damage value.");
            Helper.toChat(cs, EnumChatFormatting.AQUA + "AddOre: "		+EnumChatFormatting.BLUE+"Adds the given ore name if it exists.");
            Helper.toChat(cs, EnumChatFormatting.AQUA + "AddOreRange: "	+EnumChatFormatting.BLUE+"Adds all the ore names of the given item.");
            Helper.toChat(cs, EnumChatFormatting.AQUA + "AddFluid: "	+EnumChatFormatting.BLUE+"Adds the given fluid name if it exists.");        	
        }

        Helper.toChat(cs, EnumChatFormatting.AQUA + "IdItem: "		+EnumChatFormatting.BLUE+"Outputs all relevant information about the given item.");
        
        //Helper.toChat(cs, EnumChatFormatting.AQUA + "remove: "	+EnumChatFormatting.BLUE+"Removes the specified entry from the list.");
        //Helper.toChat(cs, EnumChatFormatting.AQUA + "list: "		+EnumChatFormatting.BLUE+"List all entries in the values file. by page.");
        //Helper.toChat(cs, EnumChatFormatting.AQUA + "data: "		+EnumChatFormatting.BLUE+"Import/Export/List available data, use command for more info.");
        //Helper.toChat(cs, EnumChatFormatting.AQUA + "sync: "		+EnumChatFormatting.BLUE+"Forces a sync for oneself or all players (op command).");
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
	    	subCommands.add(new CommandForceRegen());
	    	
	    	subCommands.add(new CommandAddItem());
	    	subCommands.add(new CommandAddItemRange());
	    	subCommands.add(new CommandAddOre());
	    	subCommands.add(new CommandAddOreRange());
	    	subCommands.add(new CommandAddFluid());
	    	subCommands.add(new CommandIDItem());
	    	
	    	//subCommands.add(new CommandRemove());	    	
	    	//subCommands.add(new CommandList());
	    	//subCommands.add(new CommandData());
	    	//subCommands.add(new CommandSync());

	        for (CommandBase commandBase : subCommands)
	        {
	            commands.add(commandBase.getCommandName());
	        }
	    }
}