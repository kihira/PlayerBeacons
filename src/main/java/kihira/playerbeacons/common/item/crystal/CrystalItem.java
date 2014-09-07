package kihira.playerbeacons.common.item.crystal;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.buff.Buff;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public abstract class CrystalItem extends Item implements ICrystal {

	private IIcon blankCrystal;
	private IIcon crystalOverlay;
    protected float[] rgba = new float[] {1, 1, 1, 1};

	public CrystalItem() {
		//This equals one day in real time. Change it depending on how fast we calculate bad render
		this.setMaxDamage(43200);
        this.setMaxStackSize(1);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setUnlocalizedName(ModItems.Names.CRYSTAL);
        this.setTextureName(ModItems.Names.getTextureName(ModItems.Names.CRYSTAL));
	}

	@Override
    @SuppressWarnings("unchecked")
	public void addInformation(ItemStack itemStack, EntityPlayer player, List par3List, boolean par4) {
		List<Buff> buffList = this.getAffectedBuffs();
		if (buffList != null) {
			par3List.add("Enables Buffs: ");
			for (Buff buffName : buffList) {
				par3List.add(StatCollector.translateToLocal("buff." + buffName.buffName + ".name"));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerIcons(IIconRegister iconRegister) {
		this.blankCrystal = iconRegister.registerIcon(this.getIconString() + "_blank");
		this.crystalOverlay = iconRegister.registerIcon(this.getIconString() + "_overlay");
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIconFromDamageForRenderPass(int par1, int par2) {
		return par2 == 0 ? this.crystalOverlay : this.blankCrystal;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getColorFromItemStack(ItemStack itemStack, int par2) {
		int i = 0;
        float[] rgba = this.getRGBA();
        if (rgba != null && rgba.length == 4) {
            i = i | ((int) (rgba[3] * 255) << 24);
            i = i | ((int) (rgba[0] * 255) << 16);
            i = i | ((int) (rgba[1] * 255) << 8);
            i = i | ((int) (rgba[2] * 255));
        }
		return i;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

    @Override
    @SideOnly(Side.CLIENT)
    public float[] getRGBA() {
        return this.rgba;
    }

/*    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.openGui(PlayerBeacons.instance, 0, world, 0, 0, 0);
        return itemStack;
    }*/
}
