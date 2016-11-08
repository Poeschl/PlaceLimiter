package de.poeschl.bukkit.placelimiter.utils;

import de.poeschl.bukkit.placelimiter.PlaceLimiterPlugin;
import de.poeschl.bukkit.placelimiter.commands.ReloadConfigCommand;
import de.poeschl.bukkit.placelimiter.listeners.BlockPlacingListener;
import de.poeschl.bukkit.placelimiter.managers.PlacementManager;
import de.poeschl.bukkit.placelimiter.managers.SettingManager;

import java.util.logging.Logger;

public class InstanceFactory {

    public ReloadConfigCommand createReloadConfigCommand(PlaceLimiterPlugin plugin, SettingManager settingsManager) {
        return new ReloadConfigCommand(plugin, settingsManager);
    }

    public BlockPlacingListener createBlockPlacingListener(Logger logger, SettingManager settingsManager, PlacementManager placementManager) {
        return new BlockPlacingListener(logger, settingsManager, placementManager);
    }
}
