package kihira.playerbeacons.common.block;

import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.beacon.IBeaconBase;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.tileentity.TileEntityDummy;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDefiledSoulConductor extends BlockMultiBlock implements IBeaconBase {

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
    public float getCorruptionReduction(IBeacon beacon, int blockCount) {
        return beacon.getLevels() * 10F;
    }

    @Override
    public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
        return new TileEntityDummy();
    }
}
