package playerbeacons.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;

import java.util.Random;

public class EventHandler {

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
							//TODO Improve drop algorithm?
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

		if ((deadEntity instanceof EntityPlayer) && (e.source instanceof DamageBehead)) {
			EntityPlayer deadThing = (EntityPlayer) deadEntity;
			ItemStack itemStack = new ItemStack(Item.skull, 1, 3);
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("SkullOwner", deadThing.username);
			itemStack.setTagCompound(tag);
			deadThing.entityDropItem(itemStack, 1);
		}
	}
}
