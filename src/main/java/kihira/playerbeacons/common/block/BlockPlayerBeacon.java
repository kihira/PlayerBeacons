package kihira.playerbeacons.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.api.crystal.ICrystalContainer;
import kihira.playerbeacons.client.particle.EntityBuffParticleFX;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.tileentity.TileEntityPlayerBeacon;
import kihira.playerbeacons.common.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.List;
import java.util.Random;

public class BlockPlayerBeacon extends Block implements ITileEntityProvider {

    public BlockPlayerBeacon() {
		super(Material.rock);
        this.setHardness(8f);
        this.setResistance(100.0F);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setBlockName("playerBeacon");
        this.setBlockTextureName("playerbeacon:pyramidBrick");
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
    public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEntityPlayerBeacon();
	}

    @Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPlayerBeacon) {
				TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) tileEntity;
				if ((player.getCommandSenderName().equals(tileEntityPlayerBeacon.getOwnerUUID())) || player.capabilities.isCreativeMode || tileEntityPlayerBeacon.getOwnerUUID().equals(" ")) {
					tileEntity.invalidate();
					return world.setBlockToAir(x, y, z);
				}
				else {
					player.attackEntityFrom(PlayerBeacons.damageBehead, 10);
					player.addChatComponentMessage(new ChatComponentText("block.playerBeacon.guard"));
				}
			}
		}
		return false;
	}

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9) {
        if (player.getCurrentEquippedItem() != null && !world.isRemote) {
            ItemStack itemStack = player.getCurrentEquippedItem();
            //Check if player is holding a skull and that is belongs to them
            if (!(player instanceof FakePlayer) && itemStack.getItem() == Items.skull && itemStack.getItemDamage() == Util.EnumHeadType.PLAYER.getID()
                    && itemStack.hasTagCompound() && itemStack.getTagCompound().getString("SkullOwner").equals(player.getCommandSenderName())) {
                TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);
                //If there is no current beacon owner, set it to them
                if (tileEntityPlayerBeacon.getOwnerUUID().equals(" ")) {
                    tileEntityPlayerBeacon.setOwnerUUID(player);
                    if (itemStack.stackSize-- == 0) player.setCurrentItemOrArmor(0, null);
                    else player.setCurrentItemOrArmor(0, itemStack);
                }
            }
            //Create crystal from emeralds
            else if (itemStack.getItem() == Items.emerald) {
                if (itemStack.stackSize-- == 0) player.setCurrentItemOrArmor(0, null);
                else player.setCurrentItemOrArmor(0, itemStack);
                EntityItem item = new EntityItem(world, x, y + 0.5, z, new ItemStack(PlayerBeacons.crystalItem));
                world.spawnEntityInWorld(item);
            }
            //If they right click with depleted crystal, disperse all corruption
            else if (itemStack.getItem() instanceof ICrystal) {
                if (itemStack.stackSize-- == 0) player.setCurrentItemOrArmor(0, null);
                else player.setCurrentItemOrArmor(0, itemStack);
                world.markBlockForUpdate(x, y, z);
                player.addChatComponentMessage(new ChatComponentTranslation("crystal.dissipation").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA).setItalic(true)));
            }
        }
        return true;
	}

    @Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntityPlayerBeacon tileEntity = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);
            if (!(player.getCommandSenderName().equals(tileEntity.getOwnerUUID())) && !(player.capabilities.isCreativeMode) && !tileEntity.getOwnerUUID().equals(" ")) {
                player.attackEntityFrom(PlayerBeacons.damageBehead, 2);
                player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("block.playerBeacon.guard")));
            }
		}
	}

    @Override
    @SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int xPos, int yPos, int zPos, Random rand) {
        //A weird way to limit particles spawned but should work
        if (Minecraft.getMinecraft().gameSettings.particleSetting > 0) {
            if (rand.nextInt(5) > Minecraft.getMinecraft().gameSettings.particleSetting || Minecraft.getMinecraft().gameSettings.particleSetting == 2) return;
        }

        TileEntityPlayerBeacon playerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(xPos, yPos, zPos);
        int levels = playerBeacon.getLevels();

        if (playerBeacon.isBeaconValid() && levels > 0) {
            for (int y = 0; ((world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos - levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos - levels);
                this.doParticles(playerBeacon, xPos - levels, yPos - levels + 1 + y, zPos - levels, crystalContainer, new Random());
            }
            for (int y = 0; ((world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos - levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos - levels);
                this.doParticles(playerBeacon, xPos + levels + 1, yPos - levels + 1 + y, zPos - levels, crystalContainer, new Random());
            }
            for (int y = 0; ((world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos + levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos - levels, yPos - levels + 1 + y, zPos + levels);
                this.doParticles(playerBeacon, xPos - levels, yPos - levels + 1 + y, zPos + levels + 1, crystalContainer, new Random());
            }
            for (int y = 0; ((world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos + levels) instanceof ICrystalContainer) && (y < (1 + levels))); y++) {
                ICrystalContainer crystalContainer = (ICrystalContainer) world.getTileEntity(xPos + levels, yPos - levels + 1 + y, zPos + levels);
                this.doParticles(playerBeacon, xPos + levels + 1, yPos - levels + 1 + y, zPos + levels + 1, crystalContainer, new Random());
            }
		}
    }

    @SideOnly(Side.CLIENT)
    private void doParticles(TileEntityPlayerBeacon playerBeacon, int targetX, int targetY, int targetZ, ICrystalContainer crystalContainer, Random rand) {
        if (crystalContainer != null && crystalContainer.getSizeInventory() > 0) {
            for (int i = 0; i < crystalContainer.getSizeInventory(); i++) {
                ItemStack itemStack = crystalContainer.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() instanceof ICrystal) {
                    List<String> buffList = ((ICrystal) itemStack.getItem()).getAffectedBuffs();
                    if (buffList != null && !buffList.isEmpty()) {
                        for (String buff : buffList) {
                            for (int j = 0; j < 2; j++) {
                                Minecraft.getMinecraft().effectRenderer.addEffect(new EntityBuffParticleFX(targetX + (rand.nextFloat() / 5F), targetY + 0.4F + (rand.nextFloat() / 2F), targetZ + (rand.nextFloat() / 5F), playerBeacon, Buff.buffs.get(buff)));
                            }
                        }
                    }
                }
            }
        }
    }
}
