package kihira.playerbeacons.common.block;

import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.tileentity.TileEntityDefiledSoulPylon;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockDefiledSoulPylon extends BlockContainer {

	public BlockDefiledSoulPylon() {
		super(Material.rock);
        this.setHardness(8F);
        this.setResistance(100F);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setBlockName("defiledSoulPylon");
        this.setBlockTextureName("playerbeacon:pyramidBrick");
        this.setBlockBounds(0.20F, 0.0F, 0.20F, 0.8F, 1.0F, 0.8F);
	}

    @Override
	public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB par5AxisAlignedBB, List list, Entity entity) {
		TileEntityDefiledSoulPylon defiledSoulPylon = (TileEntityDefiledSoulPylon) world.getTileEntity(x, y, z);
		if (defiledSoulPylon.isPylonBase() || defiledSoulPylon.isPylonTop()) {
            this.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		}
		else {
            this.setBlockBounds(0.20F, 0.0F, 0.20F, 0.8F, 1.0F, 0.8F);
		}
        super.addCollisionBoxesToList(world, x, y, z, par5AxisAlignedBB, list, entity);
	}

    @Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
		TileEntityDefiledSoulPylon defiledSoulPylon = (TileEntityDefiledSoulPylon) world.getTileEntity(x, y, z);
        if (defiledSoulPylon.isPylonTop() || defiledSoulPylon.isPylonBase()) return AxisAlignedBB.getBoundingBox(x + 0F, y + 0F, z + 0F, x + 1F, y + 1F, z + 1F);
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
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}

    @Override
	public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
		TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getTileEntity(x, y, z);
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
			TileEntityDefiledSoulPylon tileEntityDefiledSoulPylon = (TileEntityDefiledSoulPylon) world.getTileEntity(x, y, z);
			if (!tileEntityDefiledSoulPylon.isPylonBase() && !tileEntityDefiledSoulPylon.isPylonTop()) {
				ItemStack itemStack = entityPlayer.getCurrentEquippedItem();
				if (itemStack != null) {
					if (itemStack.getItem() instanceof ICrystal) {
						if (tileEntityDefiledSoulPylon.getStackInSlot(0) == null) {
							tileEntityDefiledSoulPylon.setInventorySlotContents(0, entityPlayer.getCurrentEquippedItem());
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

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block neighbourBlock) {
        TileEntityDefiledSoulPylon defiledSoulPylon = (TileEntityDefiledSoulPylon) world.getTileEntity(x, y, z);
        defiledSoulPylon.checkPylon();

        if (defiledSoulPylon.isPylonTop() || defiledSoulPylon.isPylonBase()) {
            ItemStack itemStack = defiledSoulPylon.getStackInSlotOnClosing(0);
            if (itemStack != null) {
                world.spawnEntityInWorld(new EntityItem(world, x, y, z, itemStack));
                defiledSoulPylon.setInventorySlotContents(0, null);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);

        TileEntityDefiledSoulPylon defiledSoulPylon = (TileEntityDefiledSoulPylon) world.getTileEntity(x, y, z);
        defiledSoulPylon.checkPylon();
    }

    @Override
	public TileEntity createNewTileEntity(World world, int var2) {
		return new TileEntityDefiledSoulPylon();
	}
}
