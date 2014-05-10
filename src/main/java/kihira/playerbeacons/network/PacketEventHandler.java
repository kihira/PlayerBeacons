/**
 * Parts of the following code originally provided by @author Vazkii under
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * and modified by @author Kihira. As such the following code is released
 * under the same licence.
 */
package kihira.playerbeacons.network;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Random;

public class PacketEventHandler {

    private Random rand = new Random();

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
            PlayerBeacons.logger.warn("Received a corruption update packet on the server for %s, this isn't supposed to happen!", ByteBufUtils.readUTF8String(payload));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent e) {
        ByteBuf payload = e.packet.payload();
        int id = payload.readInt();

        if (id == Message.CORRUPTION.id) {
            String playerName = ByteBufUtils.readUTF8String(payload);
            float corr = payload.readFloat();
            World world = Minecraft.getMinecraft().theWorld;
            EntityPlayer player = world.getPlayerEntityByName(playerName);

            if (player != null) {
                PlayerBeacons.proxy.corruptRandomPixels(player, corr);
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
