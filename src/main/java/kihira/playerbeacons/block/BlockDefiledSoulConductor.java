package kihira.playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.world.World;
import kihira.playerbeacons.common.PlayerBeacons;

public class BlockDefiledSoulConductor extends Block {

	public BlockDefiledSoulConductor(int id) {
		super(id, Material.rock);
		setHardness(15f);
		setResistance(100.0F);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setUnlocalizedName("defiledSoulConductor");
		setTextureName("playerbeacon:pyramidBrick");
	}

    public String getItemIconName() {
        return null;
    }

	public boolean isBeaconBase(World worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return true;
	}

	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}
}
