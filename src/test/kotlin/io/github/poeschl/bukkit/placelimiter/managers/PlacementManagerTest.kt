package io.github.poeschl.bukkit.placelimiter.managers

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.squareup.moshi.Moshi
import io.github.poeschl.bukkit.placelimiter.models.PlacementList
import io.github.poeschl.bukkit.placelimiter.models.Player
import io.github.poeschl.bukkit.placelimiter.utils.HashMapStringIntAdapter
import io.github.poeschl.bukkit.placelimiter.utils.InstanceFactory
import io.github.poeschl.bukkit.placelimiter.utils.PlayerJsonAdapter
import io.github.poeschl.bukkit.placelimiter.utils.WorldJsonAdapter
import okio.Okio
import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import java.io.File
import java.util.*
import java.util.logging.Logger

class PlacementManagerTest {

    val testFolder: File = File("build/test")

    @Before
    fun setUp() {
        if (!testFolder.exists()) {
            testFolder.mkdirs()
        }
    }

    @After
    fun tearDown() {
//        testFolder.listFiles().forEach { it.writeBytes(ByteArray(0)) }
    }

    @Test
    fun testGetAllPlayers() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockPlacements: PlacementList = mock()
        val player1: Player = mock()
        val player2: Player = mock()
        val player3: Player = mock()
        `when`(mockPlacements.playerList).thenReturn(arrayListOf(player1, player2, player3))

        val placementManager: PlacementManager = PlacementManager(mockLogger, testFolder, mockPlacements)

        //THEN
        val playerList = placementManager.allPlayers

        //VERIFY
        Assertions.assertThat(playerList).containsExactlyElementsOf(arrayListOf(player1, player2, player3))
    }

    @Test
    fun testGetOnePlayer() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockPlacements: PlacementList = mock()
        val player: Player = mock()
        `when`(mockPlacements.getPlayer(any())).thenReturn(player)

        val placementManager: PlacementManager = PlacementManager(mockLogger, testFolder, mockPlacements)

        //THEN
        val playerResult = placementManager.getPlayer(UUID.randomUUID())

        //VERIFY
        Assertions.assertThat(playerResult).isEqualTo(player)
    }

    @Test
    fun testPlayerSaving() {
        //WHEN
        val mockLogger: Logger = mock()
        val mockPlacements: PlacementList = mock()
        val player: Player = mock()
        `when`(mockPlacements.getPlayer(any())).thenReturn(player)

        val placementManager: PlacementManager = PlacementManager(mockLogger, testFolder, mockPlacements)

        //THEN
        placementManager.savePlayer(player)

        //VERIFY
        verify(mockPlacements, com.nhaarman.mockito_kotlin.times(1)).updatePlayer(player)
    }

    @Test
    fun testGlobalSave() {
        //WHEN
        val mockLogger: Logger = mock()
        val player: Player = Player(UUID.randomUUID())
        val placements: PlacementList = InstanceFactory().createPlacementList()
        placements.updatePlayer(player)

        val placementManager: PlacementManager = PlacementManager(mockLogger, testFolder, placements)

        //THEN
        placementManager.save()

        //VERIFY
        val moshi = Moshi.Builder().add(PlayerJsonAdapter()).add(HashMapStringIntAdapter()).add(WorldJsonAdapter()).build()
        val jsonAdapter = moshi.adapter(PlacementList::class.java)
        val fileSource = Okio.buffer(Okio.source(File(testFolder, "placements.json")))
        val fileResults = jsonAdapter.fromJson(fileSource)
        fileSource.close()

        Assertions.assertThat(fileResults.playerList).containsExactlyElementsOf(arrayListOf(player))
    }

    @Test
    fun testGlobalLoad() {
        //WHEN
        val mockLogger: Logger = mock()
        val player: Player = Player(UUID.fromString("0ffdf1f1-cf9a-4608-86c2-9c8d33da51c2"))
        val placements: PlacementList = InstanceFactory().createPlacementList()
        val resourcesFolder = File(Thread.currentThread().contextClassLoader.getResource("placements.json").path).parentFile

        val placementManager: PlacementManager = PlacementManager(mockLogger, resourcesFolder, placements)

        //THEN
        placementManager.load()

        //VERIFY
        Assertions.assertThat(placementManager.allPlayers).containsExactlyElementsOf(arrayListOf(player))
    }
}