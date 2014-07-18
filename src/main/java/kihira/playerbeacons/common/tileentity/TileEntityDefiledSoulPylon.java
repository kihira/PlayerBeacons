package kihira.playerbeacons.common.tileentity;

import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.api.crystal.ICrystalContainer;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityDefiledSoulPylon extends TileEntityMultiBlock implements ICrystalContainer {

	private ItemStack crystal;

	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound) {
		super.readFromNBT(nbtTagCompound);
		NBTTagCompound tag = (NBTTagCompound) nbtTagCompound.getTag("crystal");
		this.crystal = ItemStack.loadItemStackFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbtTagCompound) {
		super.writeToNBT(nbtTagCompound);
		NBTTagCompound tag = new NBTTagCompound();
		if (this.crystal != null) this.crystal.writeToNBT(tag);
        nbtTagCompound.setTag("crystal", tag);
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

    @Override
    public boolean canUpdate() {
        return false;
    }

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i == 0) return this.crystal;
		else return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
        if (i == 0) {
            return this.crystal;
        }
        return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i == 0) {
			this.crystal = itemstack;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, PlayerBeacons.defiledSoulPylonBlock);
		}
	}

	@Override
	public String getInventoryName() {
		return "Pylon";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return false;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i == 0 && (itemstack == null || itemstack.getItem() instanceof ICrystal);
	}

    public void checkPylon() {
        Block topBlock = this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord);
        Block bottomBlock = this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord);
        //No top part
        if (topBlock == PlayerBeacons.defiledSoulPylonBlock && bottomBlock == PlayerBeacons.defiledSoulPylonBlock) {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 3, 2);
        }
        //Base
        else if (!this.worldObj.isAirBlock(this.xCoord, this.yCoord - 1, this.zCoord) && bottomBlock != PlayerBeacons.defiledSoulPylonBlock) {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 2, 2);
        }
        //Top
        else if (!this.worldObj.isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord) && topBlock != PlayerBeacons.defiledSoulPylonBlock) {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 1, 2);
        }
        //Neither
        else {
            this.worldObj.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, 0, 2);
        }
    }

	public boolean isPylonBase() {
		return this.getBlockMetadata() == 2;
	}

	public boolean isPylonTop() {
		return this.getBlockMetadata() == 1;
	}

    @Override
    public IBeacon getBeacon() {
        if (this.hasParent && this.isParentValid()) return (IBeacon) this.worldObj.getTileEntity(this.parentX, this.parentY, this.parentZ);
        else return null;
    }

    @Override
    public void setBeacon(IBeacon theBeacon) {
        if (theBeacon.getTileEntity() instanceof TileEntityMultiBlock) {
            TileEntityMultiBlock multiBlock = (TileEntityMultiBlock) theBeacon.getTileEntity();
            this.setParent(multiBlock);
        }
    }
}
