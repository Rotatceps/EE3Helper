package com.rota.ee3help;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.pahimar.ee3.api.exchange.EnergyValue;
import com.pahimar.ee3.exchange.WrappedStack;
import com.pahimar.ee3.reference.Reference;
import com.pahimar.ee3.util.SerializationHelper;

import cpw.mods.fml.common.FMLCommonHandler;

public class DataTracker
{
	public static final String EE3_ENERGYVALUES_DIR = 
			FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getSaveHandler().getWorldDirectory() 
			+ File.separator +"data" + File.separator 
			+ Reference.LOWERCASE_MOD_ID + File.separator 
			+ "energyvalues";

	public static final String EE3_ENERGYVALUES_DIR_OFFSET = 
			"data" + File.separator 
			+ Reference.LOWERCASE_MOD_ID 
			+ File.separator + "energyvalues";
	
	public static final String EE3H_USERDATA_DIR = "data" + File.separator + "ee3h" + File.separator + "userdata";
	public static final String MC_SAVES_DIR = "saves";
	
	private static HashMap<File,String> worldData = new HashMap<File,String>();
	private static ArrayList<File> worldDataList = new ArrayList<File>();
	private static ArrayList<File> userDataList = new ArrayList<File>();
	
	public static HashMap<File,String> data = new HashMap<File,String>();
	public static ArrayList<File> dataList = new ArrayList<File>();
	
	public static boolean listAccurate;
	
	// Use this - SerializationHelper.readEnergyValueStackMapFromJsonFile
	// Use this - SerializationHelper.writeEnergyValueStackMapToJsonFile
	
	public static void buildList()
	{
		dataList.clear();
		data.clear();
		
		buildUserList();
		buildWorldList();
		
		for(File f : worldDataList)
		{
			dataList.add(f);
			data.put(f,"WORLD: "+worldData.get(f));
		}
		
		for(File f : userDataList)
		{
			dataList.add(f);
			data.put(f,"USER: "+f.getName());
		}
		
		listAccurate = true;
	}
	
	public static void buildUserList()
	{
		userDataList.clear();
		
		File dir = new File(EE3H_USERDATA_DIR);
		if(!dir.exists())
			dir.mkdirs();
		
		if(dir.isDirectory())
		{
			for(File f : dir.listFiles())
			{
				if(f.getName().contains(".json"))
				{
					userDataList.add(f);
				}
			}
		}
	}
	
	public static void buildWorldList()
	{
		worldData.clear();
		worldDataList.clear();
		
		File dir = new File(MC_SAVES_DIR);
		if(!dir.exists())
			dir.mkdir();
		
		if(dir.isDirectory())
		{
			File [] folders = dir.listFiles();
			
			for(File f : folders)
			{
				if(f.isDirectory())
				{
					for(File internal : Arrays.asList(f.listFiles()))
					{
						if(internal.getName().contains("level.dat"))
						{
							worldData.put(new File(MC_SAVES_DIR + File.separator 
									+ f.getName() + File.separatorChar 
									+ EE3_ENERGYVALUES_DIR_OFFSET + File.separator 
									+ Helper.getEE3File("pre", "values"))
									,f.getName());

							break;
						}
					}
				}
			}
		}
		
		for(Map.Entry<File,String> entry : worldData.entrySet())
		{
			if(entry.getKey().exists())
				worldDataList.add(entry.getKey());
		}
	}
	
	public static boolean importData(int index)
	{
		if(!listAccurate) return false;

		try
		{
			dataList.get(index);
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return false;
		}
		Map<WrappedStack, EnergyValue> dataMap = SerializationHelper.readEnergyValueStackMapFromJsonFile(dataList.get(index));
		Helper.savePre(dataMap);
		
		listAccurate = false;
		return true;
	}
	
	public static void exportData()
	{
		File dir = new File(EE3H_USERDATA_DIR);
		if(!dir.exists())
			dir.mkdirs();
		
		if(dir.isDirectory())
		{
			Map<WrappedStack, EnergyValue> dataMap = Helper.loadPre();

			String folderName = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getSaveHandler().getWorldDirectoryName();
			File values = new File(dir + File.separator + "EMC-"+folderName.toUpperCase()+".json");
			
			if(values.exists())
				values.delete();
			
			SerializationHelper.writeEnergyValueStackMapToJsonFile(values, dataMap);
		}
		
		listAccurate = false;
	}
}
