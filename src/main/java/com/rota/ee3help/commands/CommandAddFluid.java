package com.rota.ee3help.commands;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.api.exchange.EnergyValueRegistryProxy;
import com.pahimar.ee3.exchange.WrappedStack;
import com.rota.ee3help.Helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.fluids.FluidRegistry;

public class CommandAddFluid extends CommandModifyBase
{
	RegistryNamespaced registryNames = GameData.getItemRegistry();

	public CommandAddFluid()
	{
		name = "AddFluid";
		usage.add("AddFluid <fluidname> <emcvalue>");
		usage.add("AddFluid <fluidname> <emcvalue> <mB>");
	}
	
	private void addFluid(String name, float value, int mb)
	{
		WrappedStack w = WrappedStack.wrap(FluidRegistry.getFluid(name));
		EnergyValue e = new EnergyValue(value/1000);
		
        if (e != null && Float.compare(e.getValue(), 0) > 0)
        {
        	EnergyValueRegistryProxy.setEnergyValue(w, e, EnergyValueRegistryProxy.Phase.PRE_CALCULATION);
        }
	}
	
	@Override
	public String getCommandName()
	{
		return "AddFluid";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "Use command with no arguments.";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		try
		{
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs, getUsageString());
					break;
				case 2:
					if(FluidRegistry.isFluidRegistered((args[0])))
					{
						addFluid(args[0],Float.parseFloat(args[1]),0);
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) FLUID: "+args[0]);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid Fluid name.");
					break;
				case 3:
					if(FluidRegistry.isFluidRegistered((args[0])))
					{
						addFluid(args[0],Float.parseFloat(args[1]),Integer.parseInt(args[2]));
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(+) FLUID: "+args[0]);
					}
					else
						Helper.toChatErr(cs, "(X) Invalid Fluid name.");
					break;
				default:
					Helper.toChatErr(cs, getUsageString());
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
