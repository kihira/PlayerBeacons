package playerbeacons.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import playerbeacons.util.Util;

import java.util.List;

public class CommandPlayerHead extends CommandBase {

	@Override
	public String getCommandName() {
		return "playerhead";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/playerhead <playername> | Playername is case sensitive!";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if ((astring.length > 0) && (astring[0].length() > 0)) {
			EntityPlayer player = icommandsender.getEntityWorld().getPlayerEntityByName(icommandsender.getCommandSenderName());
			if (player != null) {
				player.entityDropItem(Util.getHead(3, astring[0]), 1);
				notifyAdmins(icommandsender, "commands.playerhead.success", astring[0], player.username);
			}
		}
		else throw new WrongUsageException("commands.playerhead.usage", astring);
	}
}
