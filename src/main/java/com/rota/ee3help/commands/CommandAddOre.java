package com.rota.ee3help.commands;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.OreStack;
import com.pahimar.ee3.exchange.WrappedStack;
import com.rota.ee3help.Helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

public class CommandAddOre extends CommandModifyBase
{
	EnergyValueRegistry registryValues = EnergyValueRegistry.INSTANCE;
	RegistryNamespaced registryNames = GameData.getItemRegistry();

	public CommandAddOre()
	{
		name = "AddOre";
		usage.add("AddOre <orename> <emcvalue>");
	}
	
	private void addOre(String name, float value)
	{
		WrappedStack w = WrappedStack.wrap(new OreStack(name));
		EnergyValue e = new EnergyValue(value);
		
        if (w != null && e != null && Float.compare(e.getValue(), 0) > 0)
        {
        	EnergyValueRegistryProxy.setEnergyValue(w, e, EnergyValueRegistryProxy.Phase.PRE_CALCULATION);
    		EnergyValueRegistry.INSTANCE.save();
        }
	}
	
	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		try
		{			
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs,getUsageString());
					break;
				case 2:
					if(OreDictionary.doesOreNameExist(args[0]))
					{
						addOre(args[0],Float.parseFloat(args[1]));
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ORE: "+args[0]);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid OreDict name.");

					break;
				default:
					Helper.toChatErr(cs, "Invalid number of arguments for operation.");
					Helper.toChatErr(cs,getUsageString());
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
