package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class PlayerBeaconBaseBlock extends Block {

	public PlayerBeaconBaseBlock(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("Player Beacon Base");
	}
}
