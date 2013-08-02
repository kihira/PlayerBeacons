package playerbeacons.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityPlayerBeacon extends TileEntity {

	private String owner;
	private boolean isActive;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.owner = par1NBTTagCompound.getString("owner");
		this.isActive = par1NBTTagCompound.getBoolean("isActive");

	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("owner", owner);
		par1NBTTagCompound.setBoolean("isActive", this.isActive);

	}

	@Override
	public void updateEntity() {
		//Update every 10 ticks. no need to update every tick
		if ((this.worldObj.getTotalWorldTime() %10L == 0) && !this.worldObj.isRemote) {
			//Use this somewhere
			//this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));

		}
	}
}
