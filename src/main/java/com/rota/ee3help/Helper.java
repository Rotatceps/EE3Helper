package com.rota.ee3help;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;

public class Helper
{	
	public static String getItemName(String s)
	{
		// If this name is a number we parse the number then check the item registry for an item with that ID.
		// If the ID is valid we'll get the name, if not we'll get a null.
		// If the name isn't a number just return the name again.
		
		try
		{
			RegistryNamespaced rn = GameData.getItemRegistry();
			int id = Integer.parseInt(s);
			
			if(rn.containsId(id))
				return rn.getNameForObject(rn.getObjectById(id));
			else
				return null;
		}
		catch (NumberFormatException e) {}
		return s;
	}
	
	public static void toChat(ICommandSender cs, String msg)
	{
		for(String s : msg.split("\n"))
		{
			cs.addChatMessage(new ChatComponentTranslation(s));	
		}
	}
	
	public static void toChatErr(ICommandSender cs, String msg)
	{
		for(String s : msg.split("\n"))
		{
			cs.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.RED + s));	
		}	
	}
}
