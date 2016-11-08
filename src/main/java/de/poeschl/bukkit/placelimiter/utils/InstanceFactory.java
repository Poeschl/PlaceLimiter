package de.poeschl.bukkit.placelimiter.utils;

import de.poeschl.bukkit.placelimiter.PlaceLimiterPlugin;
import de.poeschl.bukkit.placelimiter.commands.ReloadConfigCommand;
import de.poeschl.bukkit.placelimiter.managers.SettingManager;

public class InstanceFactory {

    public ReloadConfigCommand createReloadConfigCommand(PlaceLimiterPlugin plugin, SettingManager settingsManager) {
        return new ReloadConfigCommand(plugin, settingsManager);
    }
}
