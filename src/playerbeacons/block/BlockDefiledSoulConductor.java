package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.World;

public class BlockDefiledSoulConductor extends Block {

	public BlockDefiledSoulConductor(int id) {
		super(id, Material.rock);
		setHardness(15f);
		setResistance(100.0F);
		setCreativeTab(CreativeTabs.tabDecorations);
		setUnlocalizedName("Defiled Soul Conductor");
		setTextureName("playerbeacon:pyramidBrick");
	}

	public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}
}
