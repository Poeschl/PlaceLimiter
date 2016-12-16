package io.github.poeschl.bukkit.placelimiter.managers;

import io.github.poeschl.bukkit.placelimiter.models.Block;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.logging.Logger;

import static io.github.poeschl.bukkit.placelimiter.models.Block.DATA_ID_DELIMITER;


public class SettingManager {

    static final String NO_PERMISSION_MESSAGE_KEY = "noPermissionMessage";
    static final String LIMIT_PLACE_REACHED_MESSAGE_KEY = "limitPlaceReachedMessage";
    static final String NOT_PLACED_FROM_THIS_PLAYER_KEY = "notPlacedFromThisPlayer";
    static final String PLACE_RULES_KEY = "placeRules";

    Map<Block, Integer> cacheRuleList;
    List<Block> cacheLimitedBlocks;

    private FileConfiguration config;
    private Logger logger;

    public SettingManager(FileConfiguration config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void updateConfig(FileConfiguration config) {
        this.config = config;
    }

    public String getNoPermissionMessage() {
        return replaceColors(config.getString(NO_PERMISSION_MESSAGE_KEY));
    }

    public String getLimitPlaceReachedMessage() {
        return replaceColors(config.getString(LIMIT_PLACE_REACHED_MESSAGE_KEY));
    }

    public String getNotFromPlayerPlacedMessage() {
        return replaceColors(config.getString(NOT_PLACED_FROM_THIS_PLAYER_KEY));
    }

    public boolean isMaterialLimited(Block block) {
        if (cacheRuleList == null || cacheLimitedBlocks == null) {
            updateCache();
        }
        return cacheRuleList.containsKey(block);
    }

    public int getMaterialLimit(Block block) {
        if (cacheRuleList == null || cacheLimitedBlocks == null) {
            updateCache();
        }
        return cacheRuleList.get(block);
    }

    /**
     * Looks up the exact restricted block for the given block. This is nessesary because sometimes DIRT placement is limited but every DIRT block have an data id like DIRT:1, DIRT:2
     *
     * @param block The block to look up
     * @return the pure limited block
     */
    public Block getRestrictedBlockOf(Block block) {
        if (cacheRuleList == null || cacheLimitedBlocks == null) {
            updateCache();
        }
        return cacheLimitedBlocks.get(cacheLimitedBlocks.indexOf(block));
    }

    public void clearCache() {
        cacheRuleList = null;
        cacheLimitedBlocks = null;
        logger.fine("Cache cleared");
    }

    void updateCache() {
        cacheRuleList = new HashMap<>();
        cacheLimitedBlocks = new LinkedList<>();
        List<?> materialMap = config.getList(PLACE_RULES_KEY);

        logger.info("Detected Restrictions:");
        for (Iterator it = materialMap.iterator(); it.hasNext(); ) {
            HashMap<String, Integer> currentRule = (HashMap<String, Integer>) it.next();

            Block block;
            String key = currentRule.keySet().toArray(new String[0])[0];
            if (key.contains(String.valueOf(DATA_ID_DELIMITER))) {
                int data = Integer.valueOf(key.substring(key.indexOf(DATA_ID_DELIMITER) + 1, key.length()));
                String name = key.substring(0, key.indexOf(DATA_ID_DELIMITER));

                block = new Block(Material.getMaterial(name), (byte) data);
            } else {
                block = new Block(Material.getMaterial(key));
            }

            int limit = currentRule.get(key);

            cacheRuleList.put(block, limit);
            cacheLimitedBlocks.add(block);
            logger.info(block.toString() + " -> " + limit + " times");
        }
    }

    private String replaceColors(String string) {
        return string.replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
    }
}
