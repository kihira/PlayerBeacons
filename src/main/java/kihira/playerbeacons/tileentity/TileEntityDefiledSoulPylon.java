package kihira.playerbeacons.tileentity;

import kihira.playerbeacons.api.throttle.ICrystal;
import kihira.playerbeacons.api.throttle.IThrottleContainer;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityDefiledSoulPylon extends TileEntity implements IInventory, IThrottleContainer {

	private ItemStack crystal;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagCompound tag = (NBTTagCompound) par1NBTTagCompound.getTag("crystal");
		this.crystal = ItemStack.loadItemStackFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		NBTTagCompound tag = new NBTTagCompound();
		if (this.crystal != null) this.crystal.writeToNBT(tag);
		par1NBTTagCompound.setTag("crystal", tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.func_148857_g());
        this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
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
		return null;
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return null;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		if (this.crystal != null) {
			ItemStack itemstack = this.crystal;
			this.crystal= null;
			return itemstack;
		}
		else return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i == 0) {
			this.crystal = itemstack;
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
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
		return i == 0 && itemstack.getItem() instanceof ICrystal;
	}

	public boolean isPylonBase() {
		return !this.worldObj.isAirBlock(this.xCoord, this.yCoord - 1, this.zCoord) && this.worldObj.getBlock(this.xCoord, this.yCoord - 1, this.zCoord) != PlayerBeacons.defiledSoulPylonBlock;
	}

	public boolean isPylonTop() {
		return !this.worldObj.isAirBlock(this.xCoord, this.yCoord + 1, this.zCoord) && this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) != PlayerBeacons.defiledSoulPylonBlock;
	}
}
