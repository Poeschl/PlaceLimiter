package de.poeschl.bukkit.placelimiter;

import de.poeschl.bukkit.placelimiter.commands.ReloadConfigCommand;
import de.poeschl.bukkit.placelimiter.listeners.BlockPlacingListener;
import de.poeschl.bukkit.placelimiter.managers.PlacementManager;
import de.poeschl.bukkit.placelimiter.managers.SettingManager;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceLimiterPlugin extends JavaPlugin {

    private SettingManager settingManager;
    private PlacementManager placementManager;

    @Override
    public void onEnable() {
        super.onEnable();
        PluginDescriptionFile pdfFile = this.getDescription();

        if (getConfig().getKeys(false).size() == 0) {
            getConfig();
            saveDefaultConfig();
        }
        settingManager = new SettingManager(getConfig(), getLogger());
        placementManager = new PlacementManager(this);
        placementManager.load();

        getCommand("plreload").setExecutor(new ReloadConfigCommand(this, settingManager));
        getServer().getPluginManager().registerEvents(new BlockPlacingListener(this, settingManager, placementManager), this);

        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        PluginDescriptionFile pdfFile = this.getDescription();
        placementManager.save();
        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled!");
    }

    public void reload() {
        reloadConfig();
        settingManager.updateConfig(getConfig());
        settingManager.clearCache();
        placementManager.load();
    }
}
