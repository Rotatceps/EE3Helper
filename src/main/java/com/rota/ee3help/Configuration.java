package com.rota.ee3help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;

import cpw.mods.fml.common.FMLLog;

public class Configuration
{
	public static final String CONFIG_DIR = "config";
	public static final String CONFIG_NAME = "ee3h.cfg";
	
	File dir = new File(CONFIG_DIR);
	File config = new File(CONFIG_DIR + File.separator + CONFIG_NAME);
	
	public static final String DESCRIPTION_AUTO_OREDICT = "Automatically add all Ore Dictionary entries for added items.";
	public static final String DESCRIPTION_ALLOW_NONOPSYNC = "Allow non-op players to request a sync.";
	public boolean auto_oredict;
	public boolean allow_nonopsync;
	
	
	public Configuration()
	{
		dir.mkdirs();
		
		if(!config.exists())
		{
			setDefault();
			writeConfig();
			return;
		}
		
		readConfig();
	}
	
	public void setDefault()
	{
		auto_oredict = true;
		allow_nonopsync = true;
	}
	
	public void writeConfig()
	{
		try
		{
			PrintWriter pw = new PrintWriter(config);
			
			pw.println("# If you don't see all of the options, delete this file and they should regenerate.");
			pw.println("# If you must make comments in this file, ensure the that line begins with a '#'\n");
			
			pw.println("# " + DESCRIPTION_AUTO_OREDICT);
			pw.print("auto_oredict = " + auto_oredict + "\n");
			
			pw.println("# " + DESCRIPTION_ALLOW_NONOPSYNC);
			pw.print("auto_oredict = " + allow_nonopsync + "\n");
			
			pw.close();
		}
		catch (IOException e)
		{
			FMLLog.getLogger().error(e.getMessage() + "\n" + e.getStackTrace().toString());
		}
	}
	
	public void readConfig()
	{
		ArrayList<String> lines = new ArrayList<String>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(config));
			
			String line = null;
			while((line = br.readLine()) != null)
			{
				line = line.trim();
				if(line.startsWith("#")) continue;
				if(line.isEmpty()) continue;
				
				lines.add(line.replaceAll("\\s", "").toLowerCase());
			}
			
			for(String s : lines)
			{
				String[] splitS = s.split("=");
				if(splitS.length != 2) continue;
				
				try
				{
					Field f = this.getClass().getField(splitS[0]);
					if(f.getType() == Boolean.TYPE)
					{
						f.set(this, Boolean.parseBoolean(splitS[1]));
					}
					
					// We only have boolean for now.
				}
				catch (NoSuchFieldException e)
				{
					// Nonsense yet syntactically correct entry.
					
					FMLLog.getLogger().error("Bogus entry in config. Regenerate/correct ee3h.cfg.");
					setDefault();
				}
				catch (IllegalArgumentException e)
				{
					// Could not parse value.
					
					FMLLog.getLogger().error("Non-boolean value for boolean configuration");
					setDefault();
				}
				catch (SecurityException e){FMLLog.getLogger().error(e.getMessage()); setDefault();}
				catch (IllegalAccessException e){FMLLog.getLogger().error(e.getMessage()); setDefault();}
			}
			br.close();
		}
		catch (IOException e)
		{
			FMLLog.getLogger().error(e.getMessage() + "\n" + e.getStackTrace().toString());
			setDefault();
		}
	}
}
