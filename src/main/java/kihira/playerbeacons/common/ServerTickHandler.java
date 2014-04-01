package kihira.playerbeacons.common;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import kihira.playerbeacons.tileentity.TileEntityPlayerBeacon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class ServerTickHandler {

    private short cycle = 0;

    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.world.getTotalWorldTime() % 20 == 0) {
            this.cycle++;
            if (event.world.playerEntities != null) {
                List<Object> playerEntities = new ArrayList<Object>(event.world.playerEntities);
                for (Object playerEntity : playerEntities) {
                    EntityPlayer entityPlayer = (EntityPlayer) playerEntity;
                    NBTTagCompound nbtTagCompound = PlayerBeacons.beaconData.loadBeaconInformation(event.world, entityPlayer.getCommandSenderName());
                    if (nbtTagCompound != null) {
                        int x = nbtTagCompound.getInteger("x");
                        int y = nbtTagCompound.getInteger("y");
                        int z = nbtTagCompound.getInteger("z");
                        TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) event.world.getTileEntity(x, y, z);
                        if (tileEntityPlayerBeacon != null) {
                            tileEntityPlayerBeacon.checkBeacon();
                            if (this.cycle % 2 == 0) {
                                if (!PlayerBeacons.config.disableCorruption) {
                                    tileEntityPlayerBeacon.calcPylons();
                                    tileEntityPlayerBeacon.calcCorruption();
                                    tileEntityPlayerBeacon.applyCorruption();
                                }
                                if (tileEntityPlayerBeacon.hasSkull()) tileEntityPlayerBeacon.doEffects();
                                if (this.cycle % 4 == 0) event.world.markBlockForUpdate(x, y, z);
                            }
                            if (this.cycle >= 32000) this.cycle = 0;
                        }
                    }
                }
            }
        }
    }
}
