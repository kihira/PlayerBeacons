package playerbeacons.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import playerbeacons.api.throttle.ICrystal;
import playerbeacons.api.buff.Buff;
import playerbeacons.common.PlayerBeacons;

import java.util.List;

public class CrystalItem extends Item implements ICrystal {

	private Icon blankCrystal;
	private Icon crystalOverlay;

	public CrystalItem(int id) {
		super(id);
		//This equals one day in real time. Change it depending on how fast we calculate bad stuff
		setMaxDamage(43200);
		setMaxStackSize(1);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setTextureName("playerbeacon:crystalitem");
		setUnlocalizedName("crystalitem");
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		List buffList = getAffectedBuffs();
		if (buffList != null) {
			par3List.add("Throttles Buffs: ");
			for (Object obj:buffList) {
				par3List.add(Buff.buffs.get(obj.toString()).getName());
			}
		}
	}

	@Override
	public float getCorruptionThrottle(Buff buff, int beaconLevel, int throttleCount) {
		return 10f * throttleCount;
	}

	@Override
	public float getCorruptionReduction(float currentCorruption, int currentCorruptionLevel, int beaconLevel) {
		return 0;
	}

	@Override
	public List<String> getAffectedBuffs() {
		return null;
	}

	@Override
	public double[] getRGBA() {
		return new double[]{1, 1, 1, 1};
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IconRegister iconRegister) {
		this.blankCrystal = iconRegister.registerIcon(this.getIconString() + "_blank");
		this.crystalOverlay = iconRegister.registerIcon(this.getIconString() + "_overlay");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Icon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 0 ? this.crystalOverlay : this.blankCrystal;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		int i = 0;
		double[] rgba = getRGBA();
		i = i | ((int) (rgba[3] * 255) << 24);
		i = i | ((int) (rgba[0] * 255) << 16);
		i = i | ((int) (rgba[1] * 255) << 8);
		i = i | ((int) (rgba[2] * 255));
		return i;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}
}
