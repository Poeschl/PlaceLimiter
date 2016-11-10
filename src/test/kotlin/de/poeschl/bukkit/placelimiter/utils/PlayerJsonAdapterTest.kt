package de.poeschl.bukkit.placelimiter.utils

import com.nhaarman.mockito_kotlin.mock
import de.poeschl.bukkit.placelimiter.models.Block
import de.poeschl.bukkit.placelimiter.models.Player
import de.poeschl.bukkit.placelimiter.models.PlayerJson
import org.assertj.core.api.Assertions
import org.bukkit.Location
import org.bukkit.Material
import org.junit.Test
import java.util.*

class PlayerJsonAdapterTest {


    @Test
    fun testHashMapFromJson() {
        val playerUuid = UUID.randomUUID()
        val dummyJson = PlayerJson()
        val shouldResult = Player(playerUuid)
        val mockBlock: Block = Block(Material.DIRT)
        val blockPlaceCount = 5
        val mockLocation: Location = mock()

        dummyJson.placedBlocks.put(mockBlock.material.name + ":" + mockBlock.data, blockPlaceCount)
        dummyJson.placementLocations.add(mockLocation)
        dummyJson.uuid = playerUuid.toString()
        shouldResult.placedBlocks.put(mockBlock, blockPlaceCount)
        shouldResult.placedLocations.add(mockLocation)

        //WHEN
        val testAdapter = PlayerJsonAdapter()

        //THEN
        val fromJsonResult = testAdapter.playerFromJson(dummyJson)

        //VERIFY
        Assertions.assertThat(fromJsonResult).isEqualTo(shouldResult)
    }

    @Test
    fun testHashMapToJson() {
        val playerUuid = UUID.randomUUID()
        val dummyPlayer = Player(playerUuid)
        val shouldResult = PlayerJson()
        val mockBlock: Block = Block(Material.DIRT)
        val blockPlaceCount = 5
        val mockLocation: Location = mock()

        shouldResult.placedBlocks.put(mockBlock.material.name + ":" + mockBlock.data, blockPlaceCount)
        shouldResult.placementLocations.add(mockLocation)
        shouldResult.uuid = playerUuid.toString()
        dummyPlayer.placedBlocks.put(mockBlock, blockPlaceCount)
        dummyPlayer.placedLocations.add(mockLocation)

        //WHEN
        val testAdapter = PlayerJsonAdapter()

        //THEN
        val toJsonResult = testAdapter.playerToJson(dummyPlayer)

        //VERIFY
        Assertions.assertThat(toJsonResult.uuid).isEqualTo(shouldResult.uuid)
        Assertions.assertThat(toJsonResult.placedBlocks).isEqualTo(shouldResult.placedBlocks)
        Assertions.assertThat(toJsonResult.placementLocations).isEqualTo(shouldResult.placementLocations)
    }
}