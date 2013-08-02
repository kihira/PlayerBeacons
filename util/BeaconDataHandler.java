package playerbeacons.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BeaconDataHandler {

	private NBTTagCompound beaconList = new NBTTagCompound();

	public BeaconDataHandler() {
		loadData();
	}

	private void loadData() {

		File mainFile = new File(DimensionManager.getCurrentSaveRootDirectory().toString() + "playerbeacons.dat");

		if (mainFile.exists()) {
			try {
				beaconList = CompressedStreamTools.readCompressed(new FileInputStream(mainFile));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void saveData(NBTTagCompound data) {
		try
		{
			File mainFileNew = new File(DimensionManager.getCurrentSaveRootDirectory().toString() + "playerbeacons.dat_new");
			File mainFile = new File(DimensionManager.getCurrentSaveRootDirectory().toString() + "playerbeacons.dat");
			File backupFile = new File(DimensionManager.getCurrentSaveRootDirectory().toString() + "playerbeacons.dat_old");
			CompressedStreamTools.writeCompressed(data, new FileOutputStream(mainFileNew));

			if (mainFile.exists())
			{
				mainFile.delete();
			}

			backupFile.renameTo(mainFile);

			if (backupFile.exists())
			{
				backupFile.delete();
			}

			mainFileNew.renameTo(backupFile);

			if (mainFileNew.exists())
			{
				mainFileNew.delete();
			}
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
	}

	public boolean updateBeaconInformation(World world, EntityPlayer player, int x, int y, int z, boolean isActive) {
		if (beaconList.hasKey(player.username)) {
			NBTTagCompound playerData = beaconList.getCompoundTag(player.username);
			if (playerData.hasKey("world" + world.getWorldInfo().getWorldName())) {
				NBTTagCompound playerDataWorld = playerData.getCompoundTag("world" + world.getWorldInfo().getWorldName());
				playerDataWorld.setInteger("x", x);
				playerDataWorld.setInteger("y", x);
				playerDataWorld.setInteger("z", x);
				playerDataWorld.setBoolean("inactive", isActive);
				playerData.setCompoundTag("world" + world.getWorldInfo().getWorldName(), playerDataWorld);
				saveData(beaconList);
				return true;
			}
		}
		return false;
	}

	public boolean deleteBeaconInformation(World world, EntityPlayer entityPlayer) {
		if (beaconList.hasKey(entityPlayer.username)) {
			NBTTagCompound playerData = beaconList.getCompoundTag(entityPlayer.username);
			if (playerData.hasKey("world" + world.getWorldInfo().getWorldName())) {
				playerData.removeTag("world" + world.getWorldInfo().getWorldName());
				saveData(beaconList);
				return true;
			}
		}
		return false;
	}

	public void addBeaconInformation(World world, EntityPlayer player, int x, int y, int z, boolean isActive) {
		if (beaconList.hasKey(player.username)) {
			NBTTagCompound playerData = beaconList.getCompoundTag(player.username);
			if (!playerData.hasKey("world" + world.getWorldInfo().getWorldName())) {
				NBTTagCompound playerDataWorld = new NBTTagCompound();
				playerDataWorld.setInteger("x", x);
				playerDataWorld.setInteger("y", x);
				playerDataWorld.setInteger("z", x);
				playerDataWorld.setBoolean("inactive", isActive);
				playerData.setCompoundTag("world" + world.getWorldInfo().getWorldName(), playerDataWorld);
				saveData(beaconList);
			}
		}
	}
}
