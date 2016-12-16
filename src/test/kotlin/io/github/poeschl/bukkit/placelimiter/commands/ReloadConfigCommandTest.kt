package io.github.poeschl.bukkit.placelimiter.commands

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import io.github.poeschl.bukkit.placelimiter.PlaceLimiterPlugin
import io.github.poeschl.bukkit.placelimiter.managers.PermissionManager
import io.github.poeschl.bukkit.placelimiter.managers.SettingManager
import io.github.poeschl.bukkit.placelimiter.utils.InstanceFactory
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.junit.Test
import org.mockito.Mockito.`when`

/**
 * Created by Markus on 08.11.2016.
 */

class ReloadConfigCommandTest {

    private val noPermissionText = "No Permission"

    @Test
    fun testReloadCommand() {
        val mockPlugin: PlaceLimiterPlugin = mock()
        val mockSettings: SettingManager = mock()
        `when`(mockSettings.noPermissionMessage).thenReturn(noPermissionText)
        val commandHandler: ReloadConfigCommand = InstanceFactory().createReloadConfigCommand(mockPlugin, mockSettings)
        val mockCommand: Command = mock()
        val mockCommandSender: CommandSender = mock()
        `when`(mockCommandSender.hasPermission(PermissionManager.PERMISSION_KEY_RELOAD)).thenReturn(true)

        commandHandler.onCommand(mockCommandSender, mockCommand, "", emptyArray())

        verify(mockPlugin, times(1)).reload()
        verify(mockCommandSender).sendMessage("Configuration reloaded")
        verify(mockCommandSender, never()).sendMessage(noPermissionText)
    }

    @Test
    fun testReloadCommandWithNoPermission() {
        val mockPlugin: PlaceLimiterPlugin = mock()
        val mockSettings: SettingManager = mock()
        `when`(mockSettings.noPermissionMessage).thenReturn(noPermissionText)
        val commandHandler: ReloadConfigCommand = InstanceFactory().createReloadConfigCommand(mockPlugin, mockSettings)
        val mockCommand: Command = mock()
        val mockCommandSender: CommandSender = mock()
        `when`(mockCommandSender.hasPermission(PermissionManager.PERMISSION_KEY_RELOAD)).thenReturn(false)

        commandHandler.onCommand(mockCommandSender, mockCommand, "", emptyArray())

        verify(mockPlugin, never()).reload()
        verify(mockCommandSender).sendMessage(noPermissionText)
        verify(mockCommandSender, never()).sendMessage("Configuration reloaded")
    }
}
