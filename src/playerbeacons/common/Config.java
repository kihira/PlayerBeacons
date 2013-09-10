package playerbeacons.common;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import playerbeacons.buff.Buff;

import java.io.File;
import java.util.HashMap;

public class Config {

	public int playerBeaconBlockID;
	public int defiledSoulConductorBlockID;
	public int defiledSoulPylonBlockID;

	public int beheaderItemID;
	public int crystalItemID;
	public int speedCrystalItemID;
	public int digCrystalItemID;
	public int jumpCrystalItemID;
	public int resCrystalItemID;

	public int decapitationEnchantmentID;
	public int spawnCooldownDuration;

	public boolean enableEasterEgg;
	public boolean enableZombieHead;

	private Configuration config;
	public boolean disableCorruption;


	public Config(File file) {
		config = new Configuration(file);
		config.load();
		loadGeneral();
		save();
	}

	private void loadGeneral() {

		Property prop;
		prop = config.get(Configuration.CATEGORY_BLOCK, "Player Beacon", 3500);
		playerBeaconBlockID = prop.getInt();

		prop = config.get(Configuration.CATEGORY_BLOCK, "Defiled Soul Conductor", 3501);
		defiledSoulConductorBlockID = prop.getInt();

		prop = config.get(Configuration.CATEGORY_BLOCK, "Defiled Soul Pylon", 3502);
		defiledSoulPylonBlockID = prop.getInt();

		prop = config.get(Configuration.CATEGORY_ITEM, "Beheader", 20000);
		beheaderItemID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_ITEM, "Speed Crystal", 20001);
		speedCrystalItemID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_ITEM, "Dig Crystal", 20002);
		digCrystalItemID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_ITEM, "Jump Crystal", 20003);
		jumpCrystalItemID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_ITEM, "Resistance Crystal", 20004);
		resCrystalItemID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_ITEM, "Depleted Crystal", 20005);
		crystalItemID = prop.getInt();

		prop = config.get(Configuration.CATEGORY_GENERAL, "Decapitation Enchantment ID", 200);
		decapitationEnchantmentID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_GENERAL, "Allow Zombies to spawn with player heads", true);
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

	}

	public void loadBuffs() {
		Property prop;
		for (Buff buff:Buff.buffs) {
			prop = config.get("Beacon Buffs", buff.getName(), true);
			if (prop.getBoolean(true)) {
				prop = config.get("Beacon Buffs", buff.getName() + " Required beacon level", buff.getMinBeaconLevel());
				buff.setMinBeaconLevel(prop.getInt());
				prop = config.get("Beacon Buffs", buff.getName() + " Corruption per buff level", (int) buff.getCorruption(buff.getMinBeaconLevel()));
				buff.setCorruption(prop.getInt());
				prop = config.get("Beacon Buffs", buff.getName() + " Max buff level", buff.getMaxBuffLevel());
				buff.setMaxBuffLevel(prop.getInt());
			}
			else {
				Buff.buffs.remove(buff);
			}
		}
		config.save();
	}

	private void save() {
		config.save();
	}
}
