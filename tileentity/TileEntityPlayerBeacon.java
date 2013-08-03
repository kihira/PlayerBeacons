package playerbeacons.tileentity;

import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.DigCrystalItem;
import playerbeacons.item.JumpCrystalItem;
import playerbeacons.item.ResCrystalItem;
import playerbeacons.item.SpeedCrystalItem;

public class TileEntityPlayerBeacon extends TileEntity {

	private String owner = " ";
	private boolean isActive = false;
	private float badStuff = 0;
	private int levels;
	private int resCrystals;
	private int speedCrystals;
	private int jumpCrystals;
	private int digCrystals;
	private ForgeChunkManager.Ticket ticket;

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		par1NBTTagCompound = PlayerBeacons.beaconData.loadBeaconInformation(worldObj, owner);
		this.owner = par1NBTTagCompound.getString("owner");
		this.badStuff = par1NBTTagCompound.getInteger("badStuff");
		this.isActive = par1NBTTagCompound.getBoolean("isActive");
		this.levels = par1NBTTagCompound.getInteger("levels");
		this.resCrystals = par1NBTTagCompound.getInteger("resCrystals");
		this.speedCrystals = par1NBTTagCompound.getInteger("speedCrystals");
		this.jumpCrystals = par1NBTTagCompound.getInteger("jumpCrystals");
		this.digCrystals = par1NBTTagCompound.getInteger("digCrystals");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		PlayerBeacons.beaconData.updateBeaconInformation(worldObj, owner, xCoord, yCoord, zCoord, isActive, badStuff, resCrystals, speedCrystals, jumpCrystals, digCrystals, levels);
	}

	public void initialSetup(EntityPlayer player) {
		if (!worldObj.isRemote) {
			this.owner = player.username;
			this.isActive = false;
			this.badStuff = 0;
			PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, player.username, this.xCoord, this.yCoord, this.zCoord, false, 0, 0, 0, 0, 0, 0);

			ticket = ForgeChunkManager.requestTicket(PlayerBeacons.instance, player.worldObj, ForgeChunkManager.Type.NORMAL);

			if (ticket == null) {
				player.sendChatToPlayer(ChatMessageComponent.func_111066_d("[PlayerBeacons] There is no more chunkloading tickets available so the beacon will not be chunk loaded"));
			}
			else {
				ticket.getModData().setInteger("x", xCoord);
				ticket.getModData().setInteger("y", yCoord);
				ticket.getModData().setInteger("z", zCoord);
				useTicket(ticket);
			}
		}
	}

	public void useTicket(ForgeChunkManager.Ticket ticket) {
		Chunk thisChunk = worldObj.getChunkFromBlockCoords(xCoord, zCoord);
		ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(thisChunk.xPosition, thisChunk.zPosition);
		ForgeChunkManager.forceChunk(ticket, chunkCoordIntPair);

	}

	public String getOwner() {
		return this.owner;
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public void invalidate() {
		ForgeChunkManager.releaseTicket(ticket);
		super.invalidate();
	}

	@Override
	public void updateEntity() {
		//Update every 10 ticks. no need to update every tick
		if ((this.worldObj.getTotalWorldTime() %20L == 0) && !this.worldObj.isRemote) {
			if (this.worldObj.getBlockId(this.xCoord, this.yCoord+1, this.zCoord) == Block.skull.blockID) {
				TileEntitySkull skull = (TileEntitySkull) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord+1, this.zCoord);
				//If player head
				if (skull.getExtraType().equals(this.owner)) {
					this.levels = 0;
					for (int i = 1; i <= 4; levels = i++) {
						int j = this.yCoord - i;

						if (j < 0) break;

						boolean flag = true;

						for (int k = this.xCoord - i; k <= this.xCoord + i && flag; ++k) {
							for (int l = this.zCoord - i; l <= this.zCoord + i; ++l) {
								if (!(this.worldObj.getBlockId(k, j, l) == PlayerBeacons.config.defiledSoulConductorBlockID)) {
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
							if (levels - 1 - speedCrystals >= 0) player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, levels - 1 - speedCrystals, true));
							if (levels - 1 - jumpCrystals >= 0) player.addPotionEffect(new PotionEffect(Potion.jump.id, 200, levels - 1 - jumpCrystals, true));
							if (levels - 1 - digCrystals >= 0) player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200, levels - 1 - digCrystals, true));
							if (levels - 1 - resCrystals >= 0) player.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, levels - 1 - resCrystals, true));
						}
					}
					//System.out.println("Detected " + levels + " beacon levels");
					//Keep this below the beacon base counter so we know what level to look on based on how many levels there are
					//TODO Overhaul this entire system? Need a good way of storing what type of conductors we have. HashMap or is that too expensive?
					//Calculate bad stuff every 2 seconds
					if ((levels > 0) && (this.worldObj.getTotalWorldTime() % 40L == 0)) {
						resCrystals = 0;
						speedCrystals = 0;
						jumpCrystals = 0;
						digCrystals = 0;

						System.out.println("We have " + levels + " levels");
						for (int y = 0; ((this.worldObj.getBlockId(this.xCoord - levels, this.yCoord - levels + 1 + y, this.zCoord - levels) == PlayerBeacons.config.defiledSoulPylonBlockID) && ( y < (1 + levels))); y++) {
							TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(xCoord - levels, yCoord - levels + 1 + y, zCoord - levels);
							if ((tileEntityDefiledSoulPylon != null) && tileEntityDefiledSoulPylon.getStackInSlot(0) != null) {
								if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof JumpCrystalItem) jumpCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof DigCrystalItem) digCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof SpeedCrystalItem) speedCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof ResCrystalItem) resCrystals++;
							}
						}
						for (int y = 0; (this.worldObj.getBlockId(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord - levels) == PlayerBeacons.config.defiledSoulPylonBlockID && ( y < (1 + levels))); y++) {
							TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord - levels);
							if ((tileEntityDefiledSoulPylon != null) && tileEntityDefiledSoulPylon.getStackInSlot(0) != null) {
								if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof JumpCrystalItem) jumpCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof DigCrystalItem) digCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof SpeedCrystalItem) speedCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof ResCrystalItem) resCrystals++;
							}
						}
						for (int y = 0; (this.worldObj.getBlockId(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord + levels) == PlayerBeacons.config.defiledSoulPylonBlockID && ( y < (1 + levels))); y++) {
							TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord + levels);
							if ((tileEntityDefiledSoulPylon != null) && tileEntityDefiledSoulPylon.getStackInSlot(0) != null) {
								if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof JumpCrystalItem) jumpCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof DigCrystalItem) digCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof SpeedCrystalItem) speedCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof ResCrystalItem) resCrystals++;
							}
						}
						for (int y = 0; (this.worldObj.getBlockId(this.xCoord - levels, this.yCoord - levels + 1 + y, this.zCoord + levels) == PlayerBeacons.config.defiledSoulPylonBlockID && ( y < (1 + levels))); y++) {
							TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(this.xCoord - levels, this.yCoord - levels + 1 + y, this.zCoord + levels);
							if ((tileEntityDefiledSoulPylon != null) && tileEntityDefiledSoulPylon.getStackInSlot(0) != null) {
								if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof JumpCrystalItem) jumpCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof DigCrystalItem) digCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof SpeedCrystalItem) speedCrystals++;
								else if (tileEntityDefiledSoulPylon.getStackInSlot(0).getItem() instanceof ResCrystalItem) resCrystals++;
							}
						}

						System.out.println("Jump: " + jumpCrystals + " Speed: " + speedCrystals + " Res: " + resCrystals + " Dig: " + digCrystals);
						calcBadStuff();
						doBadStuff(badStuff);
					}
				}
				else {
					this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
					worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
				}
			}
			else if ((this.worldObj.getBlockId(this.xCoord, this.yCoord+1, this.zCoord) == Block.dragonEgg.blockID) && (PlayerBeacons.config.enableEasterEgg == true)) {
				worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
				EntityDragon dragon = new EntityDragon(worldObj);
				dragon.setLocationAndAngles(xCoord, yCoord + 20, zCoord, 0, 0);
				dragon.setCustomNameTag(owner + "'s Puppy");
				worldObj.spawnEntityInWorld(dragon);
			}
		}
	}
	private void calcBadStuff() {
		float newBadStuff = 0;
		float modifier = MinecraftServer.getServer().getDifficulty() / 2;
		int y;
		y = levels - resCrystals;
		if (y > 0) newBadStuff = newBadStuff + (y * modifier);
		y = levels - digCrystals;
		if (y > 0) newBadStuff = newBadStuff + (y * modifier);
		y = levels - speedCrystals;
		if (y > 0) newBadStuff = newBadStuff + (y * modifier);
		y = levels - resCrystals;
		if (y > 0) newBadStuff = newBadStuff + (y * modifier);

		badStuff = badStuff + newBadStuff;
		System.out.println(badStuff);
	}

	private void doBadStuff(float badStuff) {
		float remainder = badStuff % 30;
		if (badStuff > 900) {
			if (badStuff % (worldObj.rand.nextFloat() - remainder) == 0) {
				EntityPlayer player = worldObj.getPlayerEntityByName(owner);
				if (player != null) {
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("You feel an unknown force grasp at you from the beyond, pulling you into another dimension"));
					player.attackEntityFrom(DamageSource.magic, 10);
					player.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
					player.addPotionEffect(new PotionEffect(Potion.confusion.id, 600));
					player.travelToDimension(1);
				}
			}
		}
		if (badStuff > 600) {
			if (badStuff % (worldObj.rand.nextFloat() - remainder) == 0) {
				EntityPlayer player = worldObj.getPlayerEntityByName(owner);
				if (player != null) {
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("You feel an unknown force grasp at you from the beyond, disorientating you"));
					player.attackEntityFrom(DamageSource.magic, 4);
					player.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
					player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300));
				}
			}
		}
		if (badStuff > 300) {
			if (badStuff % (worldObj.rand.nextFloat() - remainder) == 0) {
				EntityPlayer player = worldObj.getPlayerEntityByName(owner);
				if (player != null) {
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("You feel an unknown force grasp at you from the beyond"));
					player.attackEntityFrom(DamageSource.magic, 2);
				}
			}
		}
	}
}
