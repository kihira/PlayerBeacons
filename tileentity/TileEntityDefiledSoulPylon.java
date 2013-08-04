package playerbeacons.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
		if (tag != null) this.crystal = ItemStack.loadItemStackFromNBT(tag);
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		if (crystal != null) {
			NBTTagCompound tag = new NBTTagCompound();
			crystal.writeToNBT(tag);
			par1NBTTagCompound.setTag("crystal", tag);
		}
	}

	@Override
	public Packet getDescriptionPacket() {
		System.out.println(FMLCommonHandler.instance().getEffectiveSide());
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		this.writeToNBT(nbttagcompound);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound);
	}

	@Override
	public void updateContainingBlockInfo() {
		super.updateContainingBlockInfo();
		System.out.println(FMLCommonHandler.instance().getEffectiveSide());
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
		if (this.crystal != null)
		{
			ItemStack itemstack = this.crystal;
			this.crystal= null;
			return itemstack;
		}
		else
		{
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		if (i == 0) this.crystal = itemstack;
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
		if (i == 0) {
			if (itemstack.getItem() instanceof CrystalItem) return true;
		}
		return false;
	}

	public boolean isPylonBase() {
		return worldObj.getBlockId(xCoord, yCoord - 1, zCoord) == PlayerBeacons.config.defiledSoulConductorBlockID;
	}
}
