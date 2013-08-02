package playerbeacons.item;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;

import java.util.List;


public class BeheaderItem extends ItemArmor {

	private int ticks = 0;

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
				enemyPlayer.setCurrentItemOrArmor(4, new ItemStack(PlayerBeacons.beheaderItem));
				player.setCurrentItemOrArmor(0, null);
			}
		}
		// =( It still doesn't tick on enemies. Only way around this is using ITickHandler
		/*
		if (entity instanceof EntityZombie) {
			EntityZombie enemyPlayer = (EntityZombie) entity;
			if (enemyPlayer.getCurrentItemOrArmor(4) == null) {
				enemyPlayer.setCurrentItemOrArmor(4, new ItemStack(PlayerBeacons.beheaderItem));
				player.setCurrentItemOrArmor(0, null);
			}
		}
		*/
		return true;
	}

	@Override
	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			ticks++;
			switch (ticks) {
				case 1: FMLClientHandler.instance().getClient().thePlayer.addChatMessage("You feel a strange device clamp around your head"); break;
				case 100: FMLClientHandler.instance().getClient().thePlayer.addChatMessage("I don't think this is very good"); break;
				case 200:
						FMLClientHandler.instance().getClient().thePlayer.addChatMessage("Welp");
						ItemStack newItemStack = new ItemStack(Item.skull, 1, 3);
						NBTTagCompound tag = new NBTTagCompound();
						System.out.println(player.username);
						tag.setString("SkullOwner", player.username);
						itemStack.setTagCompound(tag);
						player.setCurrentItemOrArmor(4, newItemStack);
						player.attackEntityFrom(new DamageBehead(), 100);
						ticks = 0;
				break;
			}
		}
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
