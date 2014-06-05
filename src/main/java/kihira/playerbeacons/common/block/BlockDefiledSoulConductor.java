package kihira.playerbeacons.common.block;

import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.beacon.IBeaconBase;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.IBlockAccess;

public class BlockDefiledSoulConductor extends Block implements IBeaconBase {

	public BlockDefiledSoulConductor() {
		super(Material.rock);
		this.setHardness(15f);
        this.setResistance(100.0F);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setBlockName("defiledSoulConductor");
        this.setBlockTextureName("playerbeacon:pyramidBrick");
	}

    @Override
	public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

    @Override
	public boolean canEntityDestroy(IBlockAccess world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}

    @Override
    public boolean isValidForBeacon(IBeacon beacon) {
        return true;
    }

    @Override
    public float getCorruptionReduction(IBeacon beacon) {
        return 0;
    }
}
