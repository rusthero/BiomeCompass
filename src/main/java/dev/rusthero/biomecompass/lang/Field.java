package dev.rusthero.biomecompass.lang;

/**
 * Represents fields in language/translation files. Used for easier access to fields from Language class.
 * <p>
 * Field names from translation files are easily transferable to this Enum. An example looks like:
 * lang.locale_code -> LANG_LOCALE_CODE
 *
 * @see Language#getString(Field)
 */
public enum Field {
    LANG_NAME,
    LANG_NAME_TRANSLITERATION,
    LANG_LOCALE_CODE,

    LOG_BIOME_COMPASS_ENABLED,
    LOG_BIOME_COMPASS_DISABLED,
    LOG_VAULT_MISSING,
    LOG_VERSION_TRACKER_OUTDATED,
    LOG_VERSION_TRACKER_ERROR,
    LOG_PLAYER_LOCATE,

    ITEM_BIOME_COMPASS_DISPLAY_NAME,
    ITEM_BIOME_COMPASS_LOCATING,
    ITEM_BIOME_COMPASS_NOT_FOUND,
    ITEM_BIOME_COMPASS_NOT_HOLDING,
    ITEM_BIOME_COMPASS_LOCATED,
    ITEM_BIOME_COMPASS_LOCATED_DISTANCE,
    ITEM_BIOME_COMPASS_ON_COOLDOWN,
    ITEM_BIOME_COMPASS_NO_EXPERIENCE,
    ITEM_BIOME_COMPASS_NO_FUNDS,
    ITEM_BIOME_COMPASS_COST,
    ITEM_BIOME_COMPASS_COST_XP,
    ITEM_BIOME_COMPASS_UNKNOWN_BIOME,

    GUI_BIOMES_MENU_TITLE_OVERWORLD,
    GUI_BIOMES_MENU_TITLE_NETHER,
    GUI_BIOMES_MENU_TITLE_THE_END,
    GUI_BIOMES_MENU_TITLE_SELECT,
    GUI_BIOMES_MENU_DISALLOWED,

    BIOME_OCEAN_NAME,
    BIOME_PLAINS_NAME,
    BIOME_DESERT_NAME,
    BIOME_WINDSWEPT_HILLS_NAME,
    BIOME_FOREST_NAME,
    BIOME_TAIGA_NAME,
    BIOME_SWAMP_NAME,
    BIOME_MANGROVE_SWAMP_NAME,
    BIOME_RIVER_NAME,
    BIOME_FROZEN_OCEAN_NAME,
    BIOME_FROZEN_RIVER_NAME,
    BIOME_SNOWY_PLAINS_NAME,
    BIOME_MUSHROOM_FIELDS_NAME,
    BIOME_BEACH_NAME,
    BIOME_JUNGLE_NAME,
    BIOME_SPARSE_JUNGLE_NAME,
    BIOME_DEEP_OCEAN_NAME,
    BIOME_STONY_SHORE_NAME,
    BIOME_SNOWY_BEACH_NAME,
    BIOME_BIRCH_FOREST_NAME,
    BIOME_DARK_FOREST_NAME,
    BIOME_SNOWY_TAIGA_NAME,
    BIOME_OLD_GROWTH_PINE_TAIGA_NAME,
    BIOME_WINDSWEPT_FOREST_NAME,
    BIOME_SAVANNA_NAME,
    BIOME_SAVANNA_PLATEAU_NAME,
    BIOME_BADLANDS_NAME,
    BIOME_WOODED_BADLANDS_NAME,
    BIOME_WARM_OCEAN_NAME,
    BIOME_LUKEWARM_OCEAN_NAME,
    BIOME_COLD_OCEAN_NAME,
    BIOME_DEEP_LUKEWARM_OCEAN_NAME,
    BIOME_DEEP_COLD_OCEAN_NAME,
    BIOME_DEEP_FROZEN_OCEAN_NAME,
    BIOME_SUNFLOWER_PLAINS_NAME,
    BIOME_WINDSWEPT_GRAVELLY_HILLS_NAME,
    BIOME_FLOWER_FOREST_NAME,
    BIOME_ICE_SPIKES_NAME,
    BIOME_OLD_GROWTH_BIRCH_FOREST_NAME,
    BIOME_OLD_GROWTH_SPRUCE_TAIGA_NAME,
    BIOME_WINDSWEPT_SAVANNA_NAME,
    BIOME_ERODED_BADLANDS_NAME,
    BIOME_BAMBOO_JUNGLE_NAME,
    BIOME_MEADOW_NAME,
    BIOME_GROVE_NAME,
    BIOME_SNOWY_SLOPES_NAME,
    BIOME_FROZEN_PEAKS_NAME,
    BIOME_JAGGED_PEAKS_NAME,
    BIOME_STONY_PEAKS_NAME,
    BIOME_DRIPSTONE_CAVES_NAME,
    BIOME_LUSH_CAVES_NAME,
    BIOME_DEEP_DARK_NAME,
    BIOME_NETHER_WASTES_NAME,
    BIOME_SOUL_SAND_VALLEY_NAME,
    BIOME_CRIMSON_FOREST_NAME,
    BIOME_WARPED_FOREST_NAME,
    BIOME_BASALT_DELTAS_NAME,
    BIOME_THE_END_NAME,
    BIOME_SMALL_END_ISLANDS_NAME,
    BIOME_END_MIDLANDS_NAME,
    BIOME_END_HIGHLANDS_NAME,
    BIOME_END_BARRENS_NAME,
    BIOME_THE_VOID_NAME;
}