package mcp.mobius.waila.api;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract interface IWailaDataAccessor {

    public abstract World getWorld();

    public abstract EntityPlayer getPlayer();

    public abstract Block getBlock();

    public abstract int getMetadata();

    public abstract TileEntity getTileEntity();

    public abstract MovingObjectPosition getPosition();

    public abstract Vec3 getRenderingPosition();

    public abstract NBTTagCompound getNBTData();

    public abstract int getNBTInteger(NBTTagCompound paramNBTTagCompound, String paramString);

    public abstract double getPartialFrame();

    public abstract ForgeDirection getSide();
}