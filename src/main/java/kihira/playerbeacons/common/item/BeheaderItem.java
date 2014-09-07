package kihira.playerbeacons.common.item;

import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

public class BeheaderItem extends ItemArmor {

	public BeheaderItem() {
		super(ArmorMaterial.IRON, 2, 0);
		this.setUnlocalizedName(ModItems.Names.BEHEADER);
        this.setTextureName(ModItems.Names.getTextureName(ModItems.Names.BEHEADER));
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setMaxDamage(200);
	}

    @Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		if (slot == 0) return "playerbeacon:textures/armour/itemBeheader.png";
		else return null;
	}

    @Override
    @SuppressWarnings("unchecked")
	public void getSubItems(Item par1, CreativeTabs creativeTabs, List list) {
        list.add(this.bindAndReset(Minecraft.getMinecraft().thePlayer.getCommandSenderName(), null));
	}

    @Override
	public boolean onLeftClickEntity(ItemStack itemStack, EntityPlayer player, Entity entity) {
		if (!entity.worldObj.isRemote) {
			if (entity instanceof EntityPlayer) {
				EntityPlayer enemyPlayer = (EntityPlayer) entity;
				if (itemStack.hasTagCompound() && itemStack.getTagCompound().getString("owner").equals(player.getCommandSenderName())) {
					if (!enemyPlayer.capabilities.isCreativeMode && MinecraftServer.getServer().isPVPEnabled() && !enemyPlayer.isOnSameTeam(player)) {
						if (enemyPlayer.getCurrentArmor(0) == null) {
							if (itemStack.getItemDamage() == 0) {
                                this.bindAndReset(player.getCommandSenderName(), itemStack);
								enemyPlayer.setCurrentItemOrArmor(4, itemStack);
								player.setCurrentItemOrArmor(0, null);
							}
							else player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.resetneeded")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
						}
						else player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.helmet")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
					}
				}
				else player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.bindneeded")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
			}
		}
		return true;
	}

    @Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
		if (!world.isRemote) {
			if (!player.capabilities.isCreativeMode) {
				switch (itemStack.getItemDamage()) {
					case 1:
						if (itemStack.hasTagCompound()) {
							String owner = itemStack.getTagCompound().getString("owner");
							if (owner != null) player.addChatComponentMessage(new ChatComponentTranslation("item.beheader.stage.0", owner).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
							else player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.stage.0.1")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
						}
						else player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.stage.0.1")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
						break;
					case 100:
						player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.stage.1")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
						player.addPotionEffect(new PotionEffect(Potion.blindness.id, 200));
						player.addPotionEffect(new PotionEffect(Potion.confusion.id, 200));
						break;
					case 200:
						player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.stage.2")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
						player.setCurrentItemOrArmor(4, null);
						player.attackEntityFrom(PlayerBeacons.damageBehead, 100);
						break;
				}
				itemStack.setItemDamage(itemStack.getItemDamage() + 1);
			}
		}
	}

    @Override
	public ItemStack onItemRightClick(ItemStack itemStack, World par2World, EntityPlayer player) {
		if (!par2World.isRemote) {
			if (itemStack.getItem() instanceof BeheaderItem) {
				if (!itemStack.hasTagCompound()) {
                    this.bindAndReset(player.getCommandSenderName(), itemStack);
                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.bind")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
				}
				if (itemStack.getItemDamage() != 0) {
                    this.bindAndReset(player.getCommandSenderName(), itemStack);
                    player.setCurrentItemOrArmor(0, itemStack);
                    player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("item.beheader.reset")).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.DARK_AQUA)));
					return itemStack;
				}
			}
		}
		return itemStack;
	}

    private ItemStack bindAndReset(String name, ItemStack itemStack) {
        if (itemStack == null) itemStack = new ItemStack(ModItems.itemBeheader);
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setString("owner", name);
        itemStack.setTagCompound(nbtTagCompound);
        itemStack.setItemDamage(0);
        return itemStack;
    }

    @Override
    @SuppressWarnings("unchecked")
	public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
		super.addInformation(itemStack, entityPlayer, list, par4);
		list.add(StatCollector.translateToLocal("item.beheader.information.0"));
		list.add(StatCollector.translateToLocal("item.beheader.information.1"));
		list.add(" ");
		if (itemStack.hasTagCompound()) {
			String owner = itemStack.getTagCompound().getString("owner");
			list.add(StatCollector.translateToLocal("item.beheader.information.2") + " " + owner);
		}
		else list.add(StatCollector.translateToLocal("item.beheader.bindneeded"));
	}

    @Override
	public int getItemEnchantability() {
		return 0;
	}
}
