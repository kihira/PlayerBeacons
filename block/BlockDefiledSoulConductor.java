package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.World;

public class BlockDefiledSoulConductor extends Block {

	public BlockDefiledSoulConductor(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("Defiled Soul Conductor");
		func_111022_d("playerbeacon:pyramidBrick");
	}

	@Override
	public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}
}
