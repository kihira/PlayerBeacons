package playerbeacons.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.World;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.CrystalItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TileEntityPlayerBeacon extends TileEntity {

	private String owner = " ";
	private boolean isActive = false;
	private int badStuff = 0;
	private HashMap<CrystalItem, Integer> conductors = new HashMap<CrystalItem, Integer>();
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
				if (skull.getSkullType() == 3) if (skull.getExtraType().equals(this.owner)) {
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
					if (levels > 0) {
						EntityPlayer player = this.worldObj.getPlayerEntityByName(skull.getExtraType());
						if (player != null) {
							//Do effects
							player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, levels - 1, true));
							player.addPotionEffect(new PotionEffect(Potion.jump.id, 200, levels - 1, true));
							player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200, levels - 1, true));
							player.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, levels - 1, true));
						}
					}
					//System.out.println("Detected " + levels + " beacon levels");
					//Keep this below the beacon base counter so we know what level to look on based on how many levels there are
					//TODO implement a better checking system
					//TODO Overhaul this entire system. Need a good way of storing what type of conductors we have. HashMap or is that too expensive?
					//This code is so bad
					int y = 1;
					int resCrystals = 0;
					int speedCrystals = 0;
					int jumpCrystals = 0;
					int digCrystals = 0;
					while (this.worldObj.getBlockId(this.xCoord - levels, this.yCoord - levels + y, this.zCoord - levels) == PlayerBeacons.config.conductorBlockID) {
						TileEntityConductor tileEntityConductor = (TileEntityConductor) worldObj.getBlockTileEntity(xCoord - levels, yCoord - levels + y, zCoord - levels);
						if ((tileEntityConductor != null) && tileEntityConductor.getStackInSlot(0) != null) {
							if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.jumpCrystalItemID) jumpCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.digCrystalItemID) digCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.speedCrystalItemID) speedCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.resCrystalItemID) resCrystals++;
						}
						y++;
						if (y > 3 + levels) break;
					}
					y = 1;
					while (this.worldObj.getBlockId(this.xCoord + levels, this.yCoord - levels + y, this.zCoord - levels) == PlayerBeacons.config.conductorBlockID) {
						TileEntityConductor tileEntityConductor = (TileEntityConductor) worldObj.getBlockTileEntity(this.xCoord + levels, this.yCoord - levels + y, this.zCoord - levels);
						if ((tileEntityConductor != null) && tileEntityConductor.getStackInSlot(0) != null) {
							if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.jumpCrystalItemID) jumpCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.digCrystalItemID) digCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.speedCrystalItemID) speedCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.resCrystalItemID) resCrystals++;
						}
						y++;
						if (y > 3 + levels) break;
					}
					y = 1;
					while (this.worldObj.getBlockId(this.xCoord + levels, this.yCoord - levels + y, this.zCoord + levels) == PlayerBeacons.config.conductorBlockID) {
						TileEntityConductor tileEntityConductor = (TileEntityConductor) worldObj.getBlockTileEntity(this.xCoord + levels, this.yCoord - levels + y, this.zCoord + levels);
						if ((tileEntityConductor != null) && tileEntityConductor.getStackInSlot(0) != null) {
							if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.jumpCrystalItemID) jumpCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.digCrystalItemID) digCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.speedCrystalItemID) speedCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.resCrystalItemID) resCrystals++;
						}
						y++;
						if (y > 3 + levels) break;
					}
					y = 1;
					while (this.worldObj.getBlockId(this.xCoord - levels, this.yCoord - levels + y, this.zCoord + levels) == PlayerBeacons.config.conductorBlockID) {
						TileEntityConductor tileEntityConductor = (TileEntityConductor) worldObj.getBlockTileEntity(this.xCoord - levels, this.yCoord - levels + y, this.zCoord + levels);
						if ((tileEntityConductor != null) && tileEntityConductor.getStackInSlot(0) != null) {
							if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.jumpCrystalItemID) jumpCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.digCrystalItemID) digCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.speedCrystalItemID) speedCrystals++;
							else if (tileEntityConductor.getStackInSlot(0).getItem().itemID == PlayerBeacons.config.resCrystalItemID) resCrystals++;
						}
						y++;
						if (y > 3 + levels) break;
					}

					System.out.println("Jump: " + jumpCrystals + " Speed: " + speedCrystals + " Res: " + resCrystals + " Dig: " + digCrystals);
					//Calculate bad stuff
					if ((levels > 0) && (this.worldObj.getTotalWorldTime() % 100L == 0)) {
						float baseBadStuff = 0;
					}

					//System.out.println("Detected " + conductors + " conductors");
				} else if (this.worldObj.getTotalWorldTime() % 60L == 0) {
					this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
					worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
				}
			}
		}
	}
}
