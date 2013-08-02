package playerbeacons.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import playerbeacons.common.PlayerBeacons;

import java.util.List;


public class BeheaderItem extends ItemArmor {

	public BeheaderItem(int id) {
		super(id, EnumArmorMaterial.IRON, 2, 0);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("beheader");
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer enemyPlayer = (EntityPlayer) entity;
			if (enemyPlayer.getCurrentArmor(0) == null) {
				enemyPlayer.setCurrentItemOrArmor(1, new ItemStack(PlayerBeacons.beheaderItem));
				player.setCurrentItemOrArmor(0, null);
			}
		}
		return true;
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {

	}

	//What does par4 do? Metadata?
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);

		list.add("Go on, try it on!");

	}

	//TODO Get nex to do something for this?
	@Override
	@SideOnly(Side.CLIENT)
	public void renderHelmetOverlay(ItemStack stack, EntityPlayer player, ScaledResolution resolution, float partialTicks, boolean hasScreen, int mouseX, int mouseY) {

	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}
}
