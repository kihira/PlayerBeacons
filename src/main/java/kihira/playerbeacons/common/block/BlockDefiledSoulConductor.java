package kihira.playerbeacons.common.block;

import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.beacon.IBeaconBase;
import kihira.playerbeacons.common.Beacon;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.tileentity.TileEntityMultiBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDefiledSoulConductor extends BlockMultiBlock implements IBeaconBase {

	public BlockDefiledSoulConductor() {
		super(Material.rock);
		this.setHardness(15F);
        this.setResistance(100F);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setBlockName("defiledSoulConductor");
        this.setBlockTextureName(PlayerBeacons.MOD_ID.toLowerCase() + ":pyramidBrick");
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
    public float getCorruptionReduction(Beacon beacon, int blockCount) {
        return beacon.getLevels() * 10F;
    }

    @Override
    public IBeacon getBeacon(World world, int x, int y, int z) {
        TileEntityMultiBlock multiBlock = (TileEntityMultiBlock) world.getTileEntity(x, y, z);
        if (multiBlock.hasParent && multiBlock.isParentValid()) {
            return (IBeacon) world.getTileEntity(multiBlock.parentX, multiBlock.parentY, multiBlock.parentZ);
        }
        return null;
    }

    @Override
    public void setBeacon(World world, int x, int y, int z, IBeacon theBeacon) {
        TileEntityMultiBlock multiBlock = (TileEntityMultiBlock) world.getTileEntity(x, y, z);
        multiBlock.setParent((TileEntityMultiBlock) theBeacon); //TODO cast check
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityMultiBlock();
    }
}
