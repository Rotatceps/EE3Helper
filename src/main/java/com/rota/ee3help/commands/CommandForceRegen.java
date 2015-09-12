package com.rota.ee3help.commands;

import java.io.File;

import com.pahimar.ee3.exchange.DynamicEnergyValueInitThread;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.handler.WorldEventHandler;
import com.pahimar.ee3.reference.Files;
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
	
	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		/* Delete the static energy values file.
		 * Simulate a server restart.
		 */
		
		File staticValues = new File(Files.STATIC_ENERGY_VALUES_JSON);
		
		if(staticValues.exists())
			staticValues.delete();
		
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(true);
		EnergyValueRegistry.getInstance().save();
		
		WorldEventHandler.hasInitilialized = false;
		
        DynamicEnergyValueInitThread d = new DynamicEnergyValueInitThread();
        Thread t = new Thread(d, "EE3H_DYNEMCTHREAD");
       
        Helper.toChat(cs, EnumChatFormatting.AQUA + "Waiting for Dynamic EMC.");
        t.start();        
        try
        {
        	while(t.isAlive())
        	{
        		Thread.sleep(100);
        	}
        }
        catch(InterruptedException e) {}
        Helper.toChat(cs, EnumChatFormatting.GREEN + "Dynamic EMC complete.");
        
        WorldEventHandler.hasInitilialized = true;
	}
}
