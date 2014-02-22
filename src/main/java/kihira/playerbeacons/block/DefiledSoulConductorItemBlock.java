package kihira.playerbeacons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class DefiledSoulConductorItemBlock extends ItemBlock {

    public DefiledSoulConductorItemBlock(int id) {
        super(id);
        setMaxDamage(0);
    }

    @Override
    public int getMetadata (int damageValue) {
        return damageValue;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation (ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add(StatCollector.translateToLocal("block.soulconductor.info.1"));
        list.add(StatCollector.translateToLocal("block.soulconductor.info.2"));
    }
}
