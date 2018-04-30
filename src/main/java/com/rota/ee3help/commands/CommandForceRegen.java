package com.rota.ee3help.commands;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.handler.WorldEventHandler;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageSyncEnergyValues;
import com.rota.ee3help.Helper;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CommandForceRegen extends CommandModifyBase
{
	@Override
	public String getCommandName()
	{
		return "Regen";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "Regen";
	}
	
	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		DecimalFormat df = new DecimalFormat("#0.00"); 

		long startTime = System.currentTimeMillis();
		Helper.toChatErr(cs, EnumChatFormatting.AQUA + "Forcing EE3 to recomputing EMC values.");

		// EE3 stuff
		WorldEventHandler.hasInitilialized = false;
		EnergyValueRegistry.INSTANCE.setShouldSave(true);
		try 
		{
			Field field = EnergyValueRegistry.class.getDeclaredField("valuesNeedRegeneration");
			field.setAccessible(true);
			field.set(EnergyValueRegistry.INSTANCE, true);
		}  
		catch (Exception e) 
		{
			throw new RuntimeException("The current version of EE3 Helper is not compatible with the installed version of EE3, please uninstall EE3 Helper.",e);
		} 	
		EnergyValueRegistry.INSTANCE.save();
		EnergyValueRegistry.INSTANCE.compute();
		PacketHandler.INSTANCE.sendToAll(new MessageSyncEnergyValues());
		WorldEventHandler.hasInitilialized = true;

		Helper.toChatErr(cs, EnumChatFormatting.GREEN + "EMC recompute completed, duration: " + df.format((((double)(System.currentTimeMillis() - startTime))/1000.0)) + "sec.");
	}
}
