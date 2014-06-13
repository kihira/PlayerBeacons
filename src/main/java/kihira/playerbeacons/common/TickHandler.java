package kihira.playerbeacons.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.network.PacketEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Iterator;

public class TickHandler {

    public static final HashMap<World, Multimap<EntityPlayer, CorruptionEffect>> activeCorruptionEffects = new HashMap<World, Multimap<EntityPlayer, CorruptionEffect>>();

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
            IBeacon playerBeacon = BeaconDataHelper.getBeaconForDim(event.player, event.player.dimension);
            //Check player has a beacon, is in same dimension and that the beacon is active
            if (playerBeacon != null && playerBeacon.getTileEntity().getWorldObj().provider.dimensionId == event.player.dimension &&  playerBeacon.isBeaconValid()) {
                //General update method, usually used to do effects
                playerBeacon.update();

                //Update the corruption count
                if (event.player.worldObj.getTotalWorldTime() % 20 == 0) {
                    BeaconDataHelper.modifyCorruptionAmount(event.player, playerBeacon.getCorruption());

                    //Make the block re-sync
                    event.player.worldObj.markBlockForUpdate(playerBeacon.getTileEntity().xCoord, playerBeacon.getTileEntity().yCoord, playerBeacon.getTileEntity().zCoord);
                }
            }

            //We only calculate new corruption effects every 20 ticks and regardless of beacon
            if (event.player.worldObj.getTotalWorldTime() % 20 == 0) {
                //Calculate the corruption effects on the player
                this.calculateCorruptionEffects(event.player, event.player.worldObj);

                //Send corruption update
                FMLProxyPacket packet = PacketEventHandler.createCorruptionMessage(event.player.getCommandSenderName(), BeaconDataHelper.getPlayerCorruptionAmount(event.player));
                PlayerBeacons.eventChannel.sendToAllAround(packet, new NetworkRegistry.TargetPoint(event.player.worldObj.provider.dimensionId, event.player.posX, event.player.posY, event.player.posZ, 64));
            }
        }
        else if (event.side.isClient() && PlayerBeacons.config.enableHideParticleEffects && event.player.worldObj.getTotalWorldTime() % 10 == 0) {
            //This is the colour of the potion particle effect, setting to 0 removes it
            event.player.getDataWatcher().updateObject(7, 0); //TODO Remove in 1.8 as this feature will be in vanilla per potion effect
        }
    }

    private void calculateCorruptionEffects(EntityPlayer player, World world) {
        Multimap<EntityPlayer, CorruptionEffect> playerCurrentEffects = activeCorruptionEffects.containsKey(world) ? activeCorruptionEffects.get(world) : HashMultimap.<EntityPlayer, CorruptionEffect>create();
        //Calculate new corruption effects
        for (CorruptionEffect corruptionEffect : CorruptionEffect.corruptionEffects) {
            if (!playerCurrentEffects.containsEntry(player, corruptionEffect) && corruptionEffect.shouldActivate(player, world)) {
                playerCurrentEffects.put(player, corruptionEffect);
                corruptionEffect.init(player);
            }
        }

        //Do the effects and remove any needed
        if (playerCurrentEffects.containsKey(player)) {
            //Iterator is used here as we can modify it whilst looping through without it throwing a ConcurrentModificationException
            Iterator<CorruptionEffect> corruptionEffects = playerCurrentEffects.get(player).iterator();
            while (corruptionEffects.hasNext()) {
                CorruptionEffect corruptionEffect = corruptionEffects.next();
                //Check if the effect should continue functioning
                if (corruptionEffect.shouldContinue(player, world)) {
                    corruptionEffect.onUpdate(player);
                }
                //Otherwise we stop it
                else {
                    corruptionEffect.finish(player);
                    corruptionEffects.remove();
                }
            }
        }

        activeCorruptionEffects.put(world, playerCurrentEffects);
    }
}
