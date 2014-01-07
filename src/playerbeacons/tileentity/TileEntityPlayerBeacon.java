package playerbeacons.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import playerbeacons.api.buff.Buff;
import playerbeacons.api.throttle.IThrottle;
import playerbeacons.api.throttle.IThrottleContainer;
import playerbeacons.api.throttle.Throttle;
import playerbeacons.common.PlayerBeacons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileEntityPlayerBeacon extends TileEntity {

	private String owner = " ";
	private boolean hasSkull;
	private float corruption = 0;
	private short corruptionLevel = 0;
	private int levels = 0;
	private HashMap<IThrottle, Integer> throttleHashMap = new HashMap<IThrottle, Integer>();

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		this.owner = par1NBTTagCompound.getString("owner");
		this.corruption = par1NBTTagCompound.getFloat("badstuff");
		this.corruptionLevel = par1NBTTagCompound.getShort("badstufflevel");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setString("owner", owner);
		par1NBTTagCompound.setFloat("badstuff", corruption);
		par1NBTTagCompound.setShort("badstufflevel", corruptionLevel);
	}

	@Override
	public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt) {
		readFromNBT(pkt.data);
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
				this.corruption = 0;
				this.corruptionLevel = 0;
				PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, player.username, this.xCoord, this.yCoord, this.zCoord, false, 0, 0, (short) 0);
			}
		}
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String newOwner) {
		if (newOwner.length() <= 16 && PlayerBeacons.beaconData.loadBeaconInformation(worldObj, newOwner) == null) {
			NBTTagCompound nbtTagCompound = PlayerBeacons.beaconData.loadBeaconInformation(this.worldObj, owner);
			PlayerBeacons.beaconData.deleteBeaconInformation(worldObj, owner);
			PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, newOwner, nbtTagCompound);
			this.owner = newOwner;
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	public boolean hasSkull() {
		return hasSkull;
	}

	@Override
	public void invalidate() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) PlayerBeacons.beaconData.deleteBeaconInformation(worldObj, owner);
		super.invalidate();
	}

	private boolean isCloneConstruct() {
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == Block.skull.blockID) return false;
		//Check 5x5 underneath and above
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				if (worldObj.getBlockId(this.xCoord + i, this.yCoord - 1, this.zCoord + j) != PlayerBeacons.defiledSoulConductorBlock.blockID) return false;
				if (worldObj.getBlockId(this.xCoord + i, this.yCoord + 3, this.zCoord + j) != PlayerBeacons.defiledSoulConductorBlock.blockID) return false;
			}
		}
		//Check pylons
		for (int i = 0; i <= 3; i++) {
			if (worldObj.getBlockId(this.xCoord + 2, this.yCoord + i, this.zCoord + 2) != PlayerBeacons.defiledSoulPylonBlock.blockID) return false;
			if (worldObj.getBlockId(this.xCoord + 2, this.yCoord + i, this.zCoord - 2) != PlayerBeacons.defiledSoulPylonBlock.blockID) return false;
			if (worldObj.getBlockId(this.xCoord - 2, this.yCoord + i, this.zCoord + 2) != PlayerBeacons.defiledSoulPylonBlock.blockID) return false;
			if (worldObj.getBlockId(this.xCoord - 2, this.yCoord + i, this.zCoord - 2) != PlayerBeacons.defiledSoulPylonBlock.blockID) return false;
		}
		return true;
	}

	public void checkBeacon() {
		this.levels = 0;
		if (this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == Block.skull.blockID) {
			hasSkull = true;
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
			return;
		}
		AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double) this.xCoord, (double) this.yCoord, (double) this.zCoord, (double) (this.xCoord), (double) (this.yCoord + 1), (double) (this.zCoord));
		List entities = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
		if ((entities != null) && (this.isCloneConstruct())) {
			EntityPlayer entityPlayer = (EntityPlayer) entities.get(0);

		}
		else if ((this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord) == Block.dragonEgg.blockID) && (PlayerBeacons.config.enableEasterEgg)) {
			worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
			EntityDragon dragon = new EntityDragon(worldObj);
			dragon.setLocationAndAngles(xCoord, yCoord + 20, zCoord, 0, 0);
			dragon.setCustomNameTag(owner + "'s Puppy");
			worldObj.spawnEntityInWorld(dragon);
		}
		else hasSkull = false;
	}

	public void doEffects() {
		TileEntitySkull skull = (TileEntitySkull) this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
		if (skull != null) {
			if (skull.getExtraType().equals(this.owner)) {
				EntityPlayer entityPlayer = worldObj.getPlayerEntityByName(this.owner);
				if (entityPlayer != null && entityPlayer.dimension == worldObj.provider.dimensionId) {
					for (Map.Entry<String, Buff> entry : Buff.buffs.entrySet()) {
						Buff buff = entry.getValue();
						EntityPlayer player = worldObj.getPlayerEntityByName(owner);
						if ((buff.getMinBeaconLevel() <= levels) && (player != null)) {
							int i = 0;
							for (IThrottle throttle : Throttle.throttleList) {
								if (throttle.getAffectedBuffs().contains(entry.getKey())) i = i + throttleHashMap.get(throttle);
							}
							//System.out.println("Applying " + buff.getName() + " with " + i + " crystals throttling");
							buff.doBuff(player, levels, i);
						}
					}
				}
			}
			else if (skull.getSkullType() == 3) {
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord, this.zCoord));
				worldObj.destroyBlock(xCoord, yCoord + 1, zCoord, false);
			}
			//Mob Head
			else if (levels > 0) {
				double d0 = (double)(this.levels * 7 + 10);
				AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB((double)this.xCoord, (double)this.yCoord, (double)this.zCoord, (double)(this.xCoord + 1), (double)(this.yCoord + 1), (double)(this.zCoord + 1)).expand(d0, d0, d0);
				axisalignedbb.maxY = (double)this.worldObj.getHeight();
				List<Object> list = null;
				int skullType = skull.getSkullType();
				if (skullType == 0 || skullType == 1) {
					List list1 = this.worldObj.getEntitiesWithinAABB(EntitySkeleton.class, axisalignedbb);
					list = new ArrayList<Object>();
					for (Object object:list1) {
						EntitySkeleton skeleton = (EntitySkeleton) object;
						if (skeleton.getSkeletonType() == skullType) list.add(object);
					}
				}
				else if (skullType == 2) list = this.worldObj.getEntitiesWithinAABB(EntityZombie.class, axisalignedbb);
				else if (skullType == 4) list = this.worldObj.getEntitiesWithinAABB(EntityCreeper.class, axisalignedbb);
				if (list != null && !list.isEmpty()) {
					EntityCreature entityCreature;
					for (Object aList : list) {
						entityCreature = (EntityCreature) aList;
						Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entityCreature, 16, 7, entityCreature.worldObj.getWorldVec3Pool().getVecFromPool(xCoord, yCoord, zCoord));
						PathNavigate entityPathNavigate = entityCreature.getNavigator();
						if (entityPathNavigate != null && vec3 != null) {
							PathEntity entityPathEntity = entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
							if (entityPathEntity != null) entityPathNavigate.setPath(entityPathEntity, 1.1D);
						}
					}
				}
			}
		}
	}

	public float getCorruption() {
		return corruption;
	}

	public void setCorruption(float newCorruption, boolean adjustLevel) {
		if (newCorruption >= 0) corruption = newCorruption;
		if (adjustLevel) {
			if (newCorruption >= 5000) corruptionLevel = 1;
			else if (newCorruption >= 10000) corruptionLevel = 2;
			else corruptionLevel = 0;
		}
	}

	public void calcPylons() {
		if (levels > 0) {
			throttleHashMap.clear();
			for (IThrottle throttle : Throttle.throttleList) {
				throttleHashMap.put(throttle, 0);
			}
			for (int y = 0; ((worldObj.getBlockTileEntity(xCoord - levels, yCoord - levels + 1 + y, zCoord - levels) instanceof IThrottleContainer) && ( y < (1 + levels))); y++) {
				doPylon(xCoord - levels, yCoord - levels + 1 + y, zCoord - levels);
			}
			for (int y = 0; ((worldObj.getBlockTileEntity(xCoord + levels, yCoord - levels + 1 + y, zCoord - levels) instanceof IThrottleContainer) && ( y < (1 + levels))); y++) {
				doPylon(xCoord + levels, yCoord - levels + 1 + y, zCoord - levels);
			}
			for (int y = 0; ((worldObj.getBlockTileEntity(xCoord + levels, yCoord - levels + 1 + y, zCoord + levels) instanceof IThrottleContainer) && ( y < (1 + levels))); y++) {
				doPylon(xCoord + levels, yCoord - levels + 1 + y, zCoord + levels);
			}
			for (int y = 0; ((worldObj.getBlockTileEntity(xCoord - levels, yCoord - levels + 1 + y, zCoord + levels) instanceof IThrottleContainer) && ( y < (1 + levels))); y++) {
				doPylon(xCoord - levels, yCoord - levels + 1 + y, zCoord + levels);
			}
			for (Map.Entry<IThrottle, Integer> entry : throttleHashMap.entrySet()) {
				if (entry.getValue() > levels) throttleHashMap.put(entry.getKey(), levels);
			}
		}
	}

	private void doPylon(int x, int y, int z) {
		IInventory iInventory = (IInventory) worldObj.getBlockTileEntity(x, y, z);
		for (int i = 0; i <= iInventory.getSizeInventory(); i++) {
			if (((iInventory.getStackInSlot(i) != null) && iInventory.getStackInSlot(i).getItem() instanceof IThrottle)) {
				ItemStack itemStack = iInventory.getStackInSlot(i);
				IThrottle item = (IThrottle) iInventory.getStackInSlot(i).getItem();
				throttleHashMap.put(item, throttleHashMap.get(item)+1);
				if (itemStack.getItemDamage()+1 >= itemStack.getMaxDamage()) iInventory.setInventorySlotContents(0, new ItemStack(PlayerBeacons.crystalItem));
				else {
					itemStack.setItemDamage(itemStack.getItemDamage() + 1);
					iInventory.setInventorySlotContents(i, itemStack);
				}
			}
		}
	}

	public void calcCorruption() {
		if (levels >= 0) {
			float newCorruption = 0;
			float y;
			for (IThrottle throttle: Throttle.throttleList) {
				for (Object obj : throttle.getAffectedBuffs()) {
					Buff buff = Buff.buffs.get(obj.toString());
					if (buff.getMinBeaconLevel() <= levels) {
						y = buff.getCorruption(levels) - throttle.getCorruptionThrottle(buff, levels, throttleHashMap.get(throttle));
						//System.out.println("Generated " + y + " corruption for " + buff.getName());
						if (y > 0) newCorruption += y;
					}
				}
			}
			setCorruption(corruption + newCorruption - (levels * 10), false);
		}
	}

	public void doCorruption(boolean alwaysDoCorruption) {
		System.out.println(corruption + " " + corruptionLevel);
		if (corruption > 0 && MinecraftServer.getServer().getDifficulty() > 0) {
			if (worldObj.rand.nextInt(1000) < 5) {
				EntityPlayer player = worldObj.getPlayerEntityByName(owner);
				if (player != null) {
					EntityEnderman enderman = new EntityEnderman(worldObj);
					enderman.setLocationAndAngles(player.posX + worldObj.rand.nextInt(10) - 5, player.posY, player.posZ + worldObj.rand.nextInt(10) - 5, 0F, 0F);
					enderman.setTarget(player);
					enderman.setScreaming(true);
					worldObj.spawnEntityInWorld(enderman);
					player.sendChatToPlayer(ChatMessageComponent.createFromText("§4§oYour corruption has allowed a foul demon to spawn from the end"));
				}
			}
		}
		if ((corruption > 15000) && (corruptionLevel == 2)) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§4§oYou feel an unknown force grasp at you from the beyond, pulling you into another dimension"));
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
				player.addPotionEffect(new PotionEffect(Potion.confusion.id, 600));
				player.travelToDimension(1);
				corruptionLevel = 0;
				this.corruption = corruption - worldObj.rand.nextInt(9000);
			}
			return;
		}
		if ((corruption > 10000) && (corruptionLevel == 1)) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§4§oYou feel an unknown force grasp at your soul from the beyond, disorientating you"));
				player.attackEntityFrom(DamageSource.magic, 4);
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 600));
				player.addPotionEffect(new PotionEffect(Potion.confusion.id, 300));
				corruptionLevel = 2;
				this.corruption = corruption - worldObj.rand.nextInt(2000);
			}
			return;
		}
		if ((corruption > 5000) && (corruptionLevel == 0)) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if (player != null) {
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§4§oYou feel an unknown force grasp at you from the beyond"));
				player.attackEntityFrom(DamageSource.magic, 2);
				corruptionLevel = 1;
				this.corruption = corruption - worldObj.rand.nextInt(1000);
			}
		}
		if (alwaysDoCorruption) {
			EntityPlayer player = worldObj.getPlayerEntityByName(owner);
			if ((player != null) && (corruption > 0)) {
				player.addPotionEffect(new PotionEffect(Potion.wither.id, (int) corruption/250, 1));
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§4§oYour corruption flows through your soul"));
			}
		}
	}
}