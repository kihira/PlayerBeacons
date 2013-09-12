package playerbeacons.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class CommandPlayerHead extends CommandBase {

	@Override
	public String getCommandName() {
		return "playerhead";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
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
				ItemStack itemStack = new ItemStack(Item.skull, 1, 3);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("SkullOwner", astring[0]);
				itemStack.setTagCompound(tag);
				player.entityDropItem(itemStack, 1);
				notifyAdmins(icommandsender, "commands.playerhead.success", astring[0], player.username);
			}
		}
		else
		{
			throw new WrongUsageException("commands.playerhead.usage", astring);
		}
	}
}
