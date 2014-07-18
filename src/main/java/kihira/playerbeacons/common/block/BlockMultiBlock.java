package kihira.playerbeacons.common.block;

import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.common.tileentity.TileEntityMultiBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public abstract class BlockMultiBlock extends BlockContainer {

    protected BlockMultiBlock(Material material) {
        super(material);
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        if (!world.isRemote) {
            //Revalidate the structure
            TileEntityMultiBlock tileEntity = (TileEntityMultiBlock) world.getTileEntity(x, y, z);

            //Only make the parent recheck, children are basically dummies
            if (tileEntity.hasParent && !tileEntity.isParent) {
                if (tileEntity.isParentValid()) {
                    IBeacon parent = (IBeacon) world.getTileEntity(tileEntity.parentX, tileEntity.parentY, tileEntity.parentZ);
                    BeaconDataHelper.markBeaconDirty(parent);
                }
                else {
                    //Parent doesn't exist, invalidate ourself
                    tileEntity.setParent(null);
                    world.notifyBlockChange(x, y, z, this);
                }
            }
        }
    }
}
