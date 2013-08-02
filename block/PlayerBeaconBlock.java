package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class PlayerBeaconBlock extends Block {

	public PlayerBeaconBlock(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("Player Beacon");
	}
}
