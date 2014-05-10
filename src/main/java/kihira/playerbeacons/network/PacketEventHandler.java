package kihira.playerbeacons.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;

public class PacketEventHandler {

    public static enum Message {
        CORRUPTION(1);

        public final int id;

        Message(int i) {
            this.id = i;
        }
    }

    @SubscribeEvent
    public void onServerPacket(FMLNetworkEvent.ServerCustomPacketEvent e) {
        ByteBuf payload = e.packet.payload();
        int id = payload.readInt();

        if (id == Message.CORRUPTION.id) {
            PlayerBeacons.logger.error("Received a corruption update packet on the server for %s, this isn't supposed to happen!", ByteBufUtils.readUTF8String(payload));
        }
    }

    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent e) {
        ByteBuf payload = e.packet.payload();
        int id = payload.readInt();

        if (id == Message.CORRUPTION.id) {
            System.out.println("Got a corruption update packet!");
            String playerName = ByteBufUtils.readUTF8String(payload);
            float corr = payload.readFloat();
            WorldClient world = Minecraft.getMinecraft().theWorld;
            EntityPlayer player = world.getPlayerEntityByName(playerName);

            if (player != null) {
                player.getEntityData().setFloat("corruption", corr);
            }
        }
    }

    public static FMLProxyPacket createCorruptionMessage(String playerName, float corr) {
        ByteBuf payload = Unpooled.buffer();
        payload.writeInt(Message.CORRUPTION.id);

        ByteBufUtils.writeUTF8String(payload, playerName);
        payload.writeFloat(corr);

        return new FMLProxyPacket(payload, "PlayerBeacons");
    }
}
