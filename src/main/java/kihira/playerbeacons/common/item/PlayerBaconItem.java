package kihira.playerbeacons.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.List;

public class PlayerBaconItem extends ItemFood {

    public PlayerBaconItem() {
        super(2, 4, false);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setTextureName("pork");
        this.setUnlocalizedName("playerBacon");
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean par4) {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("PlayerName")) {
            String playerName = itemStack.getTagCompound().getString("PlayerName");
            list.add(StatCollector.translateToLocalFormatted("item.playerBacon.food", playerName));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        super.getSubItems(item, creativeTabs, list);
        NBTTagCompound tagCompound = new NBTTagCompound();
        ItemStack itemStack = new ItemStack(item, 1, 0);
        if (Minecraft.getMinecraft().thePlayer != null) {
            tagCompound.setString("PlayerName", Minecraft.getMinecraft().thePlayer.getCommandSenderName());
            itemStack.setTagCompound(tagCompound);
            list.add(itemStack);
        }
    }
}
