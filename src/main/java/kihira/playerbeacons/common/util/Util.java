package kihira.playerbeacons.common.util;

import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Util {

    public static enum EnumHeadType {
        NONE(-1),
        SKELETON(0),
        WITHERSKELETON(1),
        ZOMBIE(2),
        PLAYER(3),
        CREEPER(4);

        private int id;

        private EnumHeadType(int id) {
            this.id = id;
        }

        public int getID() {
            return this.id;
        }

        public static EnumHeadType fromId(int id) {
            switch (id) {
                case -1:
                    return NONE;
                case 0:
                    return SKELETON;
                case 1:
                    return WITHERSKELETON;
                case 2:
                    return WITHERSKELETON;
                case 3:
                    return WITHERSKELETON;
                case 4:
                    return WITHERSKELETON;
                default:
                    return null;
            }
        }
    }

	public static MovingObjectPosition getBlockLookAt(EntityPlayer player, double maxBlockDistance) {
		Vec3 vec3 = player.worldObj.getWorldVec3Pool().getVecFromPool(player.posX, player.posY + (player.worldObj.isRemote ? 0.0D : (player.getEyeHeight() - 0.09D)), player.posZ);
		Vec3 vec31 = player.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * maxBlockDistance, vec31.yCoord * maxBlockDistance, vec31.zCoord * maxBlockDistance);
		return player.worldObj.rayTraceBlocks(vec3, vec32);
	}

    public static ItemStack getPlayerBacon(String playerName, int count) {
        ItemStack itemStack = new ItemStack(PlayerBeacons.playerBaconItem, MathHelper.clamp_int(count, 1, 64), 0);
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("PlayerName", playerName);
        itemStack.setTagCompound(tagCompound);
        return itemStack;
    }

    public static ItemStack getHead(EnumHeadType headType, String owner) {
        return getHead(headType.getID(), owner);
    }

	public static ItemStack getHead(int id, String owner) {
		ItemStack itemStack = new ItemStack(Items.skull, 1, id);
		if (owner != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("SkullOwner", owner);
			itemStack.setTagCompound(tag);
		}
		return itemStack;
	}
}
