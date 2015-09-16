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
		File staticValues = new File(Files.STATIC_ENERGY_VALUES_JSON);
		
		if(staticValues.exists())
			staticValues.delete();
		
		WorldEventHandler.hasInitilialized = false;
		
        DynamicEnergyValueInitThread d = new DynamicEnergyValueInitThread();
        Thread t = new Thread(d, "EE3H_DYNEMCTHREAD");
       
        if(!CommandRemove.nogenRemoval) 
        	Helper.toChat(cs, EnumChatFormatting.AQUA + "Waiting for Dynamic EMC.");
        t.start();        
        long start = System.currentTimeMillis();
        try
        {
        	while(t.isAlive())
        	{
        		Thread.sleep(100);
        	}
        }
        catch(InterruptedException e) {}
        if(!CommandRemove.nogenRemoval) 
        	Helper.toChat(cs, EnumChatFormatting.GREEN + "Dynamic EMC complete: " + (System.currentTimeMillis() - start - 100)/1000 + "s");
        
        WorldEventHandler.hasInitilialized = true;	
        
		EnergyValueRegistry.getInstance().setShouldRegenNextRestart(true);
		EnergyValueRegistry.getInstance().save();
	}
}
