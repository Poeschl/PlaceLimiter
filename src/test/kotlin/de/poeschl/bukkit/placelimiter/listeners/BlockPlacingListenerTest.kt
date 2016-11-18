package de.poeschl.bukkit.placelimiter.listeners


import com.nhaarman.mockito_kotlin.*
import de.poeschl.bukkit.placelimiter.managers.PermissionManager
import de.poeschl.bukkit.placelimiter.managers.PlacementManager
import de.poeschl.bukkit.placelimiter.managers.SettingManager
import de.poeschl.bukkit.placelimiter.models.Block
import de.poeschl.bukkit.placelimiter.utils.InstanceFactory
import org.assertj.core.api.Assertions
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.junit.Test
import org.mockito.Mockito.`when`
import java.util.logging.Logger

class BlockPlacingListenerTest {

    private val limitReachedString = "Limit Reached %s"
    private val notFromPlayerPlacedString = "Not your %s"

    @Test
    fun testAllowedPlacing() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockPlaceEvent: BlockPlaceEvent = mock()
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.getPlacementOfMaterial(testBlock)).thenReturn(0)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlaceEvent.player).thenReturn(mockBukkitPlayer)
        `when`(mockPlaceEvent.blockPlaced).thenReturn(mockBukkitBlock)

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockPlace(mockPlaceEvent)

        //VERIFY
        verify(mockPlayer, com.nhaarman.mockito_kotlin.times(1)).increasePlacement(testBlock, testLocation)
        verify(mockPlacementManager).savePlayer(mockPlayer)
    }

    @Test
    fun testDeniedPlacing() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockPlaceEvent: BlockPlaceEvent = mock()
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.getPlacementOfMaterial(any())).thenReturn(1)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlaceEvent.player).thenReturn(mockBukkitPlayer)
        `when`(mockPlaceEvent.blockPlaced).thenReturn(mockBukkitBlock)

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.limitPlaceReachedMessage).thenReturn(limitReachedString)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockPlace(mockPlaceEvent)

        //VERIFY
        verify(mockPlayer, never()).increasePlacement(testBlock, testLocation)
        verify(mockPlaceEvent).isCancelled = true
        verify(mockBukkitPlayer).sendMessage(String.format(limitReachedString, testBlock.toString()))
    }

    @Test
    fun testDeniedPlacingWithoutDataId() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockPlaceEvent: BlockPlaceEvent = mock()
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT, 42)
        val restrictedBlock = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.getPlacementOfMaterial(any())).thenReturn(1)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlaceEvent.player).thenReturn(mockBukkitPlayer)
        `when`(mockPlaceEvent.blockPlaced).thenReturn(mockBukkitBlock)

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.limitPlaceReachedMessage).thenReturn(limitReachedString)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(restrictedBlock)

        //THEN
        blockPlacingListener.onBlockPlace(mockPlaceEvent)

        //VERIFY
        verify(mockPlayer, never()).increasePlacement(restrictedBlock, testLocation)
        verify(mockPlaceEvent).isCancelled = true
        verify(mockBukkitPlayer).sendMessage(String.format(limitReachedString, restrictedBlock.toString()))
    }

    @Test
    fun testAllowedPlacingWithByte() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockPlaceEvent: BlockPlaceEvent = mock()
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT, 26)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.getPlacementOfMaterial(testBlock)).thenReturn(0)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlaceEvent.player).thenReturn(mockBukkitPlayer)
        `when`(mockPlaceEvent.blockPlaced).thenReturn(mockBukkitBlock)

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockPlace(mockPlaceEvent)

        //VERIFY
        verify(mockPlayer, com.nhaarman.mockito_kotlin.times(1)).increasePlacement(testBlock, testLocation)
        verify(mockPlacementManager).savePlayer(mockPlayer)
    }

    @Test
    fun testPlacingWithOverride() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockPlaceEvent: BlockPlaceEvent = mock()
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)

        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(true)

        `when`(mockPlaceEvent.player).thenReturn(mockBukkitPlayer)
        `when`(mockPlaceEvent.blockPlaced).thenReturn(mockBukkitBlock)

        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockPlace(mockPlaceEvent)

        //VERIFY
        verify(mockPlaceEvent, never()).isCancelled = true
        verify(mockPlayer, never()).increasePlacement(testBlock, testLocation)
    }

    @Test
    fun testPlacingUnlimitedBlocks() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockPlaceEvent: BlockPlaceEvent = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)

        `when`(mockPlaceEvent.blockPlaced).thenReturn(mockBukkitBlock)

        `when`(mockSettings.isMaterialLimited(any())).thenReturn(false)

        //THEN
        blockPlacingListener.onBlockPlace(mockPlaceEvent)

        //VERIFY
        verify(mockPlaceEvent, never()).isCancelled = true
        verify(mockPlayer, never()).increasePlacement(testBlock, testLocation)
    }

    @Test
    fun testAllowedBreaking() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.isBreakLocationValid(testLocation)).thenReturn(true)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")


        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, com.nhaarman.mockito_kotlin.times(1)).decreasePlacement(testBlock, testLocation)
        verify(mockPlacementManager).savePlayer(mockPlayer)
    }

    @Test
    fun testAllowedBreakingWithoutId() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT, 42)
        val restrictedBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.isBreakLocationValid(testLocation)).thenReturn(true)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")


        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(restrictedBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, com.nhaarman.mockito_kotlin.times(1)).decreasePlacement(restrictedBlock, testLocation)
        verify(mockPlacementManager).savePlayer(mockPlayer)
    }

    @Test
    fun testBreakingOnUnknownLocation() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.isBreakLocationValid(testLocation)).thenReturn(false)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockPlacementManager.allPlayers).thenReturn(arrayListOf(mockPlayer))
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, never()).decreasePlacement(testBlock, testLocation)
    }

    @Test
    fun testBreakingWithOverride() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(true)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, never()).decreasePlacement(testBlock, testLocation)
    }

    @Test
    fun testBreakingOtherBlockWithOverride() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockPlayer2: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.isBreakLocationValid(testLocation)).thenReturn(false)
        `when`(mockPlayer2.isBreakLocationValid(testLocation)).thenReturn(true)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(true)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockPlacementManager.allPlayers).thenReturn(arrayListOf(mockPlayer, mockPlayer2))
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, never()).decreasePlacement(testBlock, testLocation)
        verify(mockPlayer2, com.nhaarman.mockito_kotlin.times(1)).decreasePlacement(testBlock, testLocation)
        verify(mockPlacementManager).savePlayer(mockPlayer2)
    }

    @Test
    fun testAllowedBreakingWithByte() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT, 26)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.isBreakLocationValid(testLocation)).thenReturn(true)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")


        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.getMaterialLimit(any())).thenReturn(1)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, com.nhaarman.mockito_kotlin.times(1)).decreasePlacement(testBlock, testLocation)
        verify(mockPlacementManager).savePlayer(mockPlayer)
    }

    @Test
    fun testBreakingFromOtherPlayer() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockPlayer2: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockPlayer.isBreakLocationValid(testLocation)).thenReturn(false)
        `when`(mockPlayer2.isBreakLocationValid(testLocation)).thenReturn(true)
        `when`(mockBukkitPlayer.hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)).thenReturn(false)
        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockPlacementManager.getPlayer(anyOrNull())).thenReturn(mockPlayer)
        `when`(mockPlacementManager.allPlayers).thenReturn(arrayListOf(mockPlayer, mockPlayer2))
        `when`(mockSettings.isMaterialLimited(any())).thenReturn(true)
        `when`(mockSettings.notFromPlayerPlacedMessage).thenReturn(notFromPlayerPlacedString)
        `when`(mockSettings.getRestrictedBlockOf(testBlock)).thenReturn(testBlock)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        verify(mockPlayer, never()).decreasePlacement(testBlock, testLocation)
        verify(mockPlayer2, never()).decreasePlacement(testBlock, testLocation)
        Assertions.assertThat(mockBreakEvent.isCancelled).isTrue()
        verify(mockBukkitPlayer).sendMessage(String.format(notFromPlayerPlacedString, testBlock.toString()))
    }

    @Test
    fun testBreakingUnlimitedBlocks() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockSettings: SettingManager = mock()
        val mockPlacementManager: PlacementManager = mock()
        val blockPlacingListener = InstanceFactory().createBlockPlacingListener(mockLogger, mockSettings, mockPlacementManager)
        val mockBukkitPlayer: Player = mock()
        val mockPlayer: de.poeschl.bukkit.placelimiter.models.Player = mock()
        val mockBukkitBlock: org.bukkit.block.Block = mock()

        val testBlock: Block = Block(Material.DIRT)
        val testLocation: Location = Location(mock("World"), 0.0, 0.0, 0.0)
        val mockBreakEvent: BlockBreakEvent = BlockBreakEvent(mockBukkitBlock, mockBukkitPlayer)

        `when`(mockBukkitBlock.type).thenReturn(testBlock.material)
        @Suppress("DEPRECATION")
        `when`(mockBukkitBlock.data).thenReturn(testBlock.data)
        `when`(mockBukkitBlock.location).thenReturn(testLocation)

        `when`(mockBukkitPlayer.name).thenReturn("Sam")

        `when`(mockSettings.isMaterialLimited(any())).thenReturn(false)

        //THEN
        blockPlacingListener.onBlockBreak(mockBreakEvent)

        //VERIFY
        Assertions.assertThat(mockBreakEvent.isCancelled).isFalse()
        verify(mockPlayer, never()).decreasePlacement(testBlock, testLocation)
    }
}