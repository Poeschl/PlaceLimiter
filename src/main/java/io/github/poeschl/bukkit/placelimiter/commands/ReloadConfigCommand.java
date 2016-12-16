package io.github.poeschl.bukkit.placelimiter.commands;

import io.github.poeschl.bukkit.placelimiter.PlaceLimiterPlugin;
import io.github.poeschl.bukkit.placelimiter.managers.PermissionManager;
import io.github.poeschl.bukkit.placelimiter.managers.SettingManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {

    private PlaceLimiterPlugin plugin;
    private SettingManager settingManager;

    public ReloadConfigCommand(PlaceLimiterPlugin plugin, SettingManager settingManager) {
        this.plugin = plugin;
        this.settingManager = settingManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender.hasPermission(PermissionManager.PERMISSION_KEY_RELOAD)) {
            plugin.reload();
            commandSender.sendMessage("Configuration reloaded");
        } else {
            commandSender.sendMessage(settingManager.getNoPermissionMessage());
        }
        return true;
    }
}
