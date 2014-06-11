package kihira.playerbeacons.api;

import kihira.playerbeacons.api.beacon.IBeacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class BeaconDataHelper {

    public static float getPlayerCorruptionAmount(EntityPlayer player) {
        NBTTagCompound data = getBeaconDataTag(player);
        float corruptionAmount = 0;

        if (data.hasKey("Corruption")) {
            corruptionAmount = data.getFloat("Corruption");
        }

        return corruptionAmount;
    }

    public static void modifyCorruptionAmount(EntityPlayer player, float corrChange) {
        float corr = getPlayerCorruptionAmount(player);
        corr += corrChange;
        setPlayerCorruptionAmount(player, corr);
    }

    public static void setPlayerCorruptionAmount(EntityPlayer player, float corr) {
        NBTTagCompound data = getBeaconDataTag(player);

        data.setFloat("Corruption", Math.max(0, corr));
    }

    public static boolean doesPlayerHaveBeaconForDim(EntityPlayer player, int dimID) {
        NBTTagCompound beaconData = getBeaconDataTag(player);
        return beaconData.hasKey(String.valueOf(dimID));
    }

    public static void setBeaconForDim(EntityPlayer player, IBeacon playerBeacon, int dimID) {
        if (player != null) {
            String dimKey = String.valueOf(dimID);
            NBTTagCompound beaconData = getBeaconDataTag(player);
            NBTTagCompound worldBeaconData = beaconData.getCompoundTag(dimKey);
            if (playerBeacon != null) {
                worldBeaconData.setInteger("xPos", playerBeacon.getTileEntity().xCoord);
                worldBeaconData.setInteger("yPos", playerBeacon.getTileEntity().yCoord);
                worldBeaconData.setInteger("zPos", playerBeacon.getTileEntity().zCoord);

                //Set tag just incase it didn't alredy exist
                beaconData.setTag(dimKey, worldBeaconData);
            }
            else {
                beaconData.removeTag(dimKey);
            }
        }
    }

    public static IBeacon getBeaconForDim(EntityPlayer player, int dimID) {
        if (player != null) {
            NBTTagCompound beaconData = getBeaconDataTag(player);
            String dimKey = String.valueOf(dimID);

            if (beaconData.hasKey(dimKey)) {
                NBTTagCompound worldBeaconData = beaconData.getCompoundTag(dimKey);
                int x = worldBeaconData.getInteger("xPos");
                int y = worldBeaconData.getInteger("yPos");
                int z = worldBeaconData.getInteger("zPos");
                TileEntity tileEntity = player.worldObj.getTileEntity(x, y, z);
                if (tileEntity instanceof IBeacon) return (IBeacon) tileEntity;
                else {
                    setBeaconForDim(player, null, dimID);
                }
            }
        }
        return null;
    }

    private static NBTTagCompound getBeaconDataTag(EntityPlayer player) {
        NBTTagCompound forgeData = player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        NBTTagCompound beaconData = forgeData.getCompoundTag("PlayerBeacons");

        //Creates/sets the tags if they don't exist
        if (!forgeData.hasKey("PlayerBeacons")) forgeData.setTag("PlayerBeacons", beaconData);
        if (!player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG)) player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, forgeData);

        return beaconData;
    }
}