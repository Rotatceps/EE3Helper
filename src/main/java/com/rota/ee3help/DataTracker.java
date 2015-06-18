package com.rota.ee3help;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.pahimar.ee3.util.SerializationHelper;

import net.minecraft.command.ICommandSender;

public class DataTracker
{
	private static HashMap<File,File> worldData = new HashMap<File,File>();
	
	private static ArrayList<File> worldDataList = new ArrayList<File>();
	private static ArrayList<File> userDataList = new ArrayList<File>();
	
	// Use this - SerializationHelper.readEnergyValueStackMapFromJsonFile
	
	public static void buildUserList()
	{
		
	}
	
	public static void buildWorldList()
	{
		worldData.clear();
		worldDataList.clear();
		
		File dir = new File(Helper.MC_SAVES_DIR);
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
							worldData.put(f, 
									new File(Helper.MC_SAVES_DIR + File.separator 
									+ f.getName() + File.separatorChar 
									+ Helper.EE3_ENERGYVALUES_DIR_OFFSET + File.separator 
									+ Helper.getEE3File("pre", "values")));
							break;
						}
					}
				}
			}
		}
		
		for(Map.Entry<File,File> entry : worldData.entrySet())
		{
			System.out.println(entry.getKey().exists() + ": " + entry.getKey().getAbsolutePath());
			System.out.println(entry.getValue().exists() + ": " + entry.getValue().getAbsolutePath());
			if(entry.getValue().exists())
				worldDataList.add(entry.getValue());
		}
	}
	
	public static void importData(ICommandSender cs, int index)
	{
		
	}
	
	public static void exportData(ICommandSender cs, String name)
	{
		
	}
}
