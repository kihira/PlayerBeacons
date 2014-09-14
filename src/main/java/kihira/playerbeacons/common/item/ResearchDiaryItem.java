package kihira.playerbeacons.common.item;

import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ResearchDiaryItem extends ItemBook {

    public ResearchDiaryItem() {
        this.setMaxStackSize(1);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setUnlocalizedName(ModItems.Names.RESEARCH_DIARY);
        this.setTextureName("book_normal");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        player.openGui(PlayerBeacons.instance, 0, world, 0, 0, 0);
        return itemStack;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }
}
