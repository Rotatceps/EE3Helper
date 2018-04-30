package com.rota.ee3help.commands;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.WrappedStack;
import com.rota.ee3help.EE3Help;
import com.rota.ee3help.Helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;

public class CommandAddItemRange extends CommandModifyBase
{
	EnergyValueRegistry registryValues = EnergyValueRegistry.INSTANCE;
	RegistryNamespaced registryNames = GameData.getItemRegistry();

	public CommandAddItemRange()
	{
		name = "AddItemRange";
		usage.add("AddItemRange <itemID/name> <dmgstart> <dmgend> <emcvalue>");
	}
	
	private void addItemRange(String name, int start, int end, float value)
	{
		if(!registryNames.containsKey(name))
			return;
		
		ItemStack iStack = new ItemStack((Item) registryNames.getObject(name));
		
		for(int i = start; i <= end; i++)
		{
			iStack.setItemDamage(i);
			
			WrappedStack w = WrappedStack.wrap(iStack);
			EnergyValue e = new EnergyValue(value);
			
	        if (w != null && e != null && Float.compare(e.getValue(), 0) > 0)
	        {
	        	EnergyValueRegistryProxy.setEnergyValue(w, e, EnergyValueRegistryProxy.Phase.PRE_CALCULATION);
	    		EnergyValueRegistry.INSTANCE.save();
	        }
		}
        if(EE3Help.config.auto_oredict)
        {
        	CommandAddOreRange.addOreRangeForRange(name, start, end, value);
        }
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		try
		{
			String name;
			float value;
			
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs, getCommandUsage(cs));
					break;
				case 4:
					int start, end;
					name = Helper.getItemName(args[0]);
					start = Integer.parseInt(args[1]);
					end = Integer.parseInt(args[2]);
					value = Float.parseFloat(args[3]);
					
					if(name != null)
					{
						addItemRange(name,start,end,value);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ITEM RANGE: "+name +" DMG: " + start + "-" + end);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid item.");

					break;
				default:
					Helper.toChatErr(cs, "Invalid number of arguments for operation.");
					Helper.toChatErr(cs, getCommandUsage(cs));
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
