package playerbeacons.common;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

import java.io.File;

public class Config {

	public int playerBeaconBlockID;
	public int playerBeaconBaseBlockID;
	public int conductorBlockID;

	public int beheaderItemID;

	public int decapitationEnchantmentID;

	public boolean swiftnessBuffEnabled;
	public boolean miningBuffEnabled;
	public boolean jumpBuffEnabled;
	public boolean nightVisionBuffEnabled;


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

		//TODO Think of good name for this block
		prop = config.get(Configuration.CATEGORY_BLOCK, "Player Beacon Base", 3501);
		playerBeaconBaseBlockID = prop.getInt();

		//TODO Think of good name for this block
		prop = config.get(Configuration.CATEGORY_BLOCK, "Conductor", 3502);
		conductorBlockID = prop.getInt();

		prop = config.get(Configuration.CATEGORY_ITEM, "Beheader", 35000);
		beheaderItemID = prop.getInt();

		//TODO Check if ID is valid
		prop = config.get(Configuration.CATEGORY_GENERAL, "Decapitation Enchantment ID", 200);
		decapitationEnchantmentID = prop.getInt();

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
