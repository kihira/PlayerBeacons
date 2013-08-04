package playerbeacons.common;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.EnumSet;

public class ServerTickHandler implements IScheduledTickHandler {

	private short cycle = 0;
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		MinecraftServer mc = MinecraftServer.getServer();
		for (WorldServer worldServer : mc.worldServers) {
			for (Object object : worldServer.playerEntities) {
				EntityPlayer entityPlayer = (EntityPlayer) object;
				NBTTagCompound nbtTagCompound = PlayerBeacons.beaconData.loadBeaconInformation(worldServer, entityPlayer.username);
				if (nbtTagCompound != null) {
					int x = nbtTagCompound.getInteger("x");
					int y = nbtTagCompound.getInteger("y");
					int z = nbtTagCompound.getInteger("z");
					TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) worldServer.getBlockTileEntity(x, y, z);
					if (tileEntityPlayerBeacon != null) {
						tileEntityPlayerBeacon.calcLevels();
						if (cycle == 1) {
							tileEntityPlayerBeacon.calcPylons();
							tileEntityPlayerBeacon.doCorruption();
						}
					}
				}
			}
		}
		cycle++;
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.PLAYER);
	}

	@Override
	public String getLabel() {
		return "serverplayerbeacon";
	}

	@Override
	public int nextTickSpacing() {
		return 20;
	}
}
