package com.rota.ee3help.commands;

import com.rota.ee3help.DataTracker;

import net.minecraft.command.ICommandSender;

public class CommandData extends CommandModifyBase
{
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

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		// data list
		// data import x
		// data export name
		// data export, creates random name.
		
		DataTracker.buildWorldList();
	}
}
