package kihira.playerbeacons.common.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.TickHandler;
import kihira.playerbeacons.common.item.PlayerBaconItem;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class EventHandler {

	private final Random random = new Random();
	private long spawnCooldown = System.currentTimeMillis();
    private final ResourceLocation vignetteTexPath = new ResourceLocation("playerbeacon", "textures/misc/vignette.png");

	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		Entity entity = e.source.getEntity();
		Entity deadEntity = e.entity;

		//Death by DamageBehead
		if (e.source == PlayerBeacons.damageBehead) {
			if (deadEntity instanceof EntityPlayer) deadEntity.entityDropItem(Util.getHead(Util.EnumHeadType.PLAYER, deadEntity.getCommandSenderName()), 1);
			else if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(Util.getHead(Util.EnumHeadType.ZOMBIE, null), 1);
			else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(Util.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
			else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(Util.getHead(Util.EnumHeadType.CREEPER, null), 1);
            return;
		}

		//Death with enchantment
		if (entity instanceof EntityPlayer) {
			EntityPlayer attacker = (EntityPlayer) entity;
			ItemStack item = attacker.getHeldItem();
			if (item != null) {
                int lvl = EnchantmentHelper.getEnchantmentLevel(PlayerBeacons.config.decapitationEnchantmentID, item);
                Random random = new Random();
                if (lvl > 0 && (random.nextInt()) % (12 / lvl) == 0) {
                    if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(Util.getHead(Util.EnumHeadType.ZOMBIE, null), 1);
                    else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(Util.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
                    else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(Util.getHead(Util.EnumHeadType.CREEPER, null), 1);
                    else if (deadEntity instanceof EntityPlayer) {
                        EntityPlayer deadPlayer = (EntityPlayer) deadEntity;
                        deadPlayer.func_110142_aN().func_94547_a(PlayerBeacons.damageBehead, 1, 1); //Sets last damage as beheading so it displays our message instead
                        e.entityLiving.entityDropItem(Util.getHead(Util.EnumHeadType.PLAYER, deadPlayer.getCommandSenderName()), 1);
                    }
                }
            }
		}
	}

    @SubscribeEvent
    public void onPlayerName(PlayerEvent.NameFormat e) {
        System.out.println("pls");
        EntityPlayer player = e.entityPlayer;
        String username = e.username;
        StringBuilder nameNew = new StringBuilder();

        //  if (player.playerIsAdmin)
        {
            e.displayname =  nameNew.append(EnumChatFormatting.GOLD).append(username).toString();
        }
    }

    @SubscribeEvent
    public void onPlayerDrops(PlayerDropsEvent e) {
        //Bacon!
        e.drops.add(new EntityItem(e.entityPlayer.worldObj, e.entityPlayer.posX, e.entityPlayer.posY, e.entityPlayer.posZ, Util.getPlayerBacon(e.entityPlayer.getCommandSenderName(), e.lootingLevel + 1)));
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
						entityZombie.setCurrentItemOrArmor(4, Util.getHead(Util.EnumHeadType.PLAYER, player.getCommandSenderName()));
                        entityZombie.setEquipmentDropChance(4, 100);
						this.spawnCooldown = System.currentTimeMillis() + PlayerBeacons.config.spawnCooldownDuration * 1000L;
					}
				}
			}
		}
	}

    @SubscribeEvent
    public void onInteract(EntityInteractEvent e) {
        if (e.target instanceof EntityWolf && e.entityPlayer.getCurrentEquippedItem() != null) {
            ItemStack itemStack = e.entityPlayer.getCurrentEquippedItem();
            //Bacon does funny things to wolves
            if (itemStack.getItem() instanceof PlayerBaconItem && itemStack.hasTagCompound()) {
                EntityWolf entityWolf = (EntityWolf) e.target;
                EntityPlayer target = e.entityPlayer.worldObj.getPlayerEntityByName(itemStack.getTagCompound().getString("PlayerName"));
                //TODO range check?
                if (target != null) {
                    entityWolf.setTamed(false);
                    entityWolf.setAngry(true);
                    entityWolf.setPathToEntity(entityWolf.getNavigator().getPathToEntityLiving(target));
                    entityWolf.setAttackTarget(target);
                    if (!e.entityPlayer.capabilities.isCreativeMode && itemStack.stackSize-- <= 0) e.entityPlayer.setCurrentItemOrArmor(0, null);
                }
            }
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload e) {
        TickHandler.activeCorruptionEffects.remove(e.world);
        if (e.world.isRemote) {
            ClientProxy.playerCorruption = 0F;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre e) {
        if (e.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            float brightness = MathHelper.clamp_float(ClientProxy.playerCorruption / CorruptionEffect.CORRUPTION_MAX, 0F, 1F);
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(false);
            OpenGlHelper.glBlendFunc(0, 769, 1, 0);
            GL11.glColor4f(brightness, brightness, brightness, 1F);
            Minecraft.getMinecraft().getTextureManager().bindTexture(this.vignetteTexPath);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(0.0D, e.resolution.getScaledHeight_double(), -90.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(e.resolution.getScaledWidth_double(), e.resolution.getScaledHeight_double(), -90.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(e.resolution.getScaledWidth_double(), 0.0D, -90.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
            tessellator.draw();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onDebugText(RenderGameOverlayEvent.Text e) {
        if (e.left != null && e.left.size() > 0) {
            e.left.add("");
            e.left.add("Corruption: " + ClientProxy.playerCorruption);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
	public void onBlockDrawHighlight(DrawBlockHighlightEvent e) {
		Minecraft mc = Minecraft.getMinecraft();
        //Check needed stuff isn't null and GUI is enabled
		if (e.target != null && mc.thePlayer != null && !mc.gameSettings.hideGUI) {
			if (e.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				TileEntity tileEntity = mc.theWorld.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ);
                //Check it is a tileentity
				if (tileEntity != null && tileEntity instanceof IBeacon) {
                    IBeacon tileEntityPlayerBeacon = (IBeacon) tileEntity;
					float corruption = tileEntityPlayerBeacon.getCorruption();
					String owner = tileEntityPlayerBeacon.getOwnerUUID();
                    double viewX = e.target.blockX - RenderManager.renderPosX;
                    double viewY = e.target.blockY - RenderManager.renderPosY;
                    double viewZ = e.target.blockZ - RenderManager.renderPosZ;
                    String string;

                    string = StatCollector.translateToLocal("text.corruption") + ": " + String.valueOf(corruption) + "/s\n";
                    if (owner.equals(" ")) owner = "\u00a7kNo-one";
                    string += StatCollector.translateToLocal("text.bound") + ": \u00a74" + owner;
                    this.renderLabel(string, (float) viewX + 0.5F, (float) viewY + 2.0F, (float) viewZ + 0.5F);
                }
			}
		}
	}

	private void renderLabel(String string, float viewX, float viewY, float viewZ) {
		FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
		RenderManager renderManager = RenderManager.instance;
		float f1 = 0.016666668F * 1.2F;
        String[] lines = string.split("\\n");

		GL11.glPushMatrix();
		GL11.glTranslatef(viewX, viewY, viewZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-f1, -f1, f1);
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.instance;
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		tessellator.startDrawingQuads();
		float j = fontRenderer.splitStringWidth(string, 400) * 2.6F;
		tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
		tessellator.addVertex((double)(-j), (double)(-1), 0.0D);
		tessellator.addVertex((double)(-j), (double)(8 * lines.length), 0.0D);
		tessellator.addVertex((double)(j), (double)(8 * lines.length), 0.0D);
		tessellator.addVertex((double)(j), (double)(-1), 0.0D);
		tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        for (int i = 0; i < lines.length; i++) {
            fontRenderer.drawString(lines[i], -fontRenderer.getStringWidth(lines[i]) / 2, 8 * i, -1);
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glPopMatrix();
	}
}
