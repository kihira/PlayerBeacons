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
	private int conductors;
	private int levels;

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
						//TODO not use an array? Maybe enum?
						this.levels = 0;
						for (int i = 1; i <= 4; levels = i++) {
							int j = this.yCoord - i;

							if (j < 0) break;

							boolean flag = true;

							for (int k = this.xCoord - i; k <= this.xCoord + i && flag; ++k) {
								for (int l = this.zCoord - i; l <= this.zCoord + i; ++l) {
									if (!(this.worldObj.getBlockId(k, j, l) == PlayerBeacons.config.playerBeaconBaseBlockID)) {
										flag = false;
										break;
									}
								}
							}

							if (!flag) break;
						}
						if (levels == 1) {
							EntityPlayer player = this.worldObj.getPlayerEntityByName(skull.getExtraType());
							if (player != null) {
								//Do effects
							}
						}
						System.out.println("Detected " + levels + " beacon levels");
						//Keep this below the beacon base counter so we know what level to look on based on how many levels there are
						//TODO implement a better checking system
						this.conductors = 0;
						int y = 1;
						while (this.worldObj.getBlockId(this.xCoord-levels, this.yCoord-levels+y, this.zCoord-levels) == PlayerBeacons.config.conductorBlockID) {
							conductors++;
							y++;
							if (y > 3 + levels) break;
						}
						y = 1;
						while (this.worldObj.getBlockId(this.xCoord+levels, this.yCoord-levels+y, this.zCoord-levels) == PlayerBeacons.config.conductorBlockID) {
							conductors++;
							y++;
							if (y > 3 + levels) break;
						}
						y = 1;
						while (this.worldObj.getBlockId(this.xCoord+levels, this.yCoord-levels+y, this.zCoord+levels) == PlayerBeacons.config.conductorBlockID) {
							conductors++;
							y++;
							if (y > 3 + levels) break;
						}
						y = 1;
						while (this.worldObj.getBlockId(this.xCoord-levels, this.yCoord-levels+y, this.zCoord+levels) == PlayerBeacons.config.conductorBlockID) {
							conductors++;
							y++;
							if (y > 3 + levels) break;
						}
						System.out.println("Detected " + conductors + " conductors");
					}
					else if (this.worldObj.getTotalWorldTime() %60L == 0) {
						this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
						worldObj.destroyBlock(xCoord, yCoord+1, zCoord, false);
					}
				}
			}
		}
	}
}
