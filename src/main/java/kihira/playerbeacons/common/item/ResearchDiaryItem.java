package kihira.playerbeacons.common.item;

import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.lib.ModItems;
import net.minecraft.item.Item;

public class ResearchDiaryItem extends Item {

    public ResearchDiaryItem() {
        this.setMaxStackSize(1);
        this.setCreativeTab(PlayerBeacons.tabPlayerBeacons);
        this.setUnlocalizedName(ModItems.Names.RESEARCH_DIARY);
        this.setTextureName("book_normal");
    }
}
