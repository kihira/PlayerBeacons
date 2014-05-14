package kihira.playerbeacons.common;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config {

	public int decapitationEnchantmentID;
    public int corruptionPotionID;
	public int spawnCooldownDuration;
	public boolean disableCorruption;
	public boolean enableThaumcraft;
	public boolean enableLightning;

	public boolean enableEasterEgg;
	public boolean enableZombieHead;

	private final Configuration config;


	public Config(File file) {
		config = new Configuration(file);
		config.load();
		loadGeneral();
		save();
	}

	private void loadGeneral() {

		Property prop;

		prop = config.get(Configuration.CATEGORY_GENERAL, "Decapitation Enchantment ID", 200);
		decapitationEnchantmentID = prop.getInt();
        prop = config.get(Configuration.CATEGORY_GENERAL, "Corruption Potion ID", 30);
        decapitationEnchantmentID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_GENERAL, "Allow Zombies to spawn with player heads", true);
		prop.comment = "WARNING: Mobs wearing heads do not despawn and may build up over time.";
		enableZombieHead = prop.getBoolean(true);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Enable Easter Egg", false);
		prop.comment = "WARNING: This could destroy parts of your world unintentionally";
		enableEasterEgg = prop.getBoolean(false);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Time between special zombie spawns", 54000);
		prop.comment = "Time between chance to spawn a zombie with a player head. Default: 54000 seconds";
		spawnCooldownDuration = prop.getInt();
		prop = config.get(Configuration.CATEGORY_GENERAL, "Disable Corruption", false);
		prop.comment = "Whether to do corruption calculations or not";
		disableCorruption = prop.getBoolean(false);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Enable Thaumcraft Intergration", true);
		prop.comment = "If this is enabled and Thaumcraft is detected, the default recipes will be replaced with TC research and recipes";
		enableThaumcraft = prop.getBoolean(true);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Enable fancy lightning", false);
		prop.comment = "EXPERIMENTAL. This feature isn't finished so is disabled by default";
		enableLightning = prop.getBoolean(false);
	}

    /*
	public void loadBuffs() {
		Property prop;
		for (Map.Entry<String, Buff> buff : Buff.buffs.entrySet()) {
			Buff buffValue = buff.getValue();
			prop = config.get("Beacon Buffs", buffValue.getName(), true);
			if (prop.getBoolean(true)) {
				prop = config.get("Beacon Buffs", buffValue.getName() + " Required beacon level", buffValue.getMinBeaconLevel());
				buffValue.setMinBeaconLevel(prop.getInt());
				prop = config.get("Beacon Buffs", buffValue.getName() + " Corruption per buff level", (int) buffValue.getCorruption(buffValue.getMinBeaconLevel()));
				buffValue.setCorruption(prop.getInt());
				prop = config.get("Beacon Buffs", buffValue.getName() + " Max buff level", buffValue.getMaxBuffLevel());
				buffValue.setMaxBuffLevel(prop.getInt());
			}
			else {
				Buff.buffs.remove(buff.getKey());
			}
		}
		save();
	}
	*/

	private void save() {
		config.save();
	}
}
