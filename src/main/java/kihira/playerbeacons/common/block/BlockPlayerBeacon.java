package kihira.playerbeacons.common.block;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.foxlib.common.EnumHeadType;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.api.crystal.ICrystalContainer;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModBlocks;
import kihira.playerbeacons.common.lib.ModItems;
import kihira.playerbeacons.common.network.CorruptionUpdateMessage;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;
import java.util.Random;

public class BlockPlayerBeacon extends BlockMultiBlock {

    public BlockPlayerBeacon() {
		super(Material.rock);
        this.setHardness(8F);
        this.setResistance(100F);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setBlockName(ModBlocks.Names.SOUL_PYLON);
        this.setBlockTextureName(ModBlocks.Names.getTextureName(ModBlocks.Names.PLAYER_BEACON));
	}

    @Override
	public boolean isOpaqueCube() {
		return false;
	}

    @Override
	public int getRenderType() {
		return -1;
	}

    @Override
	public boolean renderAsNormalBlock() {
		return false;
	}

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityPlayerBeacon();
	}

    @Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        if (!world.isRemote) {
            TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);
            //If beacon is unbound, owned by player or player is in creative, destroy it
            if (tileEntityPlayerBeacon.getOwnerGameProfile() == null || player.getGameProfile().equals(tileEntityPlayerBeacon.getOwnerGameProfile()) || player.capabilities.isCreativeMode) {
                tileEntityPlayerBeacon.invalidate();
                return world.setBlockToAir(x, y, z);
            }
            else {
                player.attackEntityFrom(PlayerBeacons.damageBehead, 10);
                player.addChatComponentMessage(new ChatComponentText("block.playerBeacon.guard"));
            }
        }
        return false;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9) {
        if (player.getCurrentEquippedItem() != null && !world.isRemote) {
            ItemStack itemStack = player.getCurrentEquippedItem();
            //Check if player is holding a skull and that it belongs to them
            if (!(player instanceof FakePlayer) && itemStack.getItem() == Items.skull) {
                TileEntityPlayerBeacon playerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);

                if (itemStack.getItemDamage() == EnumHeadType.PLAYER.getID() && itemStack.hasTagCompound()) {
                    //Get skull gameprofile
                    GameProfile gameProfile = null;
                    if (itemStack.getTagCompound().hasKey("SkullOwner", 8)) { //Owners name as string
                        gameProfile = new GameProfile(null, itemStack.getTagCompound().getString("SkullOwner"));
                    }
                    else if (itemStack.getTagCompound().hasKey("SkullOwner", 10)) { //The owners game profile
                        gameProfile = NBTUtil.func_152459_a(itemStack.getTagCompound().getCompoundTag("SkullOwner"));
                    }

                    //If there is no current beacon owner, set it to them. Check based off name as the skull game profile might not have UUID
                    if (playerBeacon.getOwnerGameProfile() == null && gameProfile != null && player.getGameProfile().getName().equals(gameProfile.getName()) &&
                            !BeaconDataHelper.playerHasBeacon(player, world.provider.dimensionId)) {
                        playerBeacon.setOwner(player);
                        playerBeacon.setIsParent();
                        //Generate the Beacon instance
                        BeaconDataHelper.getBeaconForDim(player, player.dimension);
                        //Then validate
                        BeaconDataHelper.markBeaconDirty(playerBeacon);

                        if (itemStack.stackSize-- == 0) player.setCurrentItemOrArmor(0, null);
                        else player.setCurrentItemOrArmor(0, itemStack);
                    }
                }
                else {
                    //Set it as mob head
                    playerBeacon.setHeadType(EnumHeadType.fromId(itemStack.getItemDamage()));
                }
            }
            //Create crystal from emeralds
            else if (itemStack.getItem() == Items.emerald) {
                if (itemStack.stackSize-- == 0) player.setCurrentItemOrArmor(0, null);
                else player.setCurrentItemOrArmor(0, itemStack);
                EntityItem item = new EntityItem(world, x, y + 0.5, z, new ItemStack(ModItems.itemCrystal));
                world.spawnEntityInWorld(item);
            }
            //If they right click with depleted crystal, disperse all corruption TODO remove
            else if (itemStack.getItem() instanceof ICrystal) {
                if (itemStack.stackSize-- == 0) player.setCurrentItemOrArmor(0, null);
                else player.setCurrentItemOrArmor(0, itemStack);

                float oldCorr = BeaconDataHelper.getPlayerCorruptionAmount(player);
                BeaconDataHelper.setPlayerCorruptionAmount(player, 0);
                player.addChatComponentMessage(new ChatComponentTranslation("crystal.dissipation").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA).setItalic(true)));

                //Send corruption update
                PlayerBeacons.networkWrapper.sendToAllAround(new CorruptionUpdateMessage(player.getCommandSenderName(), 0, oldCorr), new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 64));
            }
        }
        return true;
	}

    @Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntityPlayerBeacon tileEntity = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);
            //If beacon is unbound, owned by player or player is in creative, allow
            if (tileEntity.getOwnerGameProfile() != null && !player.getGameProfile().equals(tileEntity.getOwnerGameProfile()) && !(player.capabilities.isCreativeMode)) {
                player.attackEntityFrom(PlayerBeacons.damageBehead, 2);
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("block.playerBeacon.guard")));
            }
		}
	}

    @Override
    public boolean isNormalCube(IBlockAccess world, int x, int y, int z) {
        return false;
    }

    //TODO fix this (levels == 0)
    @Override
    @SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int xPos, int yPos, int zPos, Random rand) {
        //A weird way to limit particles spawned but should work
        if (Minecraft.getMinecraft().gameSettings.particleSetting > 0) {
            if (rand.nextInt(5) > Minecraft.getMinecraft().gameSettings.particleSetting || Minecraft.getMinecraft().gameSettings.particleSetting == 2) return;
        }

        TileEntityPlayerBeacon playerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(xPos, yPos, zPos);
        int levels = playerBeacon.getLevels();

        if (levels > 0) {
            for (int y = 0; ((world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos - levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos - levels);
                this.doParticles(playerBeacon, xPos - levels, yPos - levels + 1 + y, zPos - levels, crystalContainer);
            }
            for (int y = 0; ((world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos - levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos - levels);
                this.doParticles(playerBeacon, xPos + levels + 1, yPos - levels + 1 + y, zPos - levels, crystalContainer);
            }
            for (int y = 0; ((world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos + levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos + levels);
                this.doParticles(playerBeacon, xPos - levels, yPos - levels + 1 + y, zPos + levels + 1, crystalContainer);
            }
            for (int y = 0; ((world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos + levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos + levels);
                this.doParticles(playerBeacon, xPos + levels + 1, yPos - levels + 1 + y, zPos + levels + 1, crystalContainer);
            }
		}
    }

    @SideOnly(Side.CLIENT)
    private void doParticles(TileEntityPlayerBeacon playerBeacon, int targetX, int targetY, int targetZ, ICrystalContainer crystalContainer) {
        if (crystalContainer != null && crystalContainer.getSizeInventory() > 0) {
            for (int i = 0; i < crystalContainer.getSizeInventory(); i++) {
                ItemStack itemStack = crystalContainer.getStackInSlot(i);
                //Verify it is a crystal
                if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                    List<Buff> buffList = ((ICrystal) itemStack.getItem()).getAffectedBuffs();
                    //Check buff list isn't empty or null
                    if (buffList != null && !buffList.isEmpty()) {
                        for (Buff buff : buffList) {
                            PlayerBeacons.proxy.spawnBeaconParticle(targetX, targetY, targetZ, playerBeacon, buff);
                        }
                    }
                }
            }
        }
    }
}
