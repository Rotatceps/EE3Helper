package com.rota.ee3help.commands;

import com.rota.ee3help.Helper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.oredict.OreDictionary;

public class CommandIDItem extends CommandBase
{
	// Prints all of a given item's identifiable names to chat.
	// ITEM ID:DAMAGE VALUE
	// UNLOCALIZED NAME
	// ORE DICTIONARY NAMES
	
	@Override
	public boolean canCommandSenderUseCommand(ICommandSender cs)
	{
		return true;
	}
	
	@Override
	public String getCommandName()
	{
		return "IdItem";
	}

	@Override
	public String getCommandUsage(ICommandSender cs) 
	{
		return
			"IdItem (while holding an item)\n" +
			"IdItem <name/id>\n" +
			"IdItem <name/id> <dmg>";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args) 
	{
		RegistryNamespaced rn = GameData.getItemRegistry();
		if(cs instanceof EntityPlayer)
		{	
			EntityPlayer player = (EntityPlayer) cs;
			
			try
			{
				Item i = null;
				ItemStack iStack = null;
				
				switch(args.length)
				{
					case 0:
						i = (player.getHeldItem() != null) ? player.getHeldItem().getItem() : null; break;
					case 1:
						i = (Item) rn.getObjectById(Integer.parseInt(args[0])); break;
					case 2: 
						i = (Item) rn.getObjectById(Integer.parseInt(args[0])); break;
				}
				
				if(i == null)
				{
					Helper.toChatErr(cs, "No held item, or no such item.");
					Helper.toChatErr(cs, getCommandUsage(cs));
					return;
				}

				iStack = new ItemStack(i);
				int dmg = 0;
				
				switch(args.length)
				{
					case 0:
						iStack.setItemDamage(player.getHeldItem().getItemDamage()); 
						dmg = iStack.getItemDamage();
						break;
					case 1:
						iStack.setItemDamage(dmg); 
						break;
					case 2: 
						dmg = Integer.parseInt(args[1]);
						iStack.setItemDamage(dmg); 
						break;
				}

				if(iStack != null)
				{
					Helper.toChat(cs, EnumChatFormatting.GRAY + "--");
					Helper.toChat(cs, EnumChatFormatting.AQUA + "NNAME: "+ rn.getIDForObject(i) + ":" + dmg);
					Helper.toChat(cs, EnumChatFormatting.AQUA + "UNAME: "+ rn.getNameForObject(i));
					
					int [] oIDs = OreDictionary.getOreIDs(iStack);
					
					for(int j = 0; j < oIDs.length; j++)
					{
						Helper.toChat(cs, EnumChatFormatting.AQUA + "ONAME: "+OreDictionary.getOreName(oIDs[j]));
					}
				}
			}
			catch(NumberFormatException e)
			{
				Helper.toChatErr(cs, "Non-number in numeric field.");
				Helper.toChatErr(cs, getCommandUsage(cs));
			}
		}
	}
}
