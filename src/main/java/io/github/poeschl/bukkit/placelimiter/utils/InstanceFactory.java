package io.github.poeschl.bukkit.placelimiter.utils;

import io.github.poeschl.bukkit.placelimiter.PlaceLimiterPlugin;
import io.github.poeschl.bukkit.placelimiter.commands.ReloadConfigCommand;
import io.github.poeschl.bukkit.placelimiter.listeners.BlockPlacingListener;
import io.github.poeschl.bukkit.placelimiter.managers.PlacementManager;
import io.github.poeschl.bukkit.placelimiter.managers.SettingManager;
import io.github.poeschl.bukkit.placelimiter.models.PlacementList;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public class InstanceFactory {

    public Logger getLogger(JavaPlugin plugin) {
        return plugin.getLogger();
    }

    public ReloadConfigCommand createReloadConfigCommand(PlaceLimiterPlugin plugin, SettingManager settingsManager) {
        return new ReloadConfigCommand(plugin, settingsManager);
    }

    public BlockPlacingListener createBlockPlacingListener(Logger logger, SettingManager settingsManager, PlacementManager placementManager) {
        return new BlockPlacingListener(logger, settingsManager, placementManager);
    }

    public PlacementManager createPlacementManager(Logger logger, File dataFolder, PlacementList placementList) {
        return new PlacementManager(logger, dataFolder, placementList);
    }

    public PlacementList createPlacementList() {
        return new PlacementList();
    }

    public SettingManager createSettingsManager(FileConfiguration config, Logger logger) {
        return new SettingManager(config, logger);
    }
}
