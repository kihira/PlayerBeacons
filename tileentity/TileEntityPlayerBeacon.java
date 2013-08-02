package playerbeacons.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import playerbeacons.common.PlayerBeacons;

public class TileEntityPlayerBeacon extends TileEntity {

	private String owner = " ";
	private boolean isActive = false;

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

	public void initialSetup(EntityPlayer player) {
		this.owner = player.username;
		this.isActive = false;
		PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, player, this.xCoord, this.yCoord, this.zCoord, false);
	}

	@Override
	public void updateEntity() {
		//Update every 10 ticks. no need to update every tick
		if ((this.worldObj.getTotalWorldTime() %10L == 0) && !this.worldObj.isRemote) {
			if (this.worldObj.getBlockId(this.xCoord, this.yCoord+1, this.zCoord) == Block.skull.blockID) {
				TileEntitySkull skull = (TileEntitySkull) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord+1, this.zCoord);
				//If player head
				if (skull.getSkullType() == 3) {
					int beaconBase =0;
					for (int i = 0; i < 4; i++) {
						for (int j = 0; j <4; j++) {
							if (this.worldObj.getBlockId(this.xCoord-1+i, this.yCoord-1, this.zCoord-1+j) == PlayerBeacons.config.playerBeaconBaseBlockID) beaconBase++;
						}
					}
					if (beaconBase == 9) {
						EntityPlayer player = this.worldObj.getPlayerEntityByName(skull.getExtraType());
						if (player != null) {
							player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200));
						}
					}
					System.out.println("Detected " + beaconBase + " beacon bases");
				}

			}
			//Use this somewhere
			//this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
		}
	}
}
