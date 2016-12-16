package io.github.poeschl.bukkit.placelimiter.managers

import com.nhaarman.mockito_kotlin.mock
import io.github.poeschl.bukkit.placelimiter.models.Block
import io.github.poeschl.bukkit.placelimiter.utils.InstanceFactory
import org.assertj.core.api.Assertions
import org.bukkit.Material
import org.bukkit.configuration.file.FileConfiguration
import org.junit.Test
import org.mockito.Mockito.`when`
import java.util.logging.Logger

class SettingManagerTest {

    @Test
    fun testConfigUpdate() {
        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val testConfig: FileConfiguration = mock()
        val settingsManager = SettingManager(mockConfig, mockLogger)

        `when`(mockConfig.getString(SettingManager.NO_PERMISSION_MESSAGE_KEY)).thenReturn("Default")
        `when`(testConfig.getString(SettingManager.NO_PERMISSION_MESSAGE_KEY)).thenReturn("Updated")

        //THEN
        settingsManager.updateConfig(testConfig)

        //VERIFY
        Assertions.assertThat(settingsManager.noPermissionMessage).isEqualTo("Updated")
    }

    @Test
    fun testMessages() {

        val noPermString = "No Permission"
        val placeLimitString = "Place Limit"
        val notFromYouString = "Not From You"

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)

        `when`(mockConfig.getString(SettingManager.NO_PERMISSION_MESSAGE_KEY)).thenReturn(noPermString)
        `when`(mockConfig.getString(SettingManager.LIMIT_PLACE_REACHED_MESSAGE_KEY)).thenReturn(placeLimitString)
        `when`(mockConfig.getString(SettingManager.NOT_PLACED_FROM_THIS_PLAYER_KEY)).thenReturn(notFromYouString)

        //THEN
        val noPermResult = settingsManager.noPermissionMessage
        val placeLimitResult = settingsManager.limitPlaceReachedMessage
        val notFromYouResult = settingsManager.notFromPlayerPlacedMessage

        //VERIFY
        Assertions.assertThat(noPermResult).isEqualTo(noPermString)
        Assertions.assertThat(placeLimitResult).isEqualTo(placeLimitString)
        Assertions.assertThat(notFromYouResult).isEqualTo(notFromYouString)
    }

    @Test
    fun testColorMessages() {

        val noPermString = "&4No Permission"
        val placeLimitString = "&3Place Limit"
        val notFromYouString = "&4Not From You"

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)

        `when`(mockConfig.getString(SettingManager.NO_PERMISSION_MESSAGE_KEY)).thenReturn(noPermString)
        `when`(mockConfig.getString(SettingManager.LIMIT_PLACE_REACHED_MESSAGE_KEY)).thenReturn(placeLimitString)
        `when`(mockConfig.getString(SettingManager.NOT_PLACED_FROM_THIS_PLAYER_KEY)).thenReturn(notFromYouString)

        //THEN
        val noPermResult = settingsManager.noPermissionMessage
        val placeLimitResult = settingsManager.limitPlaceReachedMessage
        val notFromYouResult = settingsManager.notFromPlayerPlacedMessage

        //VERIFY
        Assertions.assertThat(noPermResult).doesNotContain("&4")
        Assertions.assertThat(placeLimitResult).doesNotContain("&3")
        Assertions.assertThat(notFromYouResult).doesNotContain("&4")

        Assertions.assertThat(noPermResult).contains("§4")
        Assertions.assertThat(placeLimitResult).contains("§3")
        Assertions.assertThat(notFromYouResult).contains("§4")
    }

    @Test
    fun testMaterialLimitCheck() {

        val stoneBlock = Block(Material.STONE)
        val dirtBlock = Block(Material.DIRT, 2)

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)
        val dummyRestrictions = arrayListOf(hashMapOf(Pair(stoneBlock.toString(), 1)))
        `when`(mockConfig.getList(SettingManager.PLACE_RULES_KEY)).thenReturn(dummyRestrictions)

        //THEN
        val stoneLimited = settingsManager.isMaterialLimited(stoneBlock)
        val dirtLimited = settingsManager.isMaterialLimited(dirtBlock)

        //VERIFY
        Assertions.assertThat(stoneLimited).isTrue()
        Assertions.assertThat(dirtLimited).isFalse()
    }

    @Test
    fun testGetMaterialLimit() {

        val stoneBlock = Block(Material.STONE)

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)
        val dummyRestrictions = arrayListOf(hashMapOf(Pair(stoneBlock.toString(), 42)))
        `when`(mockConfig.getList(SettingManager.PLACE_RULES_KEY)).thenReturn(dummyRestrictions)

        //THEN
        val stoneLimit = settingsManager.getMaterialLimit(stoneBlock)

        //VERIFY
        Assertions.assertThat(stoneLimit).isEqualTo(42)
    }

    @Test
    fun testGetLimitedBlock() {

        val limitedBlock = Block(Material.DIRT)
        val placedBlock = Block(Material.DIRT, 2)

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)
        val dummyRestrictions = arrayListOf(hashMapOf(Pair(limitedBlock.toString(), 1)))
        `when`(mockConfig.getList(SettingManager.PLACE_RULES_KEY)).thenReturn(dummyRestrictions)

        //THEN
        val resultLimitedBlock = settingsManager.getRestrictedBlockOf(placedBlock)

        //VERIFY
        Assertions.assertThat(resultLimitedBlock).isEqualTo(limitedBlock)
        Assertions.assertThat(resultLimitedBlock.data).isEqualTo(limitedBlock.data)
    }

    @Test
    fun testClearCache() {
        val stoneBlock = Block(Material.STONE)

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)
        val dummyRestrictions = arrayListOf(hashMapOf(Pair(stoneBlock.toString(), 42)))
        `when`(mockConfig.getList(SettingManager.PLACE_RULES_KEY)).thenReturn(dummyRestrictions)

        //THEN
        settingsManager.updateCache()
        settingsManager.clearCache()

        //VERIFY
        Assertions.assertThat(settingsManager.cacheRuleList).isNull()
        Assertions.assertThat(settingsManager.cacheLimitedBlocks).isNull()
    }

    @Test
    fun testCacheUpdate() {

        val stoneBlock = Block(Material.STONE)
        val dirtBlock = Block(Material.DIRT, 2)
        val airBlock = Block(Material.AIR)

        //WHEN
        val mockConfig: FileConfiguration = mock()
        val mockLogger: Logger = mock()

        val settingsManager = InstanceFactory().createSettingsManager(mockConfig, mockLogger)
        val dummyRestrictions = arrayListOf(hashMapOf(Pair(stoneBlock.material.name + ":" + stoneBlock.data, 2)),
                hashMapOf(Pair(dirtBlock.toString(), 1)),
                hashMapOf(Pair(airBlock.material.name, 3)))
        `when`(mockConfig.getList(SettingManager.PLACE_RULES_KEY)).thenReturn(dummyRestrictions)

        //THEN
        settingsManager.updateCache()

        //VERIFY
        Assertions.assertThat(settingsManager.getMaterialLimit(stoneBlock)).isEqualTo(2)
        Assertions.assertThat(settingsManager.getMaterialLimit(dirtBlock)).isEqualTo(1)
        Assertions.assertThat(settingsManager.getMaterialLimit(airBlock)).isEqualTo(3)
    }
}