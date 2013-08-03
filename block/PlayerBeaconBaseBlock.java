package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class PlayerBeaconBaseBlock extends Block {

	public PlayerBeaconBaseBlock(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("playerBeaconBase");
		func_111022_d("playerbeacon:pyramidBrick");
	}

	@Override
	public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}
}
