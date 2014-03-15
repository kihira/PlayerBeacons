package kihira.playerbeacons.common;

import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;
import kihira.playerbeacons.util.Util;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;

import java.util.ArrayList;
import java.util.List;

public class CommandPlayerBeacons extends CommandBase {

	@Override
	public String getCommandName() {
		return "playerbeacons";
	}

	@Override
	public List getCommandAliases() {
		List<String> list = new ArrayList<String>();
		list.add("pb");
		list.add("playerbeacon");
		return list;
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "/playerbeacons <command> [args]";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {
		if (par2ArrayOfStr.length == 1) return getListOfStringsMatchingLastWord(par2ArrayOfStr, "setowner", "setcorruption");
		else if (par2ArrayOfStr.length == 2) {
			if (par2ArrayOfStr[0].toLowerCase().equals("setowner")) return getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames());
		}
		return null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if ((astring.length > 0) && (astring[0].length() > 0)) {
			EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
			MovingObjectPosition targetBlock = Util.getBlockLookAt(player, 4);
			if (player.worldObj.getBlock(targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ) == PlayerBeacons.playerBeaconBlock) {
				TileEntityPlayerBeacon tileEntity = (TileEntityPlayerBeacon) player.worldObj.getTileEntity(targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ);
				if (astring[0].toLowerCase().equals("setowner")) {
					if (astring.length == 2 && astring[1].length() <= 16) {
						if (PlayerBeacons.beaconData.loadBeaconInformation(player.worldObj, astring[1]) == null) {
							tileEntity.setOwner(astring[1]);
							notifyAdmins(icommandsender, "commands.playerbeacon.setowner.success", player.getCommandSenderName(), targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ, astring[1]);
						}
						else throw new CommandException("commands.playerbeacon.alreadyExists", astring[1]);
					}
					else throw new WrongUsageException("commands.playerbeacon.setowner.usage");
				}
				else if (astring[0].toLowerCase().equals("setcorruption")) {
					if (astring.length == 3 && !astring[1].isEmpty() && !astring[2].isEmpty()) {
						try {
							tileEntity.setCorruption(Float.valueOf(astring[1]), Boolean.getBoolean(astring[2]));
							notifyAdmins(icommandsender, "commands.playerbeacon.setcorruption.success", player.getCommandSenderName(), targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ, astring[1]);
						}
						catch (Exception e) {
							throw new WrongUsageException("commands.playerbeacon.setcorruption.usage");
						}
					}
					else throw new WrongUsageException("commands.playerbeacon.setcorruption.usage");
				}
			}
			else throw new WrongUsageException("commands.playerhead.notBeacon", targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ);
		}
		else throw new WrongUsageException("commands.playerhead.usage", (Object) astring);
	}

    @Override
    public int compareTo(Object o) {
        return super.compareTo((ICommand)o);
    }
}
