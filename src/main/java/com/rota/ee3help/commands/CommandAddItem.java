package com.rota.ee3help.commands;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.exchange.EnergyValueRegistry;

import com.rota.ee3help.EE3Help;
import com.rota.ee3help.Helper;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;

import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;

public class CommandAddItem extends CommandModifyBase
{
	EnergyValueRegistry registryValues = EnergyValueRegistry.INSTANCE;
	RegistryNamespaced registryNames = GameData.getItemRegistry();
	
	public CommandAddItem()
	{
		name = "AddItem";
		usage.add("AddItem <emcvalue>, Uses held item.");
		usage.add("AddItem <itemID/name> <emcvalue> (DMG=0|N/A)");
		usage.add("AddItem <itemID/name> <damagevalue> <emcvalue>");
	}
	
	private void addItem(String name, int damageValue, float value)
	{
		if(!registryNames.containsKey(name))
			return;
		
		ItemStack iStack = new ItemStack((Item) registryNames.getObject(name));

		iStack.setItemDamage(damageValue);
		
		WrappedStack w = WrappedStack.wrap(iStack);
		EnergyValue e = new EnergyValue(value);
		
        if (w != null && e != null && Float.compare(e.getValue(), 0) > 0)
        {
        	EnergyValueRegistryProxy.setEnergyValue(w, e, EnergyValueRegistryProxy.Phase.PRE_CALCULATION);
    		EnergyValueRegistry.INSTANCE.save();
            if(EE3Help.config.auto_oredict)
            {
            	CommandAddOreRange.addOreRange(name, damageValue, value);
            }
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
					Helper.toChatErr(cs,getUsageString());
					break;
				case 1:
					if(cs instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) cs;
						ItemStack iStack = player.getHeldItem();
						value = Float.parseFloat(args[0]);
						if(iStack != null)
						{
							addItem(registryNames.getNameForObject(iStack.getItem()),iStack.getItemDamage(),value);
							Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ITEM: "+registryNames.getNameForObject(iStack.getItem()));
						}
						else
							Helper.toChatErr(cs, "(X) Invalid item.");
					}
					else
						FMLLog.getLogger().error("EE3H Command sender not instance of player, can't check held item.");
					break;
				case 2:
					name = Helper.getItemName(args[0]);
					value = Float.parseFloat(args[1]);
					
					if(name != null)
					{
						addItem(name,0,value);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ITEM: "+name);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid item.");
					break;
				case 3:
					name = Helper.getItemName(args[0]);

					value = Float.parseFloat(args[2]);
					
					if(name != null)
					{
						addItem(name,0,value);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ITEM: "+name);
					}
					else
						Helper.toChatErr(cs, "(X) No such item.");
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
