package com.rota.ee3help;

import com.rota.ee3help.commands.CommandEE3H;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(name = EE3Help.MOD_NAME, modid = EE3Help.MOD_ID, version = EE3Help.VERSION, acceptableRemoteVersions = "*")
public class EE3Help 
{	
	public static final String MOD_NAME = "EE3 Helper";
	public static final String MOD_ID = "EE3HELP";
	public static final String VERSION = "2.2a";
	
	public static boolean listAccurate = false;
	public static Configuration config;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) 
	{
		config = new Configuration();
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) 
	{
		if (!Loader.isModLoaded("EE3")) 
			return;

		event.registerServerCommand(new CommandEE3H());
	}
}

