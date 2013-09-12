package playerbeacons.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class Util {
	public static MovingObjectPosition getBlockLookAt(EntityPlayer player, double maxBlockDistance) {
		Vec3 vec3 = player.worldObj.getWorldVec3Pool().getVecFromPool(player.posX, player.posY + (player.worldObj.isRemote ? 0.0D : (player.getEyeHeight() - 0.09D)), player.posZ);
		Vec3 vec31 = player.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * maxBlockDistance, vec31.yCoord * maxBlockDistance, vec31.zCoord * maxBlockDistance);
		return player.worldObj.clip(vec3, vec32);
	}

	public static ItemStack getHead(int skullType, String owner) {
		ItemStack itemStack = new ItemStack(Item.skull, 1, skullType);
		if (owner != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("SkullOwner", owner);
			itemStack.setTagCompound(tag);
		}
		return itemStack;
	}
}
