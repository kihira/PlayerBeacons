package playerbeacons.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.tileentity.TileEntityConductor;

public class ConductorBlock extends BlockContainer {

	public ConductorBlock(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("conductorBlock");
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, int par5, int par6) {

		TileEntityConductor tileEntityConductor = (TileEntityConductor)world.getBlockTileEntity(x, y, z);

		if (tileEntityConductor != null) {
			ItemStack inv = tileEntityConductor.getStackInSlot(0);
			if (inv != null) {
				tileEntityConductor.setInventorySlotContents(0, null);
				EntityItem item = new EntityItem(world, x, y+1, z, inv);
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
				if (itemStack.getItem() == PlayerBeacons.speedCrystalItem) {
					TileEntityConductor tileEntityConductor = (TileEntityConductor) world.getBlockTileEntity(x, y, z);
					if (tileEntityConductor.getStackInSlot(0) == null) {
						tileEntityConductor.setInventorySlotContents(0, entityPlayer.getCurrentItemOrArmor(0));
						entityPlayer.setCurrentItemOrArmor(0, null);
					}
				}
			}
			else {
				TileEntityConductor tileEntityConductor = (TileEntityConductor) world.getBlockTileEntity(x, y, z);
				ItemStack inv = tileEntityConductor.getStackInSlot(0);
				if (inv != null) {
					tileEntityConductor.setInventorySlotContents(0, null);
					EntityItem item = new EntityItem(world, x, y+1, z, inv);
					world.spawnEntityInWorld(item);
				}
			}
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityConductor();
	}
}
