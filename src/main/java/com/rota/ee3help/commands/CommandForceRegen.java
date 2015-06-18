package com.rota.ee3help.commands;

import java.io.File;

import com.pahimar.ee3.exchange.DynamicEnergyValueInitThread;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.rota.ee3help.DataTracker;
import com.rota.ee3help.Helper;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CommandForceRegen extends CommandModifyBase
{
	@Override
	public String getCommandName()
	{
		return "regen";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "regen";
	}
	
	public void processCommand(ICommandSender cs, String[] args)
	{
		Helper.toChat(cs, EnumChatFormatting.AQUA + "Clearing saved values & forcing DynamicEMC");
		File energyValuesDirectory = new File(DataTracker.EE3_ENERGYVALUES_DIR);

		if(energyValuesDirectory.exists() && energyValuesDirectory.isDirectory())
		{
			File [] files = energyValuesDirectory.listFiles();
			for(File f : files)
			{
				if(f.getName().toLowerCase().contains(".gz"))
					f.delete();
			}
		}
		DynamicEnergyValueInitThread.initEnergyValueRegistry();
		Helper.toChat(cs, EnumChatFormatting.GOLD + "Values should appear shortly.");	
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(false);
	}
}
