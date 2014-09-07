package kihira.playerbeacons.common.item.itemblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.common.block.BlockDefiledSoulPylon;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class DefiledSoulPylonItemBlock extends ItemBlock {

    public DefiledSoulPylonItemBlock(Block block) {
        super(block);
        this.setMaxDamage(0);
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        return super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, BlockDefiledSoulPylon.getPylonType(world, x, y, z));
    }

    @Override
    public int getMetadata(int damageValue) {
        return damageValue;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        list.add(StatCollector.translateToLocal("block.soulpylon.info.1"));
    }
}
