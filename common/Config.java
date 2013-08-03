package playerbeacons.common;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import java.io.File;

public class Config {

	public int playerBeaconBlockID;
	public int defiledSoulConductorBlockID;
	public int defiledSoulPylonBlockID;

	public int beheaderItemID;
	public int speedCrystalItemID;
	public int digCrystalItemID;
	public int jumpCrystalItemID;
	public int resCrystalItemID;

	public int decapitationEnchantmentID;

	public boolean swiftnessBuffEnabled;
	public boolean miningBuffEnabled;
	public boolean jumpBuffEnabled;
	public boolean nightVisionBuffEnabled;

	public boolean enableEasterEgg;

	private Configuration config;


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

		prop = config.get(Configuration.CATEGORY_GENERAL, "Decapitation Enchantment ID", 200);
		decapitationEnchantmentID = prop.getInt();

		prop = config.get(Configuration.CATEGORY_GENERAL, "Enable Easter Egg", false);
		prop.comment = "WARNING: This could destory parts of your world unintentionally";
		enableEasterEgg = prop.getBoolean(false);

		//Buffs
		prop = config.get("Beacon Buffs", "Swiftness", true);
		swiftnessBuffEnabled = prop.getBoolean(true);
		prop = config.get("Beacon Buffs", "Mining Speed", true);
		miningBuffEnabled = prop.getBoolean(true);
		prop = config.get("Beacon Buffs", "Jump", true);
		jumpBuffEnabled = prop.getBoolean(true);
		prop = config.get("Beacon Buffs", "Night Vision", true);
		nightVisionBuffEnabled = prop.getBoolean(true);

	}

	private void save() {
		config.save();
	}
}
