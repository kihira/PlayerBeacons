package playerbeacons.item;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;

import java.util.List;

public class BeheaderItem extends ItemArmor {

	public BeheaderItem(int id) {
		super(id, EnumArmorMaterial.IRON, 2, 0);
		setCreativeTab(CreativeTabs.tabCombat);
		setUnlocalizedName("Beheader");
		setTextureName("playerbeacon:beheader");
		setMaxDamage(200);
	}

	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if (slot == 0) return "playerbeacon:textures/armour/beheader.png";
		else return null;
	}

	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setString("owner", Minecraft.getMinecraft().thePlayer.username);
		ItemStack itemStack = new ItemStack(par1, 1, 0);
		itemStack.setTagCompound(tagCompound);
		par3List.add(itemStack);
	}

	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!entity.worldObj.isRemote) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer enemyPlayer = (EntityPlayer) entity;
				if (stack.hasTagCompound() && stack.getTagCompound().getString("owner").equals(player.username)) {
					if (!enemyPlayer.capabilities.isCreativeMode) {
						if (enemyPlayer.getCurrentArmor(0) == null) {
							if (stack.getItemDamage() == 0) {
								NBTTagCompound nbtTagCompound = new NBTTagCompound();
								nbtTagCompound.setString("owner", player.username);
								ItemStack itemStack = new ItemStack(PlayerBeacons.beheaderItem);
								itemStack.setTagCompound(nbtTagCompound);
								enemyPlayer.setCurrentItemOrArmor(4, itemStack);
								player.setCurrentItemOrArmor(0, null);
							}
							else player.sendChatToPlayer(ChatMessageComponent.createFromText("§3This device needs to be reset before it can claim a victim"));
						}
						else player.sendChatToPlayer(ChatMessageComponent.createFromText("§3The players helmet prevents you from doing that!"));
					}
				}
				else player.sendChatToPlayer(ChatMessageComponent.createFromText("§3You need to bind this device to you"));
			}
		}
		return true;
	}

	public void onArmorTickUpdate(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				switch (itemStack.getItemDamage()) {
					case 1: {
						if (itemStack.hasTagCompound()) {
							String owner = itemStack.getTagCompound().getString("owner");
							if (owner != null) player.sendChatToPlayer(ChatMessageComponent.createFromText("§6" + owner + " has clamped a strange device clamp around your head"));
							else player.sendChatToPlayer(ChatMessageComponent.createFromText("§6You feel a strange device clamp around your head"));
						}
						else player.sendChatToPlayer(ChatMessageComponent.createFromText("§6You feel a strange device clamp around your head"));
					} break;
					case 100:
						player.sendChatToPlayer(ChatMessageComponent.createFromText("§6The device tightens, as you hear it power up"));
						player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200));
						player.addPotionEffect(new PotionEffect(Potion.confusion.id, 200));
						break;
					case 200:
						player.sendChatToPlayer(ChatMessageComponent.createFromText("§6With a quick slash, you suddenly find yourself without your head"));
						player.setCurrentItemOrArmor(4, null);
						player.attackEntityFrom(new DamageBehead(), 100);
						break;
				}
				itemStack.setItemDamage(itemStack.getItemDamage() + 1);
			}
		}
	}

	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		if (!par2World.isRemote) {
			if (par1ItemStack.getItem() instanceof BeheaderItem) {
				if (!par1ItemStack.hasTagCompound()) {
					NBTTagCompound tagCompound = new NBTTagCompound();
					tagCompound.setString("owner", par3EntityPlayer.username);
					par1ItemStack.setTagCompound(tagCompound);
					par3EntityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("§3This device has now been bound to you"));
				}
				if (par1ItemStack.getItemDamage() != 0) {
					ItemStack itemStack = new ItemStack(PlayerBeacons.beheaderItem);
					NBTTagCompound nbtTagCompound = new NBTTagCompound();
					nbtTagCompound.setString("owner", par3EntityPlayer.username);
					itemStack.setTagCompound(nbtTagCompound);
					par3EntityPlayer.setCurrentItemOrArmor(0, null);
					par3EntityPlayer.setCurrentItemOrArmor(0, itemStack);
					par3EntityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("§3The device clicks, it seems to have reset"));
					return itemStack;
				}
			}
		}
		return par1ItemStack;
	}

	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);
		list.add("A cruel device,");
		list.add("this will behead");
		list.add("anyone who wears it");
		list.add(" ");
		if (itemStack.hasTagCompound()) {
			String owner = itemStack.getTagCompound().getString("owner");
			list.add("Owner: §4" + owner);
		}
		else {
			list.add("Right click to bind");
		}
	}

	public int getItemEnchantability() {
		return 0;
	}
}
