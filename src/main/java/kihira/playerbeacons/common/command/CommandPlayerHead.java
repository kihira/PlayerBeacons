package kihira.playerbeacons.common.command;

import kihira.foxlib.common.EnumHeadType;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandPlayerHead extends CommandBase {

	@Override
	public String getCommandName() {
		return "playerhead";
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) {
		return "commands.playerhead.usage";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender commandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] args) {
        EntityPlayer player = commandSender.getEntityWorld().getPlayerEntityByName(commandSender.getCommandSenderName());
        if (args != null && player != null) {
            if (args.length == 0) {
                player.entityDropItem(EnumHeadType.getHead(EnumHeadType.PLAYER, player.getCommandSenderName()), 1);
                func_152373_a(commandSender, this, "commands.playerhead.success", player.getCommandSenderName(), player.getCommandSenderName());
            }
            else if (args.length > 0 && args[0].length() > 0) {
                player.entityDropItem(EnumHeadType.getHead(EnumHeadType.PLAYER, args[0]), 1);
                func_152373_a(commandSender, this, "commands.playerhead.success", args[0], player.getCommandSenderName());
            }
            else throw new WrongUsageException("commands.playerhead.usage", args);
        }
        else throw new WrongUsageException("commands.playerhead.usage", args);
    }

    @Override
    public int compareTo(Object o) {
        return super.compareTo((ICommand) o);
    }
}
