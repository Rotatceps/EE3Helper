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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

public class CommandAddOre extends CommandModifyBase
{
	EnergyValueRegistry registryValues = EnergyValueRegistry.getInstance();
	RegistryNamespaced registryNames = GameData.getItemRegistry();

	private void addOre(String name, float value)
	{
        Map<WrappedStack, EnergyValue> valuesPre = Helper.loadPre();

		WrappedStack w = WrappedStack.wrap(new OreStack(name));
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
	}
	
	@Override
	public String getCommandName()
	{
		return "add-ore";
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
					Helper.toChatErr(cs, "add-ore <orename> <emcvalue>");
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
					Helper.toChatErr(cs, "add-ore <orename> <emcvalue>");
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
