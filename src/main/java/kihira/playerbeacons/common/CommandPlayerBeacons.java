package kihira.playerbeacons.common;

import kihira.playerbeacons.api.BeaconDataHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

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
                BeaconDataHelper.setPlayerCorruptionAmount(getCommandSenderAsPlayer(commandSender), Float.valueOf(args[1]));
                func_152373_a(commandSender, this, "Set corruption to %s", args[1]); //Notify admins
            }
        }
    }
}
