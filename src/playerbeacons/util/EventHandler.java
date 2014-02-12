package playerbeacons.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import org.lwjgl.opengl.GL11;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.CrystalItem;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.Random;

public class EventHandler {

	private final Random random = new Random();
	private long spawnCooldown = System.currentTimeMillis();

	@ForgeSubscribe
	public void onDeath(LivingDeathEvent e) {
		Entity entity = e.source.getEntity();
		Entity deadEntity = e.entity;

		//Death by DamageBehead
		if (e.source instanceof DamageBehead) {
			if (deadEntity instanceof EntityPlayer) deadEntity.entityDropItem(Util.getHead(3, ((EntityPlayer) deadEntity).username), 1);
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
                if ((random.nextInt()) % (12/lvl) == 0) {
                    if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(Util.getHead(2, null), 1);
                    else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(Util.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
                    else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(Util.getHead(4, null), 1);
                    else if (deadEntity instanceof EntityPlayer) {
                        EntityPlayer deadPlayer = (EntityPlayer)deadEntity;
                        e.setCanceled(true);
                        MinecraftServer.getServer().getConfigurationManager().sendChatMsg(ChatMessageComponent.createFromText(deadPlayer.username + " was beheaded by " + attacker.username));
                        e.entityLiving.entityDropItem(Util.getHead(3, deadPlayer.username), 1);
                    }
                }
            }
		}
	}

	@ForgeSubscribe
	public void onEntitySpawn(LivingSpawnEvent e) {
		if (PlayerBeacons.config.enableZombieHead && e.entityLiving instanceof EntityZombie) {
			EntityZombie entityZombie = (EntityZombie) e.entity;
			if (!entityZombie.isVillager() && (random.nextInt(1001) < 5) && (this.spawnCooldown - System.currentTimeMillis()) <= 0) {
				if (entityZombie.worldObj.playerEntities.size() > 0) {
					int i = random.nextInt(entityZombie.worldObj.playerEntities.size());
					EntityPlayer player = (EntityPlayer) entityZombie.worldObj.playerEntities.get(i);
					//spawn within 50 blocks and similar y level
					if ((player.getDistanceToEntity(entityZombie) < 50) && (player.posY - entityZombie.posY < 5)) {
						entityZombie.setCurrentItemOrArmor(4, Util.getHead(3, player.username));
                        entityZombie.setEquipmentDropChance(4, 100);
						this.spawnCooldown = System.currentTimeMillis() + PlayerBeacons.config.spawnCooldownDuration * 1000L;
					}
				}
			}
		}
	}

	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void onBlockDrawHighlight(DrawBlockHighlightEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
		if (e.target != null && mc.thePlayer != null && !mc.gameSettings.hideGUI) {
			if ((e.target.typeOfHit == EnumMovingObjectType.TILE) && (mc.thePlayer.getCurrentItemOrArmor(0) != null) && (mc.thePlayer.getCurrentItemOrArmor(0).getItem() instanceof CrystalItem)) {
				TileEntity tileEntity = mc.theWorld.getBlockTileEntity(e.target.blockX , e.target.blockY, e.target.blockZ);
				if (tileEntity != null && tileEntity instanceof TileEntityPlayerBeacon) {
					TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) tileEntity;
					float corruption = tileEntityPlayerBeacon.getCorruption();
					String owner = tileEntityPlayerBeacon.getOwner();
					if (e.target.blockX == tileEntityPlayerBeacon.xCoord && e.target.blockY == tileEntityPlayerBeacon.yCoord && e.target.blockZ == tileEntityPlayerBeacon.zCoord) {
						double viewX = e.target.blockX - RenderManager.renderPosX;
						double viewY = e.target.blockY - RenderManager.renderPosY;
						double viewZ = e.target.blockZ - RenderManager.renderPosZ;
						String string;
						if (corruption >= 15000) string = "Corruption: §4" + String.valueOf(corruption);
						else if (corruption >= 10000) string = "Corruption: §c" + String.valueOf(corruption);
						else if (corruption >= 5000) string = "Corruption: §e" + String.valueOf(corruption);
						else string = "Corruption: " + String.valueOf(corruption);
						this.renderLabel(string, (float) viewX + 0.5F, (float) viewY + 1.8F, (float) viewZ + 0.5F);
						if (owner.equals(" ")) owner = "§kNo-one";
						string = "Bound to: §4" + owner;
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
