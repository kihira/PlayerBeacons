package kihira.playerbeacons.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.item.CrystalItem;
import kihira.playerbeacons.tileentity.TileEntityDefiledSoulPylon;

import java.util.List;

public class BlockDefiledSoulPylon extends BlockContainer {

	public BlockDefiledSoulPylon(int id) {
		super(id, Material.rock);
		setHardness(8f);
		setResistance(100.0F);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setUnlocalizedName("defiledSoulPylon");
		setTextureName("playerbeacon:pyramidBrick");
		setBlockBounds(0.20F, 0.0F, 0.20F, 0.8F, 1.0F, 0.8F);
	}

	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
		if (tileEntityDefiledSoulPylon.isPylonBase() || tileEntityDefiledSoulPylon.isPylonTop()) {
			setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
			super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
		else {
			setBlockBounds(0.20F, 0.0F, 0.20F, 0.8F, 1.0F, 0.8F);
			super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
	}

	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
		if (tileEntityDefiledSoulPylon.isPylonBase() || tileEntityDefiledSoulPylon.isPylonTop()) return AxisAlignedBB.getBoundingBox(x + 0F, y + 0F, z + 0F, x + 1F, y + 1F, z + 1F);
		else return  AxisAlignedBB.getBoundingBox(x + 0.20D, y + 0.0F, z + 0.20F, x +  0.8F, y + 1.0F, z + 0.8F);
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return -1;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}

	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon)world.getBlockTileEntity(x, y, z);
		if (tileEntityDefiledSoulPylon != null) {
			ItemStack inv = tileEntityDefiledSoulPylon.getStackInSlot(0);
			if (inv != null) {
				tileEntityDefiledSoulPylon.setInventorySlotContents(0, null);
				EntityItem item = new EntityItem(world, x, y + 0.5, z, inv);
				world.spawnEntityInWorld(item);
			}
		}
		super.breakBlock(world, x, y, z, par5, par6);
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
		if (!world.isRemote) {
			TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
			if (!tileEntityDefiledSoulPylon.isPylonBase() && !tileEntityDefiledSoulPylon.isPylonTop()) {
				ItemStack itemStack = entityPlayer.getCurrentItemOrArmor(0);
				if (itemStack != null) {
					if (itemStack.getItem() instanceof CrystalItem) {
						if (tileEntityDefiledSoulPylon.getStackInSlot(0) == null) {
							tileEntityDefiledSoulPylon.setInventorySlotContents(0, entityPlayer.getCurrentItemOrArmor(0));
							tileEntityDefiledSoulPylon.updateContainingBlockInfo();
							if (!entityPlayer.capabilities.isCreativeMode) entityPlayer.setCurrentItemOrArmor(0, null);
							world.markBlockForUpdate(x, y, z);
						}
					}
				}
				else {
					ItemStack inv = tileEntityDefiledSoulPylon.getStackInSlot(0);
					if (inv != null) {
						tileEntityDefiledSoulPylon.setInventorySlotContents(0, null);
						tileEntityDefiledSoulPylon.updateContainingBlockInfo();
						world.markBlockForUpdate(x, y, z);
						EntityItem item = new EntityItem(world, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, inv);
						world.spawnEntityInWorld(item);
					}
				}
			}
		}
		return false;
	}

	public TileEntity createNewTileEntity(World world) {
		return new TileEntityDefiledSoulPylon();
	}
}
