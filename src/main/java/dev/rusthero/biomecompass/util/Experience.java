package dev.rusthero.biomecompass.util;

import org.bukkit.entity.Player;

/**
 * A utility for managing player experience.
 *
 * @author Jikoo
 * @see <a href=https://gist.github.com/Jikoo/30ec040443a4701b8980>Jikoo/Experience.java</a>
 */
public final class Experience {

    /**
     * Calculate a player's total experience based on level and progress to next.
     *
     * @param player Player whose experience is going to be got.
     * @return The amount of experience the Player has.
     * @see <a href=http://minecraft.gamepedia.com/Experience#Leveling_up>Experience#Leveling_up</a>
     */
    public static int getExp(Player player) {
        return getExpFromLevel(player.getLevel()) + Math.round(getExpToNext(player.getLevel()) * player.getExp());
    }

    /**
     * Calculate total experience based on level.
     *
     * @param level Level that is going to be converted to total experience.
     * @return Total experience calculated.
     * @see <a href=http://minecraft.gamepedia.com/Experience#Leveling_up>Experience#Leveling_up</a>
     */
    public static int getExpFromLevel(int level) {
        if (level > 30) return (int) (4.5 * level * level - 162.5 * level + 2220);
        if (level > 15) return (int) (2.5 * level * level - 40.5 * level + 360);

        return level * level + 6 * level;
    }

    /**
     * Calculate level (including progress to next level) based on total experience.
     *
     * @param exp Total experience.
     * @return Level calculated.
     */
    public static double getLevelFromExp(long exp) {
        int level = getIntLevelFromExp(exp);

        // Get remaining exp progressing towards next level. Cast to float for next bit of math.
        float remainder = exp - (float) getExpFromLevel(level);

        // Get level progress with float precision.
        float progress = remainder / getExpToNext(level);

        // Slap both numbers together and call it a day. While it shouldn't be possible for progress
        // to be an invalid value (value < 0 || 1 <= value)
        return ((double) level) + progress;
    }

    /**
     * Calculate level based on total experience.
     *
     * @param exp Total experience.
     * @return Level calculated.
     */
    public static int getIntLevelFromExp(long exp) {
        if (exp > 1395) return (int) ((Math.sqrt(72 * exp - 54215D) + 325) / 18);

        if (exp > 315) return (int) (Math.sqrt(40 * exp - 7839D) / 10 + 8.1);

        if (exp > 0) return (int) (Math.sqrt(exp + 9D) - 3);

        return 0;
    }

    /**
     * Get the total amount of experience required to progress to the next level.
     *
     * @param level Current level.
     * @see <a href=http://minecraft.gamepedia.com/Experience#Leveling_up>Experience#Leveling_up</a>
     */
    private static int getExpToNext(int level) {
        // Simplified formula. Internal: 112 + (level - 30) * 9
        if (level >= 30) return level * 9 - 158;

        // Simplified formula. Internal: 37 + (level - 15) * 5
        if (level >= 15) return level * 5 - 38;

        // Internal: 7 + level * 2
        return level * 2 + 7;
    }

    /**
     * Gives experience to a player.
     *
     * <p>This method is preferred over {@link Player#giveExp(int)}.
     * <br>In older versions the method does not take differences in exp per level into account.
     * This leads to over levelling when granting players large amounts of experience.
     * <br>In modern versions, while differing amounts of experience per level are accounted for, the
     * approach used is loop-heavy and requires an excessive number of calculations, which makes it
     * quite slow.
     *
     * @param player Player to be affected.
     * @param exp    The amount of experience to add or remove.
     */
    public static void giveExp(Player player, int exp) {
        exp += getExp(player);

        if (exp < 0) exp = 0;

        double levelAndExp = getLevelFromExp(exp);
        int level = (int) levelAndExp;
        player.setLevel(level);
        player.setExp((float) (levelAndExp - level));
    }

    private Experience() {
    }
}
