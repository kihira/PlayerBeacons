package kihira.playerbeacons.common.command;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.network.PacketEventHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class CommandPlayerBeacons extends CommandBase {

    @Override
    public String getCommandName() {
        return "pb";
    }

    @Override
    public String getCommandUsage(ICommandSender commandSender) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) {
        if (args.length >= 1) {
            if (args[0].equals("corruption") && args.length >= 2) {
                EntityPlayer player = getCommandSenderAsPlayer(commandSender);
                float newCorr = Float.valueOf(args[1]);
                float oldCorr = BeaconDataHelper.getPlayerCorruptionAmount(player);

                BeaconDataHelper.setPlayerCorruptionAmount(player, newCorr);
                func_152373_a(commandSender, this, "Set corruption to %s", newCorr); //Notify admins

                //Send corruption update
                FMLProxyPacket packet = PacketEventHandler.createCorruptionMessage(player.getCommandSenderName(), newCorr, oldCorr);
                PlayerBeacons.eventChannel.sendToAllAround(packet, new NetworkRegistry.TargetPoint(player.worldObj.provider.dimensionId, player.posX, player.posY, player.posZ, 64));
            }
        }
    }
}
