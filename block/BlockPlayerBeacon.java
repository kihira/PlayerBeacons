package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.proxy.ClientProxy;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.Random;

public class BlockPlayerBeacon extends Block implements ITileEntityProvider {

	public BlockPlayerBeacon(int id) {
		super(id, Material.iron);
		setHardness(8f);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("playerBeaconBlock");
		func_111022_d("playerbeacon:pyramidBrick");
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPlayerBeacon();
	}

	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPlayerBeacon) {
				if ((player.username.equals(((TileEntityPlayerBeacon) tileEntity).getOwner())) || (player.capabilities.isCreativeMode)) {
					PlayerBeacons.beaconData.deleteBeaconInformation(world, ((TileEntityPlayerBeacon) tileEntity).getOwner());
					return world.setBlockToAir(x, y, z);
				}
				else {
					player.attackEntityFrom(new DamageBehead(), 2);
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("A mystical energy seems to guard this device"));
				}
			}
		}
		return false;
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPlayerBeacon) {
				if (!(player.username.equals(((TileEntityPlayerBeacon) tileEntity).getOwner())) || !(player.capabilities.isCreativeMode)) {
					player.attackEntityFrom(new DamageBehead(), 2);
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("A mystical energy seems to guard this device"));
				}
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity instanceof TileEntityPlayerBeacon) {
			((TileEntityPlayerBeacon) tileEntity).initialSetup((EntityPlayer) par5EntityLivingBase);
		}
	}

	//Should I keep? Or maybe adjust it? Wait on nex's design
	//Make this change based on the level of bad stuff?
	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		for (int l = 0; l < 3; ++l) {
			double d1 = (double)((float)par3 + par5Random.nextFloat());
			int i1 = par5Random.nextInt(2) * 2 - 1;
			int j1 = par5Random.nextInt(2) * 2 - 1;
			double d3 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
			double d5 = (double)par4 + 0.5D + 0.25D * (double)j1;
			double d4 = (double)(par5Random.nextFloat() * 1.0F * (float)j1);
			double d6 = (double)par2 + 0.5D + 0.25D * (double)i1;
			double d2 = (double)(par5Random.nextFloat() * 1.0F * (float)i1);
			par1World.spawnParticle("portal", d6, d1, d5, d2, d3, d4);
		}
	}
}
