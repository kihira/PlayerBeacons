package kihira.playerbeacons.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.throttle.ICrystal;
import kihira.playerbeacons.common.DamageBehead;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class EventHandler {

	private final Random random = new Random();
	private long spawnCooldown = System.currentTimeMillis();

	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		Entity entity = e.source.getEntity();
		Entity deadEntity = e.entity;

		//Death by DamageBehead
		if (e.source instanceof DamageBehead) {
			if (deadEntity instanceof EntityPlayer) deadEntity.entityDropItem(Util.getHead(3, deadEntity.getCommandSenderName()), 1);
			else if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(Util.getHead(2, null), 1);
			else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(Util.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
			else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(Util.getHead(4, null), 1);
            return;
		}

		//Death with enchantment
		if (entity instanceof EntityPlayer) {
			EntityPlayer attacker = (EntityPlayer) entity;
			ItemStack item = attacker.getHeldItem();
			if (item != null) {
                int lvl = EnchantmentHelper.getEnchantmentLevel(PlayerBeacons.config.decapitationEnchantmentID, item);
                Random random = new Random();
                if (lvl > 0 && (random.nextInt()) % (12/lvl) == 0) {
                    if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(Util.getHead(2, null), 1);
                    else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(Util.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
                    else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(Util.getHead(4, null), 1);
                    else if (deadEntity instanceof EntityPlayer) {
                        EntityPlayer deadPlayer = (EntityPlayer)deadEntity;
                        e.setCanceled(true);
                        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText(deadPlayer.getCommandSenderName() + " was beheaded by " + attacker.getCommandSenderName()));
                        e.entityLiving.entityDropItem(Util.getHead(3, deadPlayer.getCommandSenderName()), 1);
                    }
                }
            }
		}
	}

	@SubscribeEvent
	public void onEntitySpawn(LivingSpawnEvent e) {
		if (PlayerBeacons.config.enableZombieHead && e.entityLiving instanceof EntityZombie) {
			EntityZombie entityZombie = (EntityZombie) e.entity;
			if (!entityZombie.isVillager() && (this.random.nextInt(1001) < 5) && (this.spawnCooldown - System.currentTimeMillis()) <= 0) {
				if (entityZombie.worldObj.playerEntities.size() > 0) {
					int i = this.random.nextInt(entityZombie.worldObj.playerEntities.size());
					EntityPlayer player = (EntityPlayer) entityZombie.worldObj.playerEntities.get(i);
					//spawn within 50 blocks and similar y level
					if ((player.getDistanceToEntity(entityZombie) < 50) && (player.posY - entityZombie.posY < 5)) {
						entityZombie.setCurrentItemOrArmor(4, Util.getHead(3, player.getCommandSenderName()));
                        entityZombie.setEquipmentDropChance(4, 100);
						this.spawnCooldown = System.currentTimeMillis() + PlayerBeacons.config.spawnCooldownDuration * 1000L;
					}
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onBlockDrawHighlight(DrawBlockHighlightEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		if (e.target != null && mc.thePlayer != null && !mc.gameSettings.hideGUI) {
			if ((e.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (mc.thePlayer.getCurrentEquippedItem() != null) && (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ICrystal)) {
				TileEntity tileEntity = mc.theWorld.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ);
				if (tileEntity != null && tileEntity instanceof IBeacon) {
                    IBeacon tileEntityPlayerBeacon = (IBeacon) tileEntity;
					float corruption = tileEntityPlayerBeacon.getCorruption();
					String owner = tileEntityPlayerBeacon.getOwner();
					if (e.target.blockX == tileEntityPlayerBeacon.getTileEntity().xCoord && e.target.blockY == tileEntityPlayerBeacon.getTileEntity().yCoord && e.target.blockZ == tileEntityPlayerBeacon.getTileEntity().zCoord) {
						double viewX = e.target.blockX - RenderManager.renderPosX;
						double viewY = e.target.blockY - RenderManager.renderPosY;
						double viewZ = e.target.blockZ - RenderManager.renderPosZ;
						String string;
						if (corruption >= 15000) string = "Corruption: \u00a74" + String.valueOf(corruption);
						else if (corruption >= 10000) string = "Corruption: \u00a7c" + String.valueOf(corruption);
						else if (corruption >= 5000) string = "Corruption: \u00a7e" + String.valueOf(corruption);
						else string = "Corruption: " + String.valueOf(corruption);
						this.renderLabel(string, (float) viewX + 0.5F, (float) viewY + 1.8F, (float) viewZ + 0.5F);
						if (owner.equals(" ")) owner = "\u00a7kNo-one";
						string = "Bound to: \u00a74" + owner;
						this.renderLabel(string, (float) viewX + 0.5F, (float) viewY + 2.0F, (float) viewZ + 0.5F);
					}
				}
			}
		}
	}

	private void renderLabel(String string, float viewX, float viewY, float viewZ) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		RenderManager renderManager = RenderManager.instance;
		float f1 = 0.016666668F * 1.6F;
		GL11.glPushMatrix();
		GL11.glTranslatef(viewX, viewY, viewZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		byte b0 = 0;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		int j = fontRenderer.getStringWidth(string) / 2;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex((double)(-j - 1), (double)(-1 + b0), 0.0D);
		tessellator.addVertex((double)(-j - 1), (double)(8 + b0), 0.0D);
		tessellator.addVertex((double)(j + 1), (double)(8 + b0), 0.0D);
		tessellator.addVertex((double)(j + 1), (double)(-1 + b0), 0.0D);
		tessellator.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, b0, 553648127);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, b0, -1);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}
}
