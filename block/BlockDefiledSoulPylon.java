package playerbeacons.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import playerbeacons.item.CrystalItem;
import playerbeacons.tileentity.TileEntityDefiledSoulPylon;

import java.util.List;

public class BlockDefiledSoulPylon extends BlockContainer {

	public BlockDefiledSoulPylon(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("pylonBlock");
		setBlockBounds(0.20F, 0.0F, 0.20F, 0.8F, 1.0F, 0.8F);
	}

	@Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
		if (tileEntityDefiledSoulPylon.isPylonBase()) {
			setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
			super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
		else {
			setBlockBounds(0.20F, 0.0F, 0.20F, 0.8F, 1.0F, 0.8F);
			super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
		if (tileEntityDefiledSoulPylon.isPylonBase()) return AxisAlignedBB.getBoundingBox(x + 0F, y + 0F, z + 0F, x + 1F, y + 1F, z + 1F);
		else return  AxisAlignedBB.getBoundingBox(x + 0.20D, y + 0.0F, z + 0.20F, x +  0.8F, y + 1.0F, z + 0.8F);
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

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int par6, float par7, float par8, float par9) {
		if (!world.isRemote) {
			ItemStack itemStack = entityPlayer.getCurrentItemOrArmor(0);
			if (itemStack != null) {
				if (itemStack.getItem() instanceof CrystalItem) {
					TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
					if (tileEntityDefiledSoulPylon.getStackInSlot(0) == null) {
						tileEntityDefiledSoulPylon.setInventorySlotContents(0, entityPlayer.getCurrentItemOrArmor(0));
						entityPlayer.setCurrentItemOrArmor(0, null);
					}
				}
			}
			else {
				TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getBlockTileEntity(x, y, z);
				ItemStack inv = tileEntityDefiledSoulPylon.getStackInSlot(0);
				if (inv != null) {
					tileEntityDefiledSoulPylon.setInventorySlotContents(0, null);
					EntityItem item = new EntityItem(world, x, y + 0.5, z, inv);
					world.spawnEntityInWorld(item);
				}
			}
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityDefiledSoulPylon();
	}
}
