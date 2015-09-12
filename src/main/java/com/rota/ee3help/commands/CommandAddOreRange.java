package com.rota.ee3help.commands;

import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.exchange.OreStack;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageSetEnergyValue;
import com.rota.ee3help.Helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

public class CommandAddOreRange extends CommandModifyBase
{
	EnergyValueRegistry registryValues = EnergyValueRegistry.getInstance();
	static RegistryNamespaced registryNames = GameData.getItemRegistry();
	
	public static void addOreRangeForRange(String name, int start, int end, float value)
	{
		for(int i = start; i <= end; i++)
		{
			addOreRange(name,i,value);
		}
	}
	
	public static void addOreRange(String name, int damageValue, float value)
	{
		if(!registryNames.containsKey(name))
			return;
		ItemStack iStack = new ItemStack((Item) registryNames.getObject(name));
        Map<WrappedStack, EnergyValue> valuesPre = Helper.loadPre();
		iStack.setItemDamage(damageValue);
		int oreIDs [] = OreDictionary.getOreIDs(iStack);
		if(oreIDs.length == 0) return;
		
		for(int i : oreIDs)
		{
			WrappedStack w = WrappedStack.wrap(new OreStack(OreDictionary.getOreName(i)));
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
	}
	
	@Override
	public String getCommandName()
	{
		return "add-ore-range";
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
			int dmg;
			float value;
			
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs, "add-ore-range <emcvalue>, Uses held item");
					Helper.toChatErr(cs, "add-ore-range <id/name> <emcvalue> (DMG=0|N/A)");
					Helper.toChatErr(cs, "add-ore-range <id/name> <damagevalue> <emcvalue>");
					break;
				case 1:
					if(cs instanceof EntityPlayer)
					{
						EntityPlayer player = (EntityPlayer) cs;
						ItemStack iStack = player.getHeldItem();

						if(iStack != null)
						{
							name = registryNames.getNameForObject(iStack.getItem());
							value = Float.parseFloat(args[0]);
							dmg = iStack.getItemDamage();
							
							addOreRange(name,dmg,value);
							Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ORE RANGE FOR: "+name);
						}
						else
							Helper.toChatErr(cs, "(X) Invalid item.");
					}
					break;
				case 2:
					name = Helper.getItemName(args[0]);
					value = Float.parseFloat(args[1]);
					dmg = 0;
					
					if(name != null)
					{
						addOreRange(name,dmg,value);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ORE RANGE FOR: "+name);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid item.");
					break;
				case 3:
					name = Helper.getItemName(args[0]);
					dmg = Integer.parseInt(args[1]);
					value = Float.parseFloat(args[2]);
					
					if(name != null)
					{
						addOreRange(name,dmg,value);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) ORE RANGE FOR: "+name);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid item.");
					break;
				default:
					Helper.toChatErr(cs, "Invalid number of arguments for operation.");
					Helper.toChatErr(cs, "add-ore-range <emcvalue>, Uses held item");
					Helper.toChatErr(cs, "add-ore-range <id/name> <emcvalue> (DMG=0|N/A)");
					Helper.toChatErr(cs, "add-ore-range <id/name> <damagevalue> <emcvalue>");
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
