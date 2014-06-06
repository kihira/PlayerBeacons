package kihira.playerbeacons.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;

public class TickHandler {

    public static final HashMap<World, Multimap<EntityPlayer, CorruptionEffect>> activeCorruptionEffects = new HashMap<World, Multimap<EntityPlayer, CorruptionEffect>>();

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isServer() && event.player.worldObj.getTotalWorldTime() % 20 == 0) { //TODO every tick
            IBeacon playerBeacon = BeaconDataHelper.getBeaconForDim(event.player, event.player.dimension);
            if (playerBeacon != null && playerBeacon.getTileEntity().getWorldObj().provider.dimensionId == event.player.dimension &&  playerBeacon.isBeaconValid()) {
                playerBeacon.update();
                this.calculateCorruptionEffects(event.player, playerBeacon, event.player.worldObj);

                event.player.worldObj.markBlockForUpdate(playerBeacon.getTileEntity().xCoord, playerBeacon.getTileEntity().yCoord, playerBeacon.getTileEntity().zCoord);
            }
        }
        else if (event.side.isClient() && PlayerBeacons.config.enableHideParticleEffects && event.player.worldObj.getTotalWorldTime() % 10 == 0) {
            event.player.getDataWatcher().updateObject(7, 0); //TODO Remove in 1.8 as this feature will be in vanilla per potion effect
        }
    }

    private void calculateCorruptionEffects(EntityPlayer player, IBeacon beacon, World world) {
        Multimap<EntityPlayer, CorruptionEffect> playerCurrentEffects = activeCorruptionEffects.containsKey(world) ? activeCorruptionEffects.get(world) : HashMultimap.<EntityPlayer, CorruptionEffect>create();
        //Calculate new corruption effects
        for (CorruptionEffect corruptionEffect : CorruptionEffect.corruptionEffects) {
            if (!playerCurrentEffects.containsEntry(player, corruptionEffect) && corruptionEffect.shouldActivate(player, beacon, world)) {
                playerCurrentEffects.put(player, corruptionEffect);
                corruptionEffect.init(player, beacon);
            }
        }

        //Do the effects and remove any needed
        if (playerCurrentEffects.containsKey(player)) {
            Iterator<CorruptionEffect> corruptionEffects = playerCurrentEffects.get(player).iterator();
            while (corruptionEffects.hasNext()) {
                CorruptionEffect corruptionEffect = corruptionEffects.next();
                if (corruptionEffect.shouldContinue(player, beacon, world)) {
                    corruptionEffect.onUpdate(player, beacon);
                }
                else {
                    corruptionEffect.finish(player, beacon);
                    corruptionEffects.remove();
                }
            }
        }

        activeCorruptionEffects.put(world, playerCurrentEffects);
    }
}
