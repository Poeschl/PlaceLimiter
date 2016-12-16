package io.github.poeschl.bukkit.placelimiter;

import io.github.poeschl.bukkit.placelimiter.managers.PlacementManager;
import io.github.poeschl.bukkit.placelimiter.managers.SettingManager;
import io.github.poeschl.bukkit.placelimiter.utils.InstanceFactory;
import org.bukkit.Server;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PlaceLimiterPlugin extends JavaPlugin {

    protected SettingManager settingManager;
    protected PlacementManager placementManager;

    private Logger logger;
    private String pluginName;

    @Override
    public void onEnable() {
        super.onEnable();
        PluginDescriptionFile pdfFile = getInfo();

        if (getConfig().getKeys(false).size() == 0) {
            getConfig();
            saveDefaultConfig();
        }

        InstanceFactory instanceFactory = getInstanceFactory();
        logger = instanceFactory.getLogger(this);
        settingManager = instanceFactory.createSettingsManager(getConfig(), logger);
        placementManager = instanceFactory.createPlacementManager(logger, getDataFolder(), instanceFactory.createPlacementList());
        placementManager.load();

        getCommand("plreload").setExecutor(instanceFactory.createReloadConfigCommand(this, settingManager));
        getBukkitServer().getPluginManager().registerEvents(instanceFactory.createBlockPlacingListener(logger, settingManager, placementManager), this);
        pluginName = pdfFile.getName() + " version " + pdfFile.getVersion();
        logger.info(pluginName + " is enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        placementManager.save();
        PluginDescriptionFile pdfFile = this.getDescription();
        logger.info(pluginName + " is disabled!");
    }

    public void reload() {
        reloadConfig();
        settingManager.updateConfig(getConfig());
        settingManager.clearCache();
        placementManager.load();
    }

    protected InstanceFactory getInstanceFactory() {
        return new InstanceFactory();
    }

    protected PluginDescriptionFile getInfo() {
        return getDescription();
    }

    protected Server getBukkitServer() {
        return getServer();
    }
}
