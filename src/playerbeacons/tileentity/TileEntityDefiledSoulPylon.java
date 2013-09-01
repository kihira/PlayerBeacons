package playerbeacons.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.CrystalItem;

public class TileEntityDefiledSoulPylon extends TileEntity implements IInventory {

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
		if (crystal != null) crystal.writeToNBT(tag);
		par1NBTTagCompound.setTag("crystal", tag);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.customParam1);
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		if (i == 0) return crystal;
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
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	@Override
	public String getInvName() {
		return "Pylon";
	}

	@Override
	public boolean isInvNameLocalized() {
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
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return i == 0 && itemstack.getItem() instanceof CrystalItem;
	}

	public boolean isPylonBase() {
		return !worldObj.isAirBlock(xCoord, yCoord - 1, zCoord) && worldObj.getBlockId(xCoord, yCoord - 1, zCoord) != PlayerBeacons.config.defiledSoulPylonBlockID;
	}

	public boolean isPylonTop() {
		return !worldObj.isAirBlock(xCoord, yCoord + 1, zCoord) && worldObj.getBlockId(xCoord, yCoord + 1, zCoord) != PlayerBeacons.config.defiledSoulPylonBlockID;
	}
}
