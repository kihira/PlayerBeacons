package playerbeacons.util;

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
			File mainFileNew = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat_new");
			File backupFile = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat_old");
			File mainFile = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat");
			System.out.println(mainFileNew);
			System.out.println(mainFile);
			System.out.println(backupFile);
			CompressedStreamTools.writeCompressed(data, new FileOutputStream(mainFileNew));

			if (backupFile.exists())
			{
				backupFile.delete();
			}

			mainFile.renameTo(backupFile);

			if (mainFile.exists())
			{
				mainFile.delete();
			}

			mainFileNew.renameTo(mainFile);

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

	public boolean updateBeaconInformation(World world, String player, int x, int y, int z, boolean isActive, int badStuff) {
		if (beaconList.hasKey(player)) {
			NBTTagCompound playerData = beaconList.getCompoundTag(player);
			if (playerData.hasKey("world" + world.getWorldInfo().getWorldName())) {
				NBTTagCompound playerDataWorld = playerData.getCompoundTag("world" + world.getWorldInfo().getWorldName());
				playerDataWorld.setInteger("x", x);
				playerDataWorld.setInteger("y", y);
				playerDataWorld.setInteger("z", z);
				playerDataWorld.setBoolean("inactive", isActive);
				playerDataWorld.setInteger("badstuff", badStuff);
				playerData.setCompoundTag("world" + world.getWorldInfo().getWorldName(), playerDataWorld);
				saveData(beaconList);
				return true;
			}
		}
		return false;
	}

	public boolean deleteBeaconInformation(World world, String username) {
		if (beaconList.hasKey(username)) {
			NBTTagCompound playerData = beaconList.getCompoundTag(username);
			if (playerData.hasKey("world" + world.getWorldInfo().getWorldName())) {
				playerData.removeTag("world" + world.getWorldInfo().getWorldName());
				saveData(beaconList);
				return true;
			}
		}
		return false;
	}

	public void addBeaconInformation(World world, String player, int x, int y, int z, boolean isActive, int badStuff) {
		if (!beaconList.hasKey(player)) {
			NBTTagCompound newPlayerData = new NBTTagCompound();
			beaconList.setCompoundTag(player, newPlayerData);
		}
		if (beaconList.hasKey(player)) {
			NBTTagCompound playerData = beaconList.getCompoundTag(player);
			if (!playerData.hasKey("world" + world.getWorldInfo().getWorldName())) {
				NBTTagCompound playerDataWorld = new NBTTagCompound();
				playerDataWorld.setInteger("x", x);
				playerDataWorld.setInteger("y", y);
				playerDataWorld.setInteger("z", z);
				playerDataWorld.setBoolean("inactive", isActive);
				playerDataWorld.setInteger("badstuff", badStuff);
				playerData.setCompoundTag("world" + world.getWorldInfo().getWorldName(), playerDataWorld);
				saveData(beaconList);
			}
		}
	}
}
