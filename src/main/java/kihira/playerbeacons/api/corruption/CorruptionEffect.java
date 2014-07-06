package kihira.playerbeacons.api.corruption;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The base class for all corruption effects. These effects are applied to the player usually when the corruption in a
 * beacon reaches a certain level however the effect itself can decide on the conditions. Methods are called when the
 * corruption effect starts, whilst it's active and when it finishes along with the player and the beacon itself
 */
public abstract class CorruptionEffect {

    public static final List<CorruptionEffect> corruptionEffects = new ArrayList<CorruptionEffect>();
    /**
     * This value is not a limit but more a guide. When corruption reaches or goes above this level, you should generally
     * be applying the worst effects you can
     */
    public static final float CORRUPTION_MAX = 100000;

    protected final String name;
    protected final float corruptionUnlock;

    public CorruptionEffect(String name, float corruptionUnlock) {
        if (!corruptionEffects.contains(this)) {
            this.name = name;
            this.corruptionUnlock = corruptionUnlock;
            corruptionEffects.add(this);
        }
        else throw new IllegalArgumentException(String.format("%s has already been registered as a corruption!", name));
    }

    /**
     * This is called when the corruption effect is initially applied to the player.
     * @param player The player
     */
    public abstract void init(EntityPlayer player, float corruption);

    /**
     * This is called every tick the corruption effect is active, that the player is in the same dimension as the beacon
     * and that the beacon is not disabled
     * @param player The player
     */
    public abstract void onUpdate(EntityPlayer player, float corruption);

    /**
     * This is called when the corruption effect is removed from the player (such as when corruption decreases below a
     * certain amount)
     * @param player The player
     */
    public abstract void finish(EntityPlayer player, float corruption);

    /**
     * This is called to check if the player has met the required conditions for the corruption effect to activate. This
     * is only called whilst the player does not have the corruption effect active
     * @param player The player
     * @param world The world of the beacon
     * @return Whether the effect should start to be applied
     */
    public boolean shouldActivate(EntityPlayer player, World world, float corruption) {
        return corruption >= this.corruptionUnlock;
    }

    /**
     * Whilst this effect is active, this is called to check if it should keep running.
     * @param player The player
     * @param world The world of the beacon
     * @return Whether the effect should continue to be applied
     */
    public boolean shouldContinue(EntityPlayer player, World world, float corruption) {
        return corruption >= this.corruptionUnlock;
    }

    public String getUnlocalisedName() {
        return "corruption." + this.name + ".name";
    }
}
