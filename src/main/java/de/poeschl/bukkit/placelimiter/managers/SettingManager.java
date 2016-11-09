package de.poeschl.bukkit.placelimiter.managers;

import de.poeschl.bukkit.placelimiter.models.Block;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class SettingManager {

    static final String NO_PERMISSION_MESSAGE_KEY = "noPermissionMessage";
    static final String LIMIT_PLACE_REACHED_MESSAGE_KEY = "limitPlaceReachedMessage";
    static final String NOT_PLACED_FROM_THIS_PLAYER_KEY = "notPlacedFromThisPlayer";
    static final String PLACE_RULES_KEY = "placeRules";

    private FileConfiguration config;
    private Logger logger;
    private Map<Block, Integer> cacheRuleList;

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
        if (cacheRuleList == null) {
            updateCache();
        }
        return cacheRuleList.containsKey(block);
    }

    public int getMaterialLimit(Block block) {
        if (cacheRuleList == null) {
            updateCache();
        }
        return cacheRuleList.get(block);
    }

    public void clearCache() {
        cacheRuleList = null;
        logger.fine("Cache cleared");
    }

    void updateCache() {
        cacheRuleList = new HashMap<>();
        List<?> materialMap = config.getList(PLACE_RULES_KEY);

        logger.info("Detected Restrictions:");
        for (Iterator it = materialMap.iterator(); it.hasNext(); ) {
            HashMap<String, Integer> currentRule = (HashMap<String, Integer>) it.next();

            Block block;
            String key = currentRule.keySet().toArray(new String[0])[0];
            if (key.contains(":")) {
                int data = Integer.valueOf(key.substring(key.indexOf(":") + 1, key.length()));
                String name = key.substring(0, key.indexOf(":"));

                block = new Block(Material.getMaterial(name), (byte) data);
            } else {
                block = new Block(Material.getMaterial(key));
            }

            int limit = currentRule.get(key);

            cacheRuleList.put(block, limit);
            logger.info(block.toString() + " -> " + limit + " times");
        }
    }

    private String replaceColors(String string) {
        return string.replaceAll("(?i)&([a-k0-9])", "\u00A7$1");
    }
}
