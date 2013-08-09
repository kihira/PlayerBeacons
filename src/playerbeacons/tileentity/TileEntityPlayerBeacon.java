package playerbeacons.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.CrystalItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TileEntityPlayerBeacon extends TileEntity {

	private String owner = " ";
	private boolean isActive = false;
	private float corruption = 0;
	private short corruptionLevel = 0;
	private int levels = 0;
	private HashMap<Item, Integer> crystals = new HashMap<Item, Integer>();

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.owner = par1NBTTagCompound.getString("owner");
		this.corruption = par1NBTTagCompound.getFloat("badstuff");
		this.corruptionLevel = par1NBTTagCompound.getShort("badstufflevel");
		NBTTagCompound nbtTagCompound = par1NBTTagCompound.getCompoundTag("beacondata");
		if (nbtTagCompound != null) {
			this.corruption = par1NBTTagCompound.getFloat("badstuff");
			this.isActive = nbtTagCompound.getBoolean("isActive");
			this.levels = nbtTagCompound.getInteger("levels");
			crystals.put(PlayerBeacons.resCrystalItem, nbtTagCompound.getInteger("resCrystals"));
			crystals.put(PlayerBeacons.speedCrystalItem, nbtTagCompound.getInteger("speedCrystals"));
			crystals.put(PlayerBeacons.jumpCrystalItem, nbtTagCompound.getInteger("jumpCrystals"));
			crystals.put(PlayerBeacons.digCrystalItem, nbtTagCompound.getInteger("digCrystals"));
			this.corruptionLevel = nbtTagCompound.getShort("badstufflevel");
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("owner", owner);
		par1NBTTagCompound.setFloat("badstuff", corruption);
		par1NBTTagCompound.setShort("badstufflevel", corruptionLevel);
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			PlayerBeacons.beaconData.updateBeaconInformation(worldObj, owner, xCoord, yCoord, zCoord, isActive, corruption, crystals.get(PlayerBeacons.resCrystalItem), crystals.get(PlayerBeacons.speedCrystalItem), crystals.get(PlayerBeacons.jumpCrystalItem), crystals.get(PlayerBeacons.digCrystalItem), levels, corruptionLevel);
			NBTTagCompound tagCompound = PlayerBeacons.beaconData.loadBeaconInformation(worldObj, owner);
			if (tagCompound != null) {
				par1NBTTagCompound.setCompoundTag("beacondata", tagCompound);
			}
		}
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

	public void initialSetup(EntityPlayer player) {
		if (!worldObj.isRemote) {
			if (player != null) {
				this.owner = player.username;
				this.isActive = false;
				this.corruption = 0;
				crystals.put(PlayerBeacons.crystalItem, 0);
				crystals.put(PlayerBeacons.resCrystalItem, 0);
				crystals.put(PlayerBeacons.speedCrystalItem, 0);
				crystals.put(PlayerBeacons.jumpCrystalItem, 0);
				crystals.put(PlayerBeacons.digCrystalItem, 0);
				PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, player.username, this.xCoord, this.yCoord, this.zCoord, false, 0, 0, 0, 0, 0, 0, (short) 0);
			}
		}
	}

	public String getOwner() {
		return this.owner;
	}

	public boolean isActive() {
		return isActive;
	}

	@Override
	public void invalidate() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) PlayerBeacons.beaconData.deleteBeaconInformation(worldObj, owner);
		super.invalidate();
	}

	public void checkBeacon() {
		this.levels = 0;
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord+1, this.zCoord) == Block.skull.blockID) {
			TileEntitySkull skull = (TileEntitySkull) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
			//If player head
			if (skull.getExtraType().equals(this.owner)) {
				EntityPlayer entityPlayer = worldObj.getPlayerEntityByName(this.owner);
				if (entityPlayer != null && entityPlayer.dimension == worldObj.provider.dimensionId) {
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
				}
			}
			else {
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
				worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
			}
		}
		else if ((this.worldObj.getBlockId(this.xCoord, this.yCoord+1, this.zCoord) == Block.dragonEgg.blockID) && (PlayerBeacons.config.enableEasterEgg)) {
			worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
			EntityDragon dragon = new EntityDragon(worldObj);
			dragon.setLocationAndAngles(xCoord, yCoord + 20, zCoord, 0, 0);
			dragon.setCustomNameTag(owner + "'s Puppy");
			worldObj.spawnEntityInWorld(dragon);
		}
	}

	public void doBuffs() {
		if (levels > 0) {
			this.isActive = true;
			EntityPlayer player = this.worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				//Do effects
				if (levels - 1 - crystals.get(PlayerBeacons.speedCrystalItem) >= 0) player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 200, levels - 1 - crystals.get(PlayerBeacons.speedCrystalItem), true));
				if (levels - 1 - crystals.get(PlayerBeacons.jumpCrystalItem) >= 0) player.addPotionEffect(new PotionEffect(Potion.jump.id, 200, levels - 1 - crystals.get(PlayerBeacons.jumpCrystalItem), true));
				if (levels - 1 - crystals.get(PlayerBeacons.digCrystalItem) >= 0) player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200, levels - 1 - crystals.get(PlayerBeacons.digCrystalItem), true));
				if (levels - 1 - crystals.get(PlayerBeacons.resCrystalItem) >= 0) player.addPotionEffect(new PotionEffect(Potion.resistance.id, 200, levels - 1 - crystals.get(PlayerBeacons.resCrystalItem), true));
			}
		}
		else
			this.isActive = false;
	}

	public float getCorruption() {
		return corruption;
	}

	public void setCorruption(float newCorruption, boolean adjustLevel) {
		if (newCorruption >= 0) corruption = newCorruption;
		if (adjustLevel) {
			if (newCorruption >= 3000) corruptionLevel = 1;
			else if (newCorruption >= 6000) corruptionLevel = 2;
			else corruptionLevel = 0;
		}
	}

	public void calcPylons() {
		if (levels > 0) {
			crystals.clear();
			crystals.put(PlayerBeacons.crystalItem, 0);
			crystals.put(PlayerBeacons.resCrystalItem, 0);
			crystals.put(PlayerBeacons.speedCrystalItem, 0);
			crystals.put(PlayerBeacons.jumpCrystalItem, 0);
			crystals.put(PlayerBeacons.digCrystalItem, 0);

			for (int y = 0; ((this.worldObj.getBlockId(this.xCoord - levels, this.yCoord - levels + 1 + y, this.zCoord - levels) == PlayerBeacons.config.defiledSoulPylonBlockID) && ( y < (1 + levels))); y++) {
				TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(xCoord - levels, yCoord - levels + 1 + y, zCoord - levels);
				ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
				if ((itemStack != null) && (itemStack.getItem() instanceof CrystalItem)) {
					crystals.put(itemStack.getItem(), crystals.get(itemStack.getItem()) + 1);
					if (itemStack.getItemDamage() + 1 >= itemStack.getMaxDamage()) tileEntityDefiledSoulPylon.setInventorySlotContents(0, new ItemStack(PlayerBeacons.crystalItem));
					else itemStack.setItemDamage(itemStack.getItemDamage() + 1);
				}
			}
			for (int y = 0; (this.worldObj.getBlockId(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord - levels) == PlayerBeacons.config.defiledSoulPylonBlockID && ( y < (1 + levels))); y++) {
				TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord - levels);
				ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
				if ((itemStack != null) && (itemStack.getItem() instanceof CrystalItem)) {
					crystals.put(itemStack.getItem(), crystals.get(itemStack.getItem()) + 1);
					if (itemStack.getItemDamage() + 1 >= itemStack.getMaxDamage()) tileEntityDefiledSoulPylon.setInventorySlotContents(0, new ItemStack(PlayerBeacons.crystalItem));
					else itemStack.setItemDamage(itemStack.getItemDamage() + 1);
				}
			}
			for (int y = 0; (this.worldObj.getBlockId(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord + levels) == PlayerBeacons.config.defiledSoulPylonBlockID && ( y < (1 + levels))); y++) {
				TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(this.xCoord + levels, this.yCoord - levels + 1 + y, this.zCoord + levels);
				ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
				if ((itemStack != null) && (itemStack.getItem() instanceof CrystalItem)) {
					crystals.put(itemStack.getItem(), crystals.get(itemStack.getItem()) + 1);
					if (itemStack.getItemDamage() + 1 >= itemStack.getMaxDamage()) tileEntityDefiledSoulPylon.setInventorySlotContents(0, new ItemStack(PlayerBeacons.crystalItem));
					else itemStack.setItemDamage(itemStack.getItemDamage() + 1);
				}
			}
			for (int y = 0; (this.worldObj.getBlockId(this.xCoord - levels, this.yCoord - levels + 1 + y, this.zCoord + levels) == PlayerBeacons.config.defiledSoulPylonBlockID && ( y < (1 + levels))); y++) {
				TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) worldObj.getBlockTileEntity(this.xCoord - levels, this.yCoord - levels + 1 + y, this.zCoord + levels);
				ItemStack itemStack = tileEntityDefiledSoulPylon.getStackInSlot(0);
				if ((itemStack != null) && (itemStack.getItem() instanceof CrystalItem)) {
					crystals.put(itemStack.getItem(), crystals.get(itemStack.getItem()) + 1);
					if (itemStack.getItemDamage() + 1 >= itemStack.getMaxDamage()) tileEntityDefiledSoulPylon.setInventorySlotContents(0, new ItemStack(PlayerBeacons.crystalItem));
					else itemStack.setItemDamage(itemStack.getItemDamage() + 1);
				}
			}
			//TODO Rework this into new corruption system
			for (Map.Entry<Item, Integer> entry : crystals.entrySet()) {
				if (entry.getValue() > levels) {
					crystals.put(entry.getKey(), levels);
				}
			}
		}
	}

	public void calcCorruption() {
		//This should allow up to level number of a single buff or spread across 2 buffs.
		if (!(((levels * 4) - crystals.get(PlayerBeacons.resCrystalItem) - crystals.get(PlayerBeacons.digCrystalItem) - crystals.get(PlayerBeacons.speedCrystalItem) - crystals.get(PlayerBeacons.jumpCrystalItem)) <= levels)) {
			float newCorruption = 0;
			float modifier = MinecraftServer.getServer().getDifficulty() / 4F;
			int y;

			y = levels - crystals.get(PlayerBeacons.resCrystalItem);
			if (y > 0) newCorruption = newCorruption + (y * modifier) + PlayerBeacons.resCrystalItem.getCorruptionValue() * PlayerBeacons.config.corruptionMultiplier;
			y = levels - crystals.get(PlayerBeacons.digCrystalItem);
			if (y > 0) newCorruption = newCorruption + (y * modifier) + PlayerBeacons.digCrystalItem.getCorruptionValue() * PlayerBeacons.config.corruptionMultiplier;
			y = levels - crystals.get(PlayerBeacons.speedCrystalItem);
			if (y > 0) newCorruption = newCorruption + (y * modifier) + PlayerBeacons.speedCrystalItem.getCorruptionValue() * PlayerBeacons.config.corruptionMultiplier;
			y = levels - crystals.get(PlayerBeacons.jumpCrystalItem);
			if (y > 0) newCorruption = newCorruption + (y * modifier) + PlayerBeacons.jumpCrystalItem.getCorruptionValue() * PlayerBeacons.config.corruptionMultiplier;

			corruption = corruption + newCorruption;
		}
	}

	public void doCorruption(boolean alwaysDoCorruption) {
		if (corruption > 0 && MinecraftServer.getServer().getDifficulty() > 0) {
			if (worldObj.rand.nextInt(1000) % 369 == 0) {
				EntityPlayer player = worldObj.getPlayerEntityByName(owner);
				if (player != null) {
					EntityEnderman enderman = new EntityEnderman(worldObj);
					enderman.setLocationAndAngles(player.posX + worldObj.rand.nextInt(10) - 5, player.posY, player.posZ + worldObj.rand.nextInt(10) - 5, 0F, 0F);
					enderman.setTarget(player);
					enderman.setScreaming(true);
					worldObj.spawnEntityInWorld(enderman);
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§4§oYour corruption has allowed a foul demon to spawn from the end"));
				}
			}
		}
		if ((corruption > 900) && (corruptionLevel == 2)) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§4§oYou feel an unknown force grasp at you from the beyond, pulling you into another dimension"));
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
				player.addPotionEffect(new PotionEffect(Potion.confusion.id, 600));
				player.travelToDimension(1);
				corruptionLevel = 0;
				this.corruption = corruption - worldObj.rand.nextInt(900);
			}
			return;
		}
		if ((corruption > 6000) && (corruptionLevel == 1)) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§4§oYou feel an unknown force grasp at your soul from the beyond, disorientating you"));
				player.attackEntityFrom(DamageSource.magic, 4);
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
				player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300));
				corruptionLevel = 2;
				this.corruption = corruption - worldObj.rand.nextInt(200);
			}
			return;
		}
		if ((corruption > 3000) && (corruptionLevel == 0)) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§4§oYou feel an unknown force grasp at you from the beyond"));
				player.attackEntityFrom(DamageSource.magic, 2);
				corruptionLevel = 1;
				this.corruption = corruption - worldObj.rand.nextInt(100);
			}
		}
		if (alwaysDoCorruption) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if ((player != null) && (corruption > 0)) {
				player.addPotionEffect(new PotionEffect(Potion.wither.id, (int) corruption/250, 1));
				player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§4§oYour corruption flows through your soul"));
			}
		}
	}
}
