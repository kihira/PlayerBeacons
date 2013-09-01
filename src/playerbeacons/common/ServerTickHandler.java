package playerbeacons.common;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.TickType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

public class ServerTickHandler implements IScheduledTickHandler {

	private short cycle = 0;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		cycle++;
		MinecraftServer mc = MinecraftServer.getServer();
		for (WorldServer worldServer : mc.worldServers) {
			if (worldServer.playerEntities != null) {
				List<Object> playerEntities = new ArrayList<Object>(worldServer.playerEntities);
				for (Iterator<Object> it = playerEntities.iterator(); it.hasNext();) {
					EntityPlayer entityPlayer = (EntityPlayer) it.next();
					NBTTagCompound nbtTagCompound = PlayerBeacons.beaconData.loadBeaconInformation(worldServer, entityPlayer.username);
					if (nbtTagCompound != null) {
						int x = nbtTagCompound.getInteger("x");
						int y = nbtTagCompound.getInteger("y");
						int z = nbtTagCompound.getInteger("z");
						TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) worldServer.getBlockTileEntity(x, y, z);
						if (tileEntityPlayerBeacon != null) {
							tileEntityPlayerBeacon.checkBeacon();
							if (cycle % 2 == 0) {
								if (!PlayerBeacons.config.disableCorruption) {
									tileEntityPlayerBeacon.calcPylons();
									tileEntityPlayerBeacon.calcCorruption();
									tileEntityPlayerBeacon.doCorruption(false);
								}
								if (tileEntityPlayerBeacon.hasSkull()) tileEntityPlayerBeacon.doEffects();
								if (cycle % 4 == 0) worldServer.markBlockForUpdate(x, y, z);
							}
							if (cycle >= 32000) cycle = 0;
						}
					}
				}
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) { }

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
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
