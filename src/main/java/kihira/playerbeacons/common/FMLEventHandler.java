package kihira.playerbeacons.common;

import com.google.common.collect.MapMaker;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.AbstractBeacon;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.network.PacketEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FMLEventHandler {

    //We can't do weak multimaps so we do this old system instead
    public static final Map<EntityPlayer, List<CorruptionEffect>> activeCorruptionEffects = new MapMaker().weakKeys().makeMap();

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START && event.side.isServer()) {
            AbstractBeacon beacon = BeaconDataHelper.getBeaconForDim(event.player, event.player.dimension);
            EntityPlayer player = event.player;
            //Check player has a beacon, is in same dimension and that the beacon is active
            if (beacon != null && beacon.dimID == player.dimension && beacon.getLevels() > 0) {
                //General update method, usually used to do effects
                beacon.doEffects(player);

                //Update the corruption count
                if (!PlayerBeacons.config.disableCorruption && event.player.worldObj.getTotalWorldTime() % 20 == 0) {
                    float oldCorr = BeaconDataHelper.getPlayerCorruptionAmount(player);
                    BeaconDataHelper.modifyCorruptionAmount(player, beacon.getCorruption());
                    float newCorr = BeaconDataHelper.getPlayerCorruptionAmount(player);

                    //Send corruption update
                    FMLProxyPacket packet = PacketEventHandler.createCorruptionMessage(player.getCommandSenderName(), newCorr, oldCorr);
                    PlayerBeacons.eventChannel.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 64));
                }
            }

            //We only calculate new corruption effects every 20 ticks and regardless of beacon
            if (!PlayerBeacons.config.disableCorruption && event.player.worldObj.getTotalWorldTime() % 20 == 0) {
                //Calculate the corruption effects on the player
                this.calculateCorruptionEffects(event.player, event.player.worldObj);
            }
        }
        else if (event.side.isClient() && PlayerBeacons.config.enableHideParticleEffects && event.player.worldObj.getTotalWorldTime() % 10 == 0) {
            //This is the colour of the potion particle effect, setting to 0 removes it
            event.player.getDataWatcher().updateObject(7, 0); //TODO Remove in 1.8 as this feature will be in vanilla per potion effect
        }
    }

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equals(PlayerBeacons.MOD_ID)) {
            PlayerBeacons.config.loadGeneral();
        }
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        //Loads the beacon on player login
        if (event.player != null) {
            BeaconDataHelper.getBeaconForDim(event.player, event.player.worldObj.provider.dimensionId);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        //Unload the beacon for the player on logout
        if (event.player != null) {
            BeaconDataHelper.unloadBeacon(BeaconDataHelper.getBeaconForDim(event.player, event.player.worldObj.provider.dimensionId));
        }
    }

    @SubscribeEvent
    public void onPlayerSwitchDim(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player != null) {
            BeaconDataHelper.unloadBeacon(BeaconDataHelper.getBeaconForDim(event.player, event.fromDim));
            BeaconDataHelper.getBeaconForDim(event.player, event.toDim);
        }
    }

    private void calculateCorruptionEffects(EntityPlayer player, World world) {
        List<CorruptionEffect> playerCurrentEffects = activeCorruptionEffects.get(player);
        float corruption = BeaconDataHelper.getPlayerCorruptionAmount(player);
        //Calculate new corruption effects
        for (CorruptionEffect corruptionEffect : CorruptionEffect.corruptionEffects) {
            //If it is currently not active and can be applied to the player
            if (!playerCurrentEffects.contains(corruptionEffect) && corruptionEffect.shouldActivate(player, world, corruption)) {
                playerCurrentEffects.add(corruptionEffect);
                corruptionEffect.init(player, corruption);
                PlayerBeacons.logger.debug("Started corruption %s for %s", corruptionEffect, player);
            }
        }

        //Check we have the player in the list
        if (playerCurrentEffects.size() > 0) {
            //Iterator is used here as we can modify it whilst looping through without it throwing a ConcurrentModificationException
            Iterator<CorruptionEffect> corruptionEffects = playerCurrentEffects.iterator();
            while (corruptionEffects.hasNext()) {
                CorruptionEffect corruptionEffect = corruptionEffects.next();
                //Check if the effect should continue functioning
                if (corruptionEffect.shouldContinue(player, world, corruption)) {
                    corruptionEffect.onUpdate(player, corruption);
                }
                //Otherwise we stop it
                else {
                    corruptionEffect.finish(player, corruption);
                    corruptionEffects.remove();
                    PlayerBeacons.logger.debug("Finished corruption %s for %s", corruptionEffect, player);
                }
            }
        }

        //Add in the modified list back into the main map
        activeCorruptionEffects.put(player, playerCurrentEffects);
    }
}
