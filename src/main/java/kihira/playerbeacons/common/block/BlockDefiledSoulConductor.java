package kihira.playerbeacons.common.block;

import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.IBlockAccess;

public class BlockDefiledSoulConductor extends Block {

	public BlockDefiledSoulConductor() {
		super(Material.rock);
		setHardness(15f);
		setResistance(100.0F);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setBlockName("defiledSoulConductor");
		setBlockTextureName("playerbeacon:pyramidBrick");
	}

    @Override
    public String getItemIconName() {
        return null;
    }

    @Override
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

    @Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}
}
