package kihira.playerbeacons.tileentity;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.api.throttle.IThrottle;
import kihira.playerbeacons.api.throttle.IThrottleContainer;
import kihira.playerbeacons.api.throttle.Throttle;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;

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
		par1NBTTagCompound.setString("owner", this.owner);
		par1NBTTagCompound.setFloat("badstuff", this.corruption);
		par1NBTTagCompound.setShort("badstufflevel", this.corruptionLevel);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
		this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
	}

	public void initialSetup(EntityPlayer player) {
		if (!this.worldObj.isRemote) {
			if (player != null) {
				this.owner = player.getCommandSenderName();
				this.corruption = 0;
				this.corruptionLevel = 0;
				PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, player.getCommandSenderName(), this.xCoord, this.yCoord, this.zCoord, false, 0, 0, (short) 0);
			}
		}
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String newOwner) {
		if (newOwner.length() <= 16 && PlayerBeacons.beaconData.loadBeaconInformation(this.worldObj, newOwner) == null) {
			NBTTagCompound nbtTagCompound = PlayerBeacons.beaconData.loadBeaconInformation(this.worldObj, this.owner);
			PlayerBeacons.beaconData.deleteBeaconInformation(this.worldObj, this.owner);
			PlayerBeacons.beaconData.addBeaconInformation(this.worldObj, newOwner, nbtTagCompound);
			this.owner = newOwner;
			this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
		}
	}

	public boolean hasSkull() {
		return this.hasSkull;
	}

	@Override
	public void invalidate() {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) PlayerBeacons.beaconData.deleteBeaconInformation(this.worldObj, this.owner);
		super.invalidate();
	}

	private boolean isCloneConstruct() {
		if (this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == Blocks.skull) return false;
		//Check 5x5 underneath and above
		for (int i = -2; i <= 2; i++) {
			for (int j = -2; j <= 2; j++) {
				if (this.worldObj.getBlock(this.xCoord + i, this.yCoord - 1, this.zCoord + j) != PlayerBeacons.defiledSoulConductorBlock) return false;
				if (this.worldObj.getBlock(this.xCoord + i, this.yCoord + 3, this.zCoord + j) != PlayerBeacons.defiledSoulConductorBlock) return false;
			}
		}
		//Check pylons
		for (int i = 0; i <= 3; i++) {
			if (this.worldObj.getBlock(this.xCoord + 2, this.yCoord + i, this.zCoord + 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
			if (this.worldObj.getBlock(this.xCoord + 2, this.yCoord + i, this.zCoord - 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
			if (this.worldObj.getBlock(this.xCoord - 2, this.yCoord + i, this.zCoord + 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
			if (this.worldObj.getBlock(this.xCoord - 2, this.yCoord + i, this.zCoord - 2) != PlayerBeacons.defiledSoulPylonBlock) return false;
		}
		return true;
	}

	public void checkBeacon() {
		this.levels = 0;
		if (this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == Blocks.skull) {
			this.hasSkull = true;
			for (int i = 1; i <= 4; levels = i++) {
				int j = this.yCoord - i;
				if (j < 0) break;
				boolean flag = true;
				for (int k = this.xCoord - i; k <= this.xCoord + i && flag; ++k) {
					for (int l = this.zCoord - i; l <= this.zCoord + i; ++l) {
						if (!(this.worldObj.getBlock(k, j, l) == PlayerBeacons.defiledSoulConductorBlock)) {
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
            //TODO Clone construct
			EntityPlayer entityPlayer = (EntityPlayer) entities.get(0);
		}
		else if ((this.worldObj.getBlock(this.xCoord, this.yCoord + 1, this.zCoord) == Blocks.dragon_egg) && (PlayerBeacons.config.enableEasterEgg)) {
			this.worldObj.func_147480_a(this.xCoord, this.yCoord + 1, this.zCoord, false); //Destroy block
			EntityDragon dragon = new EntityDragon(this.worldObj);
			dragon.setLocationAndAngles(this.xCoord, this.yCoord + 20, this.zCoord, 0, 0);
			dragon.setCustomNameTag(this.owner + "'s Puppy");
			this.worldObj.spawnEntityInWorld(dragon);
		}
		else this.hasSkull = false;
	}

    @SuppressWarnings("unchecked")
	public void doEffects() {
		TileEntitySkull skull = (TileEntitySkull) this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
		if (skull != null) {
			if (skull.func_145907_c().equals(this.owner)) {
				EntityPlayer entityPlayer = this.worldObj.getPlayerEntityByName(this.owner);
				if (entityPlayer != null && entityPlayer.dimension == this.worldObj.provider.dimensionId) {
					for (Map.Entry<String, Buff> entry : Buff.buffs.entrySet()) {
						Buff buff = entry.getValue();
						EntityPlayer player = this.worldObj.getPlayerEntityByName(this.owner);
						if ((buff.getMinBeaconLevel() <= this.levels) && (player != null)) {
							int i = 0;
							for (IThrottle throttle : Throttle.throttleList) {
								if (throttle.getAffectedBuffs().contains(entry.getKey())) i = i + this.throttleHashMap.get(throttle);
							}
							buff.doBuff(player, this.levels, i);
						}
					}
				}
			}
            //Other Players head
			else if (skull.func_145904_a() == 3) {
				this.worldObj.addWeatherEffect(new EntityLightningBolt(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord));
				this.worldObj.func_147480_a(this.xCoord, this.yCoord + 1, this.zCoord, false); //Destroy block
			}
			//Mob Head
			else if (this.levels > 0) {
				double d0 = (double)(this.levels * 7 + 10);
				AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool().getAABB(this.xCoord, this.yCoord, this.zCoord, this.xCoord + 1, this.yCoord + 1, this.zCoord + 1).expand(d0, d0, d0);
				axisalignedbb.maxY = this.worldObj.getHeight();
				List<Object> list = null;
				int skullType = skull.func_145904_a();
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
					for (Object entry : list) {
						entityCreature = (EntityCreature) entry;
                        if (!entityCreature.hasPath()) {
                            Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(entityCreature, 16, 7, entityCreature.worldObj.getWorldVec3Pool().getVecFromPool(this.xCoord, this.yCoord, this.zCoord));
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
	}

	public float getCorruption() {
		return this.corruption;
	}

	public void setCorruption(float newCorruption, boolean adjustLevel) {
		if (newCorruption >= 0) this.corruption = newCorruption;
		if (adjustLevel) {
			if (newCorruption >= 5000) this.corruptionLevel = 1;
			else if (newCorruption >= 10000) this.corruptionLevel = 2;
			else this.corruptionLevel = 0;
		}
	}

	public void calcPylons() {
		if (this.levels > 0) {
            this.throttleHashMap.clear();
			for (IThrottle throttle : Throttle.throttleList) {
                this.throttleHashMap.put(throttle, 0);
			}
			for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels) instanceof IThrottleContainer) && ( y < (1 + this.levels))); y++) {
				doPylon(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels);
			}
			for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels) instanceof IThrottleContainer) && ( y < (1 + this.levels))); y++) {
				doPylon(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord - this.levels);
			}
			for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels) instanceof IThrottleContainer) && ( y < (1 + this.levels))); y++) {
				doPylon(this.xCoord + this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels);
			}
			for (int y = 0; ((this.worldObj.getTileEntity(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels) instanceof IThrottleContainer) && ( y < (1 + this.levels))); y++) {
				doPylon(this.xCoord - this.levels, this.yCoord - this.levels + 1 + y, this.zCoord + this.levels);
			}
			for (Map.Entry<IThrottle, Integer> entry : this.throttleHashMap.entrySet()) {
				if (entry.getValue() > this.levels) this.throttleHashMap.put(entry.getKey(), this.levels);
			}
		}
	}

	private void doPylon(int x, int y, int z) {
		IInventory iInventory = (IInventory) this.worldObj.getTileEntity(x, y, z);
        //Get entire inventory incase it has more then one crystal slot.
		for (int i = 0; i <= iInventory.getSizeInventory(); i++) {
			if (((iInventory.getStackInSlot(i) != null) && iInventory.getStackInSlot(i).getItem() instanceof IThrottle)) {
				ItemStack itemStack = iInventory.getStackInSlot(i);
				IThrottle item = (IThrottle) iInventory.getStackInSlot(i).getItem();
				this.throttleHashMap.put(item, this.throttleHashMap.get(item) + 1);
				if (itemStack.getItemDamage() + 1 >= itemStack.getMaxDamage()) iInventory.setInventorySlotContents(0, new ItemStack(PlayerBeacons.crystalItem));
				else {
					itemStack.setItemDamage(itemStack.getItemDamage() + 1);
					iInventory.setInventorySlotContents(i, itemStack);
				}
			}
		}
	}

	public void calcCorruption() {
		if (this.levels >= 0) {
			float newCorruption = 0;
			float y;
			for (IThrottle throttle: Throttle.throttleList) {
				for (Object obj : throttle.getAffectedBuffs()) {
					Buff buff = Buff.buffs.get(obj.toString());
					if (buff.getMinBeaconLevel() <= this.levels) {
						y = buff.getCorruption(this.levels) - throttle.getCorruptionThrottle(buff, this.levels, this.throttleHashMap.get(throttle));
						newCorruption += y;
					}
				}
			}
			this.setCorruption(this.corruption + newCorruption - (this.levels * 10), false);
		}
	}

	public void doCorruption(boolean alwaysDoCorruption) {
        EntityPlayer player = this.worldObj.getPlayerEntityByName(this.owner);
        if (player != null) {
            if ((this.corruption > 15000) && (this.corruptionLevel == 2)) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYou feel an unknown force grasp at you from the beyond, pulling you into another dimension"));
                player.travelToDimension(1);
                this.corruptionLevel = 0;
                this.corruption -= this.worldObj.rand.nextInt(9000);
            }
            else if ((this.corruption > 10000) && (this.corruptionLevel == 1)) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYou feel an unknown force grasp at your soul from the beyond, disorientating you"));
                this.corruptionLevel = 2;
                this.corruption -= this.worldObj.rand.nextInt(2000);
            }
            else if ((this.corruption > 5000) && (this.corruptionLevel == 0)) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYou feel an unknown force grasp at you from the beyond"));
                this.corruptionLevel = 1;
                this.corruption -= this.worldObj.rand.nextInt(1000);
            }
            if (alwaysDoCorruption && this.corruption > 0) {
                player.addChatComponentMessage(new ChatComponentText("\u00a74\u00a7oYour corruption flows through your soul"));
                player.addPotionEffect(new PotionEffect(Potion.wither.id, (int)(this.corruption / 250) * 20));
            }
            if (this.corruptionLevel - 1 > -1) player.addPotionEffect(new PotionEffect(PlayerBeacons.config.corruptionPotionID, 6000, this.corruptionLevel - 1));
        }
	}
}