package playerbeacons.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
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
	private int badStuff = 0;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.owner = par1NBTTagCompound.getString("owner");
		this.badStuff = par1NBTTagCompound.getInteger("badStuff");
		this.isActive = par1NBTTagCompound.getBoolean("isActive");

	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("owner", this.owner);
		par1NBTTagCompound.setInteger("badStuff", this.badStuff);
		par1NBTTagCompound.setBoolean("isActive", this.isActive);

	}

	public void initialSetup(EntityPlayer player) {
		this.owner = player.username;
		this.isActive = false;
		this.badStuff = 0;
		PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, player.username, this.xCoord, this.yCoord, this.zCoord, false, 0);
	}

	public String getOwner() {
		return this.owner;
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public void updateEntity() {
		//Update every 10 ticks. no need to update every tick
		if ((this.worldObj.getTotalWorldTime() %10L == 0) && !this.worldObj.isRemote) {
			if (this.worldObj.getBlockId(this.xCoord, this.yCoord+1, this.zCoord) == Block.skull.blockID) {
				TileEntitySkull skull = (TileEntitySkull) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord+1, this.zCoord);
				//If player head
				if (skull.getSkullType() == 3) {
					if (skull.getExtraType().equals(this.owner)) {
						int beaconBase = 0;
						//TODO check more the one level
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
						//Keep this below the beacon base counter so we know what level to look on based on how many levels there are
						//TODO make this work depending on how many levels there are. Always check the lowest level
						int conductors = 0;
						for (int i = 0; i < 4; i++) {
							for (int j = 0; j < 4; j++) {
								for (int y = 0; y < 4; y++) {
									if (this.worldObj.getBlockId(this.xCoord-1+i, this.yCoord+y, this.zCoord-1+j) == PlayerBeacons.config.conductorBlockID) conductors++;
									else break;
								}
							}
						}
						System.out.println("Detected " + conductors + " conductors");
					}
					else if (this.worldObj.getTotalWorldTime() %60L == 0) this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
				}
			}
		}
	}
}
