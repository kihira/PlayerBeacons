package playerbeacons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;

import java.util.List;
import java.util.Random;

public class EventHandler {

	private Random random = new Random();

	@ForgeSubscribe
	public void onDeath(LivingDeathEvent e) {
		Entity entity = e.source.getEntity();
		Entity deadEntity = e.entity;

		//Death by enchantment
		if ((deadEntity instanceof EntityPlayer) && (entity instanceof EntityPlayer)) {
			EntityPlayer attacker = (EntityPlayer) entity;
			EntityPlayer deadThing = (EntityPlayer) deadEntity;
			NBTTagList enchantments = attacker.getHeldItem().getEnchantmentTagList();

			if (enchantments != null) {
				for (int i = 0; i < enchantments.tagCount(); ++i) {
					short id = ((NBTTagCompound)enchantments.tagAt(i)).getShort("id");
					short lvl = ((NBTTagCompound)enchantments.tagAt(i)).getShort("lvl");
					if (id == PlayerBeacons.config.decapitationEnchantmentID) {
						Random random = new Random();
						if ((random.nextInt()) % (6/lvl) == 0) {
							ItemStack itemStack = new ItemStack(Item.skull, 1, 3);
							NBTTagCompound tag = new NBTTagCompound();
							tag.setString("SkullOwner", deadThing.username);
							itemStack.setTagCompound(tag);
							e.entityLiving.entityDropItem(itemStack, 1);
						}
						break;
					}
				}
			}
		}

		//Death by DamageBehead
		if ((deadEntity instanceof EntityPlayer) && (e.source instanceof DamageBehead)) {
			EntityPlayer deadThing = (EntityPlayer) deadEntity;
			ItemStack itemStack = new ItemStack(Item.skull, 1, 3);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("SkullOwner", deadThing.username);
			itemStack.setTagCompound(tag);
			deadThing.entityDropItem(itemStack, 1);
		}
	}

	//TODO Change to cloning method?
	//Possible cloning method. Beheader saves information of who last wore. next zombie who wears it gets "cloned" into that player
	@ForgeSubscribe
	public void onEntitySpawn(LivingSpawnEvent e) {
		if (e.entity instanceof EntityZombie) {
			EntityZombie entityZombie = (EntityZombie) e.entity;
			if (random.nextInt(500) == 1) {
				int i = random.nextInt(entityZombie.worldObj.playerEntities.size());
				EntityPlayer player = (EntityPlayer) entityZombie.worldObj.playerEntities.get(i);
				ItemStack itemStack = new ItemStack(Item.skull, 1, 3);
				NBTTagCompound tag = new NBTTagCompound();
				tag.setString("SkullOwner", player.username);
				itemStack.setTagCompound(tag);
				entityZombie.setCurrentItemOrArmor(4, itemStack);
				player.sendChatToPlayer(ChatMessageComponent.func_111066_d("§4§oA chill runs down your spine, you feel oddly attached to something"));
			}
		}
	}
}
