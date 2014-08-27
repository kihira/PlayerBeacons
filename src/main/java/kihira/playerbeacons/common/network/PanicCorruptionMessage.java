package kihira.playerbeacons.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class PanicCorruptionMessage implements IMessage {

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public class PanicCorruptionMessageHandler implements IMessageHandler<IMessage, PanicCorruptionMessage> {

        @Override
        public PanicCorruptionMessage onMessage(IMessage message, MessageContext ctx) {
            return null;
        }
    }
}
