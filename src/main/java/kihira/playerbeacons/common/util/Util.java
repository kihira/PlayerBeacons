package kihira.playerbeacons.common.util;

import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;

import java.util.Iterator;
import java.util.UUID;

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
                case 0:
                    return SKELETON;
                case 1:
                    return WITHERSKELETON;
                case 2:
                    return ZOMBIE;
                case 3:
                    return PLAYER;
                case 4:
                    return CREEPER;
                default:
                    return NONE;
            }
        }
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

    public static EntityPlayerMP getPlayerFromUUID(String uuidString) {
        UUID uuid = UUID.fromString(uuidString);

        Iterator iterator = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
        EntityPlayerMP entityplayermp;

        do {
            if (!iterator.hasNext()) return null;
            entityplayermp = (EntityPlayerMP)iterator.next();
        }
        while (!entityplayermp.getUniqueID().equals(uuid));

        return entityplayermp;
    }
}
