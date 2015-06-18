package com.rota.ee3help.commands;

import com.rota.ee3help.DataTracker;
import com.rota.ee3help.Helper;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;

public class CommandData extends CommandModifyBase
{
	final static int MODE_WORLD = 0;
	final static int MODE_USER = 1;
	
	@Override
	public String getCommandName()
	{
		return "data";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "data";
	}
	
	public void list(ICommandSender cs, int page)
	{
		DataTracker.buildList();
		DataTracker.listAccurate = true;
		
		int pageLength = 9;
		int pages = 0;
		
		pages = (int) Math.ceil(DataTracker.dataList.size()/(double) pageLength);
		
		if(page < 0)
		{
			Helper.toChat(cs, EnumChatFormatting.BLUE + "Pages: " + pages + ", list <page>");
			return;
		}

		if(pages == 0) { Helper.toChat(cs, EnumChatFormatting.GOLD + "No Entries."); return; }
		
		page = (page < 1) ? 1 : page;
		page = (page > pages) ? pages : page;
	
		Helper.toChat(cs, EnumChatFormatting.GRAY + "---- Page "+(page)+"/"+pages+" ----");
		int index = (page * pageLength)-pageLength;
		for(int i = index; (i < index + pageLength) && (i < DataTracker.dataList.size()); i++)
		{	
			Helper.toChat(cs, EnumChatFormatting.GOLD
					+ "(" +String.format("%4s", i)+") "
					+ DataTracker.data.get(DataTracker.dataList.get(i)));
		}
	}
	
	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		try
		{
			switch(args.length)
			{
				case 0:
					Helper.toChatErr(cs, "data list <page>");
					Helper.toChatErr(cs, "data import <index>");
					Helper.toChatErr(cs, "data export");
					break;
				case 1:
					if(args[0].equalsIgnoreCase("list"))
						list(cs,-1);
					else if(args[0].equalsIgnoreCase("import"))
					{
						Helper.toChatErr(cs, "data import <index>");
					}
					else if(args[0].equalsIgnoreCase("export"))
					{
						DataTracker.exportData();
						Helper.toChat(cs, EnumChatFormatting.GREEN + "(!) Exported Data");
					}
					break;
				case 2:
					if(args[0].equalsIgnoreCase("list"))
						list(cs, Integer.parseInt(args[1]));
					else if(args[0].equalsIgnoreCase("import"))
					{
						if(DataTracker.listAccurate)
						{
							DataTracker.importData(Integer.parseInt(args[1]));
							Helper.toChat(cs, EnumChatFormatting.GREEN + "(!) Imported Data");
						}
						else
							Helper.toChatErr(cs, "Indices may be innacurate, run 'data list' first. ");
					}
					break;
				default:
					Helper.toChatErr(cs, "Invalid number of arguments for operation.");
					Helper.toChatErr(cs, "data list <page>");
					Helper.toChatErr(cs, "data import <index>");
					Helper.toChatErr(cs, "data export");
			}
		}
		catch (NumberFormatException e)
		{
			Helper.toChatErr(cs, "Non-number in numeric field.");
		}
	}
}
