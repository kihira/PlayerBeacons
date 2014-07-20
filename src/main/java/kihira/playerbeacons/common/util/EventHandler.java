package kihira.playerbeacons.common.util;

import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.foxlib.client.RenderHelper;
import kihira.foxlib.common.EnumHeadType;
import kihira.foxlib.common.Loc4;
import kihira.playerbeacons.api.BeaconDataHelper;
import kihira.playerbeacons.api.beacon.IBeacon;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.api.crystal.ICrystal;
import kihira.playerbeacons.common.Beacon;
import kihira.playerbeacons.common.FMLEventHandler;
import kihira.playerbeacons.common.PlayerBeacons;
import kihira.playerbeacons.common.item.PlayerBaconItem;
import kihira.playerbeacons.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.world.WorldEvent;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class EventHandler {

	private final Random random = new Random();
	private long spawnCooldown = System.currentTimeMillis();
    private final ResourceLocation vignetteTexPath = new ResourceLocation(PlayerBeacons.MOD_ID.toLowerCase(), "textures/misc/vignette.png");

	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		Entity entity = e.source.getEntity();
		Entity deadEntity = e.entity;

		//Death by DamageBehead
		if (e.source == PlayerBeacons.damageBehead) {
			if (deadEntity instanceof EntityPlayer) deadEntity.entityDropItem(EnumHeadType.getHead(EnumHeadType.PLAYER, deadEntity.getCommandSenderName()), 1);
			else if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(EnumHeadType.getHead(EnumHeadType.ZOMBIE, null), 1);
			else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(EnumHeadType.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
			else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(EnumHeadType.getHead(EnumHeadType.CREEPER, null), 1);
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
                    if (deadEntity instanceof EntityZombie) deadEntity.entityDropItem(EnumHeadType.getHead(EnumHeadType.ZOMBIE, null), 1);
                    else if (deadEntity instanceof EntitySkeleton) deadEntity.entityDropItem(EnumHeadType.getHead(((EntitySkeleton) deadEntity).getSkeletonType(), null), 1);
                    else if (deadEntity instanceof EntityCreeper) deadEntity.entityDropItem(EnumHeadType.getHead(EnumHeadType.CREEPER, null), 1);
                    else if (deadEntity instanceof EntityPlayer) {
                        EntityPlayer deadPlayer = (EntityPlayer) deadEntity;
                        deadPlayer.func_110142_aN().func_94547_a(PlayerBeacons.damageBehead, 1, 1); //Sets last damage as beheading so it displays our message instead
                        e.entityLiving.entityDropItem(EnumHeadType.getHead(EnumHeadType.PLAYER, deadPlayer.getCommandSenderName()), 1);
                    }
                }
            }
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
						entityZombie.setCurrentItemOrArmor(4, EnumHeadType.getHead(EnumHeadType.PLAYER, player.getCommandSenderName()));
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
        FMLEventHandler.activeCorruptionEffects.remove(e.world);

        //Use iterator to prevent CME
        Iterator<Map.Entry<Loc4, Beacon>> iterator = BeaconDataHelper.beaconMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Loc4, Beacon> entry = iterator.next();
            if (entry.getKey().dimID() == e.world.provider.dimensionId) BeaconDataHelper.beaconMap.remove(entry.getKey());
        }

        if (e.world.isRemote) {
            ClientProxy.playerCorruption = 0F;
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderGameOverlay(RenderGameOverlayEvent.Pre e) {
        if (e.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            float brightness = MathHelper.clamp_float(ClientProxy.playerCorruption / (CorruptionEffect.CORRUPTION_MAX / 2), 0F, 1F);
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
        //Check needed render isn't null and GUI is enabled
		if (e.target != null && mc.thePlayer != null && !mc.gameSettings.hideGUI) {
			if (e.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && e.currentItem != null && e.currentItem.getItem() instanceof ICrystal) {
				TileEntity tileEntity = mc.theWorld.getTileEntity(e.target.blockX, e.target.blockY, e.target.blockZ);
                //Check it is a tileentity
				if (tileEntity != null && tileEntity instanceof IBeacon) {
                    IBeacon tileEntityPlayerBeacon = (IBeacon) tileEntity;
					GameProfile ownerGameProfile = tileEntityPlayerBeacon.getOwnerGameProfile();
                    double viewX = e.target.blockX - RenderManager.renderPosX;
                    double viewY = e.target.blockY - RenderManager.renderPosY;
                    double viewZ = e.target.blockZ - RenderManager.renderPosZ;
                    String string = "";

                    if (ownerGameProfile != null) string += EnumChatFormatting.DARK_PURPLE + ownerGameProfile.getName();
                    RenderHelper.drawMultiLineMessageFacingPlayer(viewX + 0.5D, viewY + 2D, viewZ + 0.5D,
                            RenderHelper.drawWrappedMessageFacingPlayer$default$4() * 1.2F, string.split("\\n"), -1, true, false);
                }
			}
		}
	}
}
