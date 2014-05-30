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
    public boolean enableHideParticleEffects;
	public boolean enableEasterEgg;
	public boolean enableZombieHead;

	private final Configuration config;


	public Config(File file) {
		config = new Configuration(file);
		config.load();
		this.loadGeneral();
		this.save();
	}

	private void loadGeneral() {
		Property prop;

		prop = config.get(Configuration.CATEGORY_GENERAL, "Decapitation Enchantment ID", 200);
		decapitationEnchantmentID = prop.getInt();
        prop = config.get(Configuration.CATEGORY_GENERAL, "Corruption Potion ID", 30);
        corruptionPotionID = prop.getInt();
		prop = config.get(Configuration.CATEGORY_GENERAL, "Allow Zombies to spawn with player heads", true);
		prop.comment = "WARNING: Mobs wearing heads do not despawn and may build up over time.";
		enableZombieHead = prop.getBoolean(true);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Enable Easter Egg", false);
		prop.comment = "WARNING: This could destroy parts of your world unintentionally";
		enableEasterEgg = prop.getBoolean(false);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Time between special zombie spawns", 54000); //TODO Remove
		prop.comment = "Time between chance to spawn a zombie with a player head. Default: 54000 seconds";
		spawnCooldownDuration = prop.getInt();
		prop = config.get(Configuration.CATEGORY_GENERAL, "Disable Corruption", false);
		prop.comment = "Whether to do corruption calculations or not";
		disableCorruption = prop.getBoolean(false);
		prop = config.get(Configuration.CATEGORY_GENERAL, "Enable Thaumcraft Intergration", true);
		prop.comment = "If this is enabled and Thaumcraft is detected, the default recipes will be replaced with TC research and recipes";
		enableThaumcraft = prop.getBoolean(true);
        prop = config.get(Configuration.CATEGORY_GENERAL, "Hide particle effects", true);
        prop.comment = "Hides all particle effects on the player";
        enableHideParticleEffects = prop.getBoolean(true);
	}

	private void save() {
		config.save();
	}
}
