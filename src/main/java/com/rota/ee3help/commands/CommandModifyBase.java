package com.rota.ee3help.commands;

import java.util.ArrayList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandModifyBase extends CommandBase
{
	protected ArrayList<String> usage = new ArrayList<String>();
	protected String name = "";
	public CommandModifyBase()
	{
		name = "CommandModifyBase";
	}
	public String getUsageString()
	{
		return String.join("\n", usage);
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public String getCommandName()
	{
		return name;
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return getUsageString();
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{

	}
}
