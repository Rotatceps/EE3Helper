package com.rota.ee3help.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.reference.Files;
import com.rota.ee3help.EE3Help;
import com.rota.ee3help.Helper;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CommandRemove extends CommandModifyBase
{
	public ArrayList<WrappedStack> entries = new ArrayList<WrappedStack>();
	Map<WrappedStack, EnergyValue> valuesPre;
	
	private void buildList()
	{
		entries.clear();
        valuesPre = Helper.loadPre();
        for(Map.Entry<WrappedStack, EnergyValue> entry : valuesPre.entrySet())
        {
        	entries.add(entry.getKey());
        }
        Collections.sort(entries);
	}
	
	private void remove(int i)
	{
		if(i < 0 || i >= entries.size()) return;
		
		valuesPre.remove(entries.get(i));
		entries.remove(i);
		
        EnergyValueRegistry.getInstance().setShouldRegenNextRestart(true);
        Helper.savePre(valuesPre);
        
		File staticValues = new File(Files.STATIC_ENERGY_VALUES_JSON);
	}
	
	private void remove(int i, int j)
	{
		if(i < 0 || i >= entries.size() || j < 0 || j >= entries.size() || j < i) return;
		
		// subList is exclusive for the second parameter, so we take "j+1".
		
		for(WrappedStack w :entries.subList(i, j+1))
		{
			valuesPre.remove(w);
		}
		entries.subList(i, j+1).clear();
		
        EnergyValueRegistry.getInstance().setShouldRegenNextRestart(true);
        Helper.savePre(valuesPre);
	}
	
	@Override
	public String getCommandName()
	{
		return "remove";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "remove <index>, remove <start> <end>";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		if(!EE3Help.listAccurate)
		{
			Helper.toChat(cs, EnumChatFormatting.RED + "Indices may be innacurate, run 'list' first.");
			return;
		}
		try
		{
			buildList();
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs, "remove <index>");
					Helper.toChatErr(cs, "remove <start> <end>");
					break;
				case 1:
					remove(Integer.parseInt(args[0]));
					Helper.toChat(cs, EnumChatFormatting.LIGHT_PURPLE + "(-) REMOVE");
					break;
				case 2:
					remove(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
					Helper.toChat(cs, EnumChatFormatting.LIGHT_PURPLE + "(-) REMOVE RANGE");
					break;
				default:
					Helper.toChatErr(cs, "Invalid number of arguments for operation.");
					Helper.toChatErr(cs, "remove <index>");
					Helper.toChatErr(cs, "remove <start> <end>");
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
		resetFlag();
	}
}
