package com.rota.ee3help.commands;

import com.pahimar.ee3.exchange.EnergyValueRegistry;
import com.pahimar.ee3.network.PacketHandler;
import com.pahimar.ee3.network.message.MessageSyncEnergyValues;
import com.pahimar.ee3.util.LogHelper;
import com.pahimar.ee3.util.PlayerHelper;
import com.rota.ee3help.EE3Help;
import com.rota.ee3help.Helper;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumChatFormatting;

public class CommandSync extends CommandModifyBase
{
	@Override
	public String getCommandName()
	{
		return "sync";
	}

	@Override
	public String getCommandUsage(ICommandSender cs)
	{
		return "sync <self>, sync <all>";
	}

	@Override
	public void processCommand(ICommandSender cs, String[] args)
	{
		if (args[0].equalsIgnoreCase("self"))
		{
			if(EE3Help.config.allow_nonopsync)
			{
				LogHelper.info(String.format("Syncing EnergyValues with player '%s' at their request", cs.getCommandSenderName()));
				PacketHandler.INSTANCE.sendTo(new MessageSyncEnergyValues(EnergyValueRegistry.getInstance()), (EntityPlayerMP) cs);
				Helper.toChat(cs, EnumChatFormatting.GREEN + "Synced self with server.");
			}
			else
				Helper.toChatErr(cs, "Non-OP Sync is disabled in this server's configuration.");
		}
		else if (args[0].equalsIgnoreCase("all") && PlayerHelper.isPlayerOp((EntityPlayer) cs))
		{
			LogHelper.info(String.format("Syncing EnergyValues with all players at %s's request", cs.getCommandSenderName()));
			PacketHandler.INSTANCE.sendToAll(new MessageSyncEnergyValues(EnergyValueRegistry.getInstance()));
			Helper.toChat(cs, EnumChatFormatting.GREEN + "Synced all with server.");
		}
		else
		{
			Helper.toChatErr(cs, "Invalid Parameter");
			Helper.toChatErr(cs, "sync self");
			Helper.toChatErr(cs, "sync all");
		}
	}
}
