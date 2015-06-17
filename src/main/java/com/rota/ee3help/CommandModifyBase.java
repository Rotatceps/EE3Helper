package com.rota.ee3help;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandModifyBase extends CommandBase
{
	public void resetFlag()
	{
		EE3Help.listAccurate = false;
	}
	
	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	
	@Override
	public String getCommandName()
	{
		return "CMD-MODIFY";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "You dont.";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{

	}
}
