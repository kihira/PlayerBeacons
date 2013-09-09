package playerbeacons.common;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.ArrayList;
import java.util.List;

public class CommandPlayerBeacons extends CommandBase {

	@Override
	public String getCommandName() {
		return "playerbeacon";
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
		return "/playerbeacon <command> [args]";
	}

	@Override
	public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr) {

		return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getAllUsernames()) : null;
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if ((astring.length > 0) && (astring[0].length() > 0)) {
			EntityPlayer player = getCommandSenderAsPlayer(icommandsender);
			MovingObjectPosition targetBlock = getBlockLookAt(player, 4);
			System.out.println(targetBlock.blockX + " " + targetBlock.blockY + " " + targetBlock.blockZ);
			if (player.worldObj.getBlockId(targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ) == PlayerBeacons.config.playerBeaconBlockID) {
				System.out.println("Got a player beacon!");
				TileEntityPlayerBeacon tileEntity = (TileEntityPlayerBeacon) player.worldObj.getBlockTileEntity(targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ);
				if (astring[0].toLowerCase().equals("setowner")) {
					if (astring.length == 2 && astring[1].length() <= 16) {
						if (PlayerBeacons.beaconData.loadBeaconInformation(player.worldObj, astring[1]) == null) {
							tileEntity.setOwner(astring[1]);
							notifyAdmins(icommandsender, "commands.playerbeacon.setowner.success", player.username, targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ, astring[1]);
						}
						else {
							System.out.println(PlayerBeacons.beaconData.loadBeaconInformation(player.worldObj, astring[1]));
							throw new CommandException("commands.playerbeacon.alreadyExists", astring[1]);
						}
					}
					else throw new WrongUsageException("commands.playerbeacon.setowner.usage");
				}
				else if (astring[0].toLowerCase().equals("setcorruption")) {
					if (!astring[1].isEmpty() && !astring[2].isEmpty()) {
						try {
							tileEntity.setCorruption(Float.valueOf(astring[1]), Boolean.getBoolean(astring[2]));
							notifyAdmins(icommandsender, "commands.playerbeacon.setcorruption.success", targetBlock.blockX, targetBlock.blockY, targetBlock.blockZ, astring[1], astring[2]);
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

	private MovingObjectPosition getBlockLookAt(EntityPlayer player, double maxBlockDistance) {
		Vec3 vec3 = player.worldObj.getWorldVec3Pool().getVecFromPool(player.posX, player.posY + (player.worldObj.isRemote ? 0.0D : (player.getEyeHeight() - 0.09D)), player.posZ);
		Vec3 vec31 = player.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * maxBlockDistance, vec31.yCoord * maxBlockDistance, vec31.zCoord * maxBlockDistance);
		return player.worldObj.clip(vec3, vec32);
	}
}
