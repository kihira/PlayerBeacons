package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.CrystalItem;
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
				TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) tileEntity;
				if ((player.username.equals(tileEntityPlayerBeacon.getOwner())) || (player.capabilities.isCreativeMode)) {
					//Check the level of bad stuff
					tileEntityPlayerBeacon.doCorruption();
					PlayerBeacons.beaconData.deleteBeaconInformation(world, tileEntityPlayerBeacon.getOwner());
					return world.setBlockToAir(x, y, z);
				}
				else {
					player.attackEntityFrom(new DamageBehead(), 10);
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§d§oA mystical energy seems to guard this device"));
				}
			}
		}
		return false;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int meta, float par7, float par8, float par9) {
		if (!world.isRemote) {
			if (entityPlayer.getCurrentItemOrArmor(0) != null) {
				if (entityPlayer.getCurrentItemOrArmor(0).getItem().itemID == Item.emerald.itemID) {
					entityPlayer.setCurrentItemOrArmor(0, null);
					EntityItem item = new EntityItem(world, x, y + 0.5, z, new ItemStack(PlayerBeacons.crystalItem));
					entityPlayer.sendChatToPlayer(ChatMessageComponent.func_111066_d("§3§oAn energy from the beacon flows into the emerald forging a mysterious crystal"));
					world.spawnEntityInWorld(item);
					return true;
				}
				//If they right click with depleted, disperse all corruption
				else if (entityPlayer.getCurrentItemOrArmor(0).getItem() instanceof CrystalItem) {
					entityPlayer.setCurrentItemOrArmor(0, null);
					TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getBlockTileEntity(x, y, z);
					tileEntityPlayerBeacon.doCorruption();
					tileEntityPlayerBeacon.setCorruption(0, true);
					tileEntityPlayerBeacon.updateContainingBlockInfo();
					entityPlayer.sendChatToPlayer(ChatMessageComponent.func_111066_d("§3§oThe crystal fizzles away as it interacts with the beacon, releasing the corruption from within it"));
					return true;
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
					player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§d§oA mystical energy seems to guard this device"));
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

	//TODO make this work based on corruption level?
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockId(x, y+1, z) == Block.skull.blockID) {
			for (int l = 0; l < 3; ++l) {
				double d1 = (double)((float)y + random.nextFloat());
				int i1 = random.nextInt(2) * 2 - 1;
				int j1 = random.nextInt(2) * 2 - 1;
				double d3 = ((double)random.nextFloat() - 0.5D) * 0.125D;
				double d5 = (double)z + 0.5D + 0.25D * (double)j1;
				double d4 = (double)(random.nextFloat() * 1.0F * (float)j1);
				double d6 = (double)x + 0.5D + 0.25D * (double)i1;
				double d2 = (double)(random.nextFloat() * 1.0F * (float)i1);
				world.spawnParticle("portal", d6, d1, d5, d2, d3, d4);
			}
		}
	}
}
