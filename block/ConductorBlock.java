package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class ConductorBlock extends Block {

	public ConductorBlock(int id) {
		super(id, Material.iron);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("Conductor");
	}
}
