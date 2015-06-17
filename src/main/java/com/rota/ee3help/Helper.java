package com.rota.ee3help;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.reference.Files;
import com.pahimar.ee3.reference.Reference;
import com.pahimar.ee3.util.SerializationHelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.RegistryNamespaced;

public class Helper
{	
	public static final String EE3_ENERGYVALUES_DIR = 
			FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getSaveHandler().getWorldDirectory() 
			+ File.separator +"data" + File.separator + Reference.LOWERCASE_MOD_ID + File.separator + "energyvalues";
	
	public static boolean createPre()
	{
		File dir = new File(EE3_ENERGYVALUES_DIR);
		if(!dir.exists())
			dir.mkdirs();
		
		File values = new File(EE3_ENERGYVALUES_DIR + File.separator + getEE3File("pre","values"));
		if(!values.exists())
		{
			try 
			{ 
				values.createNewFile();
				PrintWriter pw = new PrintWriter(values);
				pw.println("[]");
				pw.close();
			}
			catch (IOException e) 
			{ 
				return false;
			}
		}
		return true;
	}
	
	public static void savePre(Map<WrappedStack, EnergyValue> valuesPre)
	{
		if(!createPre())
		{
			FMLLog.getLogger().error("PRE...VALUES was not found, and could not be created.");
			return;
		}
		
		SerializationHelper.writeEnergyValueStackMapToJsonFile(getEE3File("pre","values"), valuesPre);
	}
	
	public static Map<WrappedStack, EnergyValue> loadPre()
	{
		if(!createPre())
		{
			FMLLog.getLogger().error("PRE...VALUES was not found, and could not be created.");
			return null;
		}
		
		return SerializationHelper.readEnergyValueStackMapFromJsonFile(getEE3File("pre","values"));
	}
	
	public static String getEE3File(String fieldStart, String fieldEnd)
	{
		// Because PRE_ASSIGNED_ENERGY_VALUES was changed to PRE_CALCULATED_ENERGY_VALUES
		// So this should work for versions before and after that change.
		
		try
		{
			for(Field f : Files.class.getFields())
			{
				if(f.getName().toLowerCase().startsWith(fieldStart) && f.getName().toLowerCase().endsWith(fieldEnd));
				{
					f.setAccessible(true);
						if(f.getType().equals(new String().getClass()))
								 return (String) f.get(new Files());
				}
			}
		}
		catch (IllegalArgumentException e){}
		catch (IllegalAccessException e){}
		
		throw new RuntimeException("EE3 Helper: Uninstall this mod, it is fundamentally broken and must be updated.");
	}

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
		cs.addChatMessage(new ChatComponentTranslation(msg));
	}
	
	public static void toChatErr(ICommandSender cs, String msg)
	{
		cs.addChatMessage(new ChatComponentTranslation(EnumChatFormatting.RED + msg));
	}
}
