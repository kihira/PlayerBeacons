package kihira.playerbeacons.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CorruptionUpdateMessage implements IMessage {

    private String playerName;
    private float newCorr;
    private float oldCorr;

    public CorruptionUpdateMessage() {}
    public CorruptionUpdateMessage(String playerName, float newCorr, float oldCorr) {

        this.playerName = playerName;
        this.newCorr = newCorr;
        this.oldCorr = oldCorr;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerName = ByteBufUtils.readUTF8String(buf);
        this.newCorr = buf.readFloat();
        this.oldCorr = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.playerName);
        buf.writeFloat(this.newCorr);
        buf.writeFloat(this.oldCorr);
    }

    public class CorruptionUpdateMessageHandler implements IMessageHandler<CorruptionUpdateMessage, IMessage> {

        @Override
        public IMessage onMessage(CorruptionUpdateMessage message, MessageContext ctx) {
            AbstractClientPlayer player = (AbstractClientPlayer) Minecraft.getMinecraft().theWorld.getPlayerEntityByName(message.playerName);

            if (player != null) {
                if (message.newCorr == 0) PlayerBeacons.proxy.restorePlayerSkin(player);
                else PlayerBeacons.proxy.corruptPlayerSkin(player, (int) message.newCorr, (int) message.oldCorr);

                if (Minecraft.getMinecraft().thePlayer.getCommandSenderName().equals(message.playerName)) {
                    ClientProxy.playerCorruption = message.newCorr;
                }
            }
            return null;
        }
    }
}
