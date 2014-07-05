package kihira.playerbeacons.common.util;

import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;

import java.util.Iterator;
import java.util.UUID;

public class Util {

    public static ItemStack getPlayerBacon(String playerName, int count) {
        ItemStack itemStack = new ItemStack(PlayerBeacons.playerBaconItem, MathHelper.clamp_int(count, 1, 64), 0);
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("PlayerName", playerName);
        itemStack.setTagCompound(tagCompound);
        return itemStack;
    }

    //TODO move to util
    public static EntityPlayerMP getPlayerFromUUID(UUID uuid) {
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
