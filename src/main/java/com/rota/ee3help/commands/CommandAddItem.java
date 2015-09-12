package com.rota.ee3help.commands;

import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageSetEnergyValue;
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

public class CommandAddItem extends CommandModifyBase
{
	EnergyValueRegistry registryValues = EnergyValueRegistry.getInstance();
	RegistryNamespaced registryNames = GameData.getItemRegistry();
	
	private void addItem(String name, int damageValue, float value)
	{
		if(!registryNames.containsKey(name))
			return;
		
		ItemStack iStack = new ItemStack((Item) registryNames.getObject(name));
        Map<WrappedStack, EnergyValue> valuesPre = Helper.loadPre();
        
		iStack.setItemDamage(damageValue);
		
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

        EnergyValueRegistry.getInstance().setShouldRegenNextRestart(true);
        Helper.savePre(valuesPre);
        
        if(EE3Help.config.auto_oredict)
        {
        	CommandAddOreRange.addOreRange(name, damageValue, value);
        }
	}
	
	@Override
	public String getCommandName()
	{
		return "add-item";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "Use command with no arguments.";
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
					Helper.toChatErr(cs, "add-item <emcvalue>, Uses held item.");
					Helper.toChatErr(cs, "add-item <itemID/name> <emcvalue> (DMG=0|N/A)");
					Helper.toChatErr(cs, "add-item <itemID/name> <damagevalue> <emcvalue>");
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
					start = end = Integer.parseInt(args[1]);
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
					Helper.toChatErr(cs, "add-item <emcvalue>, Uses held item.");
					Helper.toChatErr(cs, "add-item <itemID/name> <emcvalue> (DMG=0|N/A)");
					Helper.toChatErr(cs, "add-item <itemID/name> <damagevalue> <emcvalue>");
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
