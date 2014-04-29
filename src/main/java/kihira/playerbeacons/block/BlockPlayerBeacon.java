package kihira.playerbeacons.block;

import kihira.playerbeacons.api.throttle.ICrystal;
import kihira.playerbeacons.common.DamageBehead;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;
import kihira.playerbeacons.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockPlayerBeacon extends Block implements ITileEntityProvider {

	public BlockPlayerBeacon() {
		super(Material.rock);
		setHardness(8f);
		setResistance(100.0F);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setBlockName("playerBeacon");
		setBlockTextureName("playerbeacon:pyramidBrick");
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
				if ((player.getCommandSenderName().equals(tileEntityPlayerBeacon.getOwner())) || player.capabilities.isCreativeMode || tileEntityPlayerBeacon.getOwner().equals(" ")) {
					tileEntityPlayerBeacon.applyCorruption();
					tileEntity.invalidate();
					return world.setBlockToAir(x, y, z);
				}
				else {
					player.attackEntityFrom(new DamageBehead(), 10);
					player.addChatComponentMessage(new ChatComponentText("\u00a7d\u00a7oA mystical energy seems to guard this device"));
				}
			}
		}
		return false;
	}

    @Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int meta, float par7, float par8, float par9) {
        if (player.getCurrentEquippedItem() != null) {
            if (!world.isRemote) {
                if (player.getCurrentEquippedItem().getItem() == Items.skull && player.getCurrentEquippedItem().getItemDamage() == Util.EnumHeadType.PLAYER.getID()
                        && player.getCurrentEquippedItem(). hasTagCompound() && player.getCurrentEquippedItem().getTagCompound().getString("SkullOwner").equals(player.getCommandSenderName())) {
                    TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);
                    if (tileEntityPlayerBeacon.getOwner().equals(" ")) {
                        tileEntityPlayerBeacon.setOwner(player);
                        player.setCurrentItemOrArmor(0, null);
                    }
                }
                else if (player.getCurrentEquippedItem().getItem() == Items.emerald) {
                    ItemStack itemStack = player.getCurrentEquippedItem();
                    if (itemStack.stackSize == 1) player.setCurrentItemOrArmor(0, null);
                    else player.setCurrentItemOrArmor(0, new ItemStack(Items.emerald, itemStack.stackSize - 1));
                    EntityItem item = new EntityItem(world, x, y + 0.5, z, new ItemStack(PlayerBeacons.crystalItem));
                    world.spawnEntityInWorld(item);
                }
                //If they right click with depleted, disperse all corruption
                else if (player.getCurrentEquippedItem().getItem() instanceof ICrystal) {
                    player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
                    TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getTileEntity(x, y, z);
                    tileEntityPlayerBeacon.applyCorruption();
                    tileEntityPlayerBeacon.setCorruption(0, true);
                    world.markBlockForUpdate(x, y, z);
                    player.addChatComponentMessage(new ChatComponentText("The crystal fizzles away as it interacts with the beacon, releasing the corruption from within it").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA).setItalic(true)));
                }
            }
        }
		return true;
	}

    @Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPlayerBeacon) {
				if (!(player.getCommandSenderName().equals(((TileEntityPlayerBeacon) tileEntity).getOwner())) && !(player.capabilities.isCreativeMode) && !((TileEntityPlayerBeacon) tileEntity).getOwner().equals(" ")) {
					player.attackEntityFrom(new DamageBehead(), 2);
					player.addChatComponentMessage(new ChatComponentText("\u00a7d\u00a7oA mystical energy seems to guard this device"));
				}
			}
		}
	}

    @Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (world.getBlock(x, y + 1, z) == Blocks.skull) {
			TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon)world.getTileEntity(x, y, z);
			float corrupution = tileEntityPlayerBeacon.getCorruption();
			for (int l = 0; l < (corrupution / 500); ++l) {
				double d1 = (double)((float)y + random.nextFloat());
				int i1 = random.nextInt(2) * 2 - 1;
				int j1 = random.nextInt(2) * 2 - 1;
				double d3 = ((double)random.nextFloat() - 0.5D) * 0.125D;
				double d5 = (double)z + 0.5D + 0.25D * (double)j1;
				double d4 = (double)(random.nextFloat() * 1.0F * (float)j1);
				double d6 = (double)x + 0.5D + 0.25D * (double)i1;
				double d2 = (double)(random.nextFloat() * 1.0F * (float)i1);
				world.spawnParticle("portal", d6, d1, d5, d2, d3, d4);
			}
		}
	}
}
