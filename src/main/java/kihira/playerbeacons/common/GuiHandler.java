package kihira.playerbeacons.common;

import cpw.mods.fml.common.network.IGuiHandler;
import kihira.playerbeacons.client.gui.GuiDiary;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 0: return new GuiDiary();
            default: return null;
        }
    }
}
