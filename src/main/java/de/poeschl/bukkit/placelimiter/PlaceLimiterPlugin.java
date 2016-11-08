package de.poeschl.bukkit.placelimiter;

import de.poeschl.bukkit.placelimiter.managers.PlacementManager;
import de.poeschl.bukkit.placelimiter.managers.SettingManager;
import de.poeschl.bukkit.placelimiter.utils.InstanceFactory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceLimiterPlugin extends JavaPlugin {

    private SettingManager settingManager;
    private PlacementManager placementManager;

    @Override
    public void onEnable() {
        super.onEnable();
        PluginDescriptionFile pdfFile = this.getDescription();
        InstanceFactory instanceFactory = new InstanceFactory();

        if (getConfig().getKeys(false).size() == 0) {
            getConfig();
            saveDefaultConfig();
        }
        settingManager = new SettingManager(getConfig(), getLogger());
        placementManager = instanceFactory.createPlacementManager(getLogger(), getDataFolder(), instanceFactory.createPlacementList());
        placementManager.load();

        getCommand("plreload").setExecutor(instanceFactory.createReloadConfigCommand(this, settingManager));
        getServer().getPluginManager().registerEvents(instanceFactory.createBlockPlacingListener(getLogger(), settingManager, placementManager), this);

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
