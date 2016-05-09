package de.poeschl.bukkit.placelimiter.commands;

import de.poeschl.bukkit.placelimiter.PlaceLimiterPlugin;
import de.poeschl.bukkit.placelimiter.managers.PermissionManager;
import de.poeschl.bukkit.placelimiter.managers.SettingManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
