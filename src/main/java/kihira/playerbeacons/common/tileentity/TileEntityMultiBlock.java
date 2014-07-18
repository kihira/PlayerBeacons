package kihira.playerbeacons.common.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityMultiBlock extends TileEntity {

    public int parentX;
    public int parentY;
    public int parentZ;

    public boolean hasParent;
    public boolean isParent;

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.parentX = nbtTagCompound.getInteger("parentX");
        this.parentY = nbtTagCompound.getInteger("parentY");
        this.parentZ = nbtTagCompound.getInteger("parentZ");

        this.hasParent = nbtTagCompound.getBoolean("hasParent");
        this.isParent = nbtTagCompound.getBoolean("isParent");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setInteger("parentX", this.parentX);
        nbtTagCompound.setInteger("parentY", this.parentY);
        nbtTagCompound.setInteger("parentZ", this.parentZ);

        nbtTagCompound.setBoolean("hasParent", this.hasParent);
        nbtTagCompound.setBoolean("isParent", this.isParent);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound tag = new NBTTagCompound();
        this.writeToNBT(tag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
    }

    /**
     * Sets this TE as a parent
     */
    public void setIsParent() {
        this.isParent = true;
    }

    /**
     * Sets the parent for this TE
     * @param parentX The parent x co-ord
     * @param parentY The parent y co-ord
     * @param parentZ The parent z co-ord
     */
    public void setParent(int parentX, int parentY, int parentZ) {
        this.parentX = parentX;
        this.parentY = parentY;
        this.parentZ = parentZ;

        this.hasParent = true;
        this.isParent = false;
    }

    /**
     * Sets the parent for this TE
     * @param tileEntity The parent TE
     */
    public void setParent(TileEntityMultiBlock tileEntity) {
        if (tileEntity != null) this.setParent(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        else {
            this.setParent(0, 0, 0);
            this.hasParent = false;
        }
    }

    /**
     * Check parent is valid
     * @return If parent exists
     */
    public boolean isParentValid() {
        if (this.isParent) return true;
        if (this.hasParent) {
            TileEntity block = this.worldObj.getTileEntity(this.parentX, this.parentY, this.parentZ);
            if (block instanceof TileEntityMultiBlock) return true;
        }
        return false;
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(this.getClass()) + "[hasParent: " + this.hasParent + ", isParent: " + this.isParent + ", parentX: " + this.parentX + ", parentY: " + this.parentY + ", parentZ: " + this.parentZ + "]";
    }
}
