package kihira.playerbeacons.common.tileentity;

public class TileEntityDummy extends TileEntityMultiBlock {
    @Override
    public boolean checkStructure() {
        return this.hasParent && this.isParentValid() && ((TileEntityMultiBlock)
                this.worldObj.getTileEntity(this.parentX, this.parentY, this.parentZ)).checkStructure();
    }

    @Override
    public void invalidateStructure() {
        if (this.hasParent && this.isParentValid()) {
            ((TileEntityMultiBlock) this.worldObj.getTileEntity(this.parentX, this.parentY, this.parentZ)).invalidateStructure();
        }
    }

    @Override
    public void formStructure() {
        if (this.hasParent && this.isParentValid()) {
            ((TileEntityMultiBlock) this.worldObj.getTileEntity(this.parentX, this.parentY, this.parentZ)).formStructure();
        }
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
