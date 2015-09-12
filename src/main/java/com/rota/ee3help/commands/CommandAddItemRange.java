package com.rota.ee3help.commands;

import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageSetEnergyValue;
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
	EnergyValueRegistry registryValues = EnergyValueRegistry.getInstance();
	RegistryNamespaced registryNames = GameData.getItemRegistry();

	private void addItemRange(String name, int start, int end, float value)
	{
		if(!registryNames.containsKey(name))
			return;
		
		ItemStack iStack = new ItemStack((Item) registryNames.getObject(name));
		
        Map<WrappedStack, EnergyValue> valuesPre = Helper.loadPre();
		for(int i = start; i <= end; i++)
		{
			iStack.setItemDamage(i);
			
			WrappedStack w = WrappedStack.wrap(iStack);
			EnergyValue e = new EnergyValue(value);
			
	        if (w != null && e != null && Float.compare(e.getValue(), 0) > 0)
	        {
	        	if(valuesPre.containsKey(w))
	        		valuesPre.replace(w, e);
	        	else
	                valuesPre.put(w, e);
	        	
	        	PacketHandler.INSTANCE.sendToAll(new MessageSetEnergyValue(w, e));
	        }
		}
        EnergyValueRegistry.getInstance().setShouldRegenNextRestart(true);
        Helper.savePre(valuesPre);
        
        if(EE3Help.config.auto_oredict)
        {
        	CommandAddOreRange.addOreRangeForRange(name, start, end, value);
        }
	}
	
	@Override
	public String getCommandName()
	{
		return "add-item-range";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "add-item-range <itemID/name> <dmgstart> <dmgend> <emcvalue>";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		resetFlag();
		try
		{
			String name;
			int start, end;
			float value;
			
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs, getCommandUsage(cs));
					break;
				case 4:
					name = Helper.getItemName(args[0]);
					start = Integer.parseInt(args[1]);
					end = Integer.parseInt(args[2]);
					value = Float.parseFloat(args[3]);
					
					if(name != null)
					{
						addItemRange(name,start,end,value);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ITEM RANGE: "+name);
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
