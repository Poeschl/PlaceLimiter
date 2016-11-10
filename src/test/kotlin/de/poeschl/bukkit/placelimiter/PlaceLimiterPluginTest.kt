package de.poeschl.bukkit.placelimiter

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import de.poeschl.bukkit.placelimiter.commands.ReloadConfigCommand
import de.poeschl.bukkit.placelimiter.listeners.BlockPlacingListener
import de.poeschl.bukkit.placelimiter.managers.PlacementManager
import de.poeschl.bukkit.placelimiter.managers.SettingManager
import de.poeschl.bukkit.placelimiter.models.PlacementList
import de.poeschl.bukkit.placelimiter.utils.InstanceFactory
import org.bukkit.Server
import org.bukkit.command.PluginCommand
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.junit.Ignore
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`
import java.util.logging.Logger

class PlaceLimiterPluginTest {

    @Test
    fun testOnEnable() {
        //WHEN
        val mockedInstanceFactory: InstanceFactory = mock()
        val pluginToTest: PlaceLimiterPlugin = mock()
        val mockedConfig: FileConfiguration = mock()
        val mockedSettings: SettingManager = mock()
        val mockedLogger: Logger = mock()
        val mockedServer: Server = mock()
        val mockedReloadCommand: ReloadConfigCommand = mock()
        val mockedPlacementManager: PlacementManager = mock()
        val mockedPlacingListener: BlockPlacingListener = mock()
        val mockedPlacementList: PlacementList = mock()
        val mockedPluginManager: PluginManager = mock()
        val mockedPluginCommand: PluginCommand = mock()
        val dummyKeys = hashSetOf("dummy")

        `when`(mockedInstanceFactory.createSettingsManager(any(FileConfiguration::class.java), eq(mockedLogger))).thenReturn(mockedSettings)
        `when`(mockedInstanceFactory.createReloadConfigCommand(eq(pluginToTest), eq(mockedSettings))).thenReturn(mockedReloadCommand)
        `when`(mockedInstanceFactory.createBlockPlacingListener(eq(mockedLogger), eq(mockedSettings), eq(mockedPlacementManager))).thenReturn(mockedPlacingListener)
        `when`(mockedInstanceFactory.getLogger(any(JavaPlugin::class.java))).thenReturn(mockedLogger)
        `when`(mockedInstanceFactory.createPlacementManager(eq(mockedLogger), any(), eq(mockedPlacementList))).thenReturn(mockedPlacementManager)
        `when`(mockedInstanceFactory.createPlacementList()).thenReturn(mockedPlacementList)

        `when`(mockedServer.pluginManager).thenReturn(mockedPluginManager)
        `when`(mockedConfig.getKeys(anyBoolean())).thenReturn(dummyKeys)
        `when`(pluginToTest.config).thenReturn(mockedConfig)
        `when`(pluginToTest.instanceFactory).thenReturn(mockedInstanceFactory)
        `when`(pluginToTest.info).thenReturn(PluginDescriptionFile("", "", ""))
        `when`(pluginToTest.bukkitServer).thenReturn(mockedServer)
        `when`(pluginToTest.getCommand(anyString())).thenReturn(mockedPluginCommand)

        `when`(pluginToTest.onEnable()).thenCallRealMethod()

        //THEN
        pluginToTest.onEnable()

        //VERIFY
        verify(pluginToTest, never()).saveDefaultConfig()
        verify(mockedPlacementManager).load()
        verify(mockedPluginManager).registerEvents(mockedPlacingListener, pluginToTest)
        verify(mockedPluginCommand).executor = mockedReloadCommand
    }

    @Test
    fun testOnEnableFirstTime() {
        //WHEN
        val mockedInstanceFactory: InstanceFactory = mock()
        val pluginToTest: PlaceLimiterPlugin = mock()
        val mockedConfig: FileConfiguration = mock()
        val mockedSettings: SettingManager = mock()
        val mockedLogger: Logger = mock()
        val mockedServer: Server = mock()
        val mockedReloadCommand: ReloadConfigCommand = mock()
        val mockedPlacementManager: PlacementManager = mock()
        val mockedPlacingListener: BlockPlacingListener = mock()
        val mockedPlacementList: PlacementList = mock()
        val mockedPluginManager: PluginManager = mock()
        val mockedPluginCommand: PluginCommand = mock()

        `when`(mockedInstanceFactory.createSettingsManager(any(FileConfiguration::class.java), eq(mockedLogger))).thenReturn(mockedSettings)
        `when`(mockedInstanceFactory.createReloadConfigCommand(eq(pluginToTest), eq(mockedSettings))).thenReturn(mockedReloadCommand)
        `when`(mockedInstanceFactory.createBlockPlacingListener(eq(mockedLogger), eq(mockedSettings), eq(mockedPlacementManager))).thenReturn(mockedPlacingListener)
        `when`(mockedInstanceFactory.getLogger(any(JavaPlugin::class.java))).thenReturn(mockedLogger)
        `when`(mockedInstanceFactory.createPlacementManager(eq(mockedLogger), any(), eq(mockedPlacementList))).thenReturn(mockedPlacementManager)
        `when`(mockedInstanceFactory.createPlacementList()).thenReturn(mockedPlacementList)

        `when`(mockedServer.pluginManager).thenReturn(mockedPluginManager)
        `when`(mockedConfig.getKeys(anyBoolean())).thenReturn(emptySet())
        `when`(pluginToTest.config).thenReturn(mockedConfig)
        `when`(pluginToTest.instanceFactory).thenReturn(mockedInstanceFactory)
        `when`(pluginToTest.info).thenReturn(PluginDescriptionFile("", "", ""))
        `when`(pluginToTest.bukkitServer).thenReturn(mockedServer)
        `when`(pluginToTest.getCommand(anyString())).thenReturn(mockedPluginCommand)

        `when`(pluginToTest.onEnable()).thenCallRealMethod()

        //THEN
        pluginToTest.onEnable()


        //VERIFY
        verify(pluginToTest).saveDefaultConfig()
        verify(mockedPlacementManager).load()
        verify(mockedPluginManager).registerEvents(mockedPlacingListener, pluginToTest)
        verify(mockedPluginCommand).executor = mockedReloadCommand
    }

    //TODO: Make this test work. Strange NPE appears
    @Ignore
    @Test
    fun testOnDisable() {
        //WHEN
        val pluginToTest: PlaceLimiterPlugin = mock()
        val mockedPlacementManager: PlacementManager = mock()
        val mockedSettings: SettingManager = mock()

        pluginToTest.placementManager = mockedPlacementManager
        pluginToTest.settingManager = mockedSettings

        `when`(pluginToTest.onDisable()).thenCallRealMethod()

        //THEN
        pluginToTest.onDisable()

        //VERIFY
        verify(mockedPlacementManager).save()
    }

    @Test
    fun testReload() {
        //WHEN
        val pluginToTest: PlaceLimiterPlugin = mock()
        val mockedPlacementManager: PlacementManager = mock()
        val mockedSettings: SettingManager = mock()
        val mockedConfig: FileConfiguration = mock()

        pluginToTest.placementManager = mockedPlacementManager
        pluginToTest.settingManager = mockedSettings
        `when`(pluginToTest.config).thenReturn(mockedConfig)

        `when`(pluginToTest.reload()).thenCallRealMethod()

        //THEN
        pluginToTest.reload()

        //VERIFY
        verify(pluginToTest).reloadConfig()
        verify(mockedSettings).updateConfig(any(FileConfiguration::class.java))
        verify(mockedSettings).clearCache()
        verify(mockedPlacementManager).load()
    }
}
