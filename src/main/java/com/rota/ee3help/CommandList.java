package com.rota.ee3help;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.OreStack;
import com.pahimar.ee3.exchange.WrappedStack;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

public class CommandList extends CommandModifyBase
{
	public ArrayList<WrappedStack> entries = new ArrayList<WrappedStack>();
	Map<WrappedStack, EnergyValue> valuesPre;
	
	private void buildList()
	{
		entries.clear();
        valuesPre = Helper.loadPre();
        
        for(Map.Entry<WrappedStack, EnergyValue> entry : valuesPre.entrySet())
        {
        	entries.add(entry.getKey());
        }
        Collections.sort(entries);
	}
	
	private String getInfo(WrappedStack w)
	{
		try
		{
			String name;
			Object wrappedStack = w.getWrappedObject();
			
			Field fieldObjectType = w.getClass().getDeclaredField("objectType");
			Field fieldStackSize = w.getClass().getDeclaredField("stackSize");
			
			fieldObjectType.setAccessible(true);
			fieldStackSize.setAccessible(true);

			String objectType = (String) fieldObjectType.get(w);
			int stackSize = (Integer) fieldStackSize.get(w);
			
			if (wrappedStack instanceof ItemStack)
			{
				ItemStack itemStack = (ItemStack) wrappedStack;
				name = GameData.getItemRegistry().getNameForObject(itemStack.getItem())+":"+itemStack.getItemDamage();				
			} 
			else if (wrappedStack instanceof OreStack)
			{
				OreStack oreStack = (OreStack) wrappedStack;
				name = oreStack.oreName;
			} 
			else if (wrappedStack instanceof FluidStack)
			{
				FluidStack fluidStack = (FluidStack) wrappedStack;
				name = fluidStack.getFluid().getName();
			} 
			else
			{
				name = "null-wrappedstack";
			}
			
			return objectType + " " + name;
		} 
		catch (NoSuchFieldException e)
		{
			FMLLog.getLogger().error("WrappedStack has been updated, this field name is invalid: " + e.getMessage());
		} 
		catch (Exception e)
		{
			FMLLog.getLogger().error("Could not access field: " + e.getMessage());
		} 

		return "PARSE_ERROR:CHECKLOG";
	}
	
	@Override
	public String getCommandName()
	{
		return "list";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "list, list <page>";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		buildList();
		
		EE3Help.listAccurate = true;
		
		int pageLength = 9;
		int pages = 0;
		
		pages = (int) Math.ceil(entries.size()/(double) pageLength);
		
		if(args.length == 0)
		{
			Helper.toChat(cs, EnumChatFormatting.AQUA + "Pages: " + pages + ", list <page>");
		}
		
		else if(args.length >= 1)
		{
			if(pages == 0) { Helper.toChat(cs, EnumChatFormatting.GOLD + "No Entries."); return; }
			int page = Integer.parseInt(args[0]);
			
			page = (page < 1) ? 1 : page;
			page = (page > pages) ? pages : page;
		
			Helper.toChat(cs, EnumChatFormatting.GRAY + "---- Page "+(page)+"/"+pages+" ----");
			int index = (page * pageLength)-pageLength;
			for(int i = index; (i < index + pageLength) && (i < entries.size()); i++)
			{	
				Helper.toChat(cs, EnumChatFormatting.GOLD
						+ "(" +String.format("%4s", i)+") "
						+ getInfo(entries.get(i)) + " "
						+ EnumChatFormatting.BLUE + "EMC: " 
						+ EnumChatFormatting.RED + valuesPre.get(entries.get(i)));
			}
		}
	}
}
