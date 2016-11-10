package de.poeschl.bukkit.placelimiter.models

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.bukkit.Location
import org.bukkit.Material
import org.junit.Test
import org.mockito.Mockito.`when`
import java.util.*


class PlacementListTest {

    @Test
    fun testGetNewPlayer() {

        val playerUUID = UUID.randomUUID()

        //WHEN
        val placementList = PlacementList()

        //THEN
        val resultPlayer = placementList.getPlayer(playerUUID)

        //VERIFY
        Assertions.assertThat(resultPlayer).isEqualTo(Player(playerUUID))
    }

    @Test
    fun testGetPlayer() {

        val playerUUID = UUID.randomUUID()

        //WHEN
        val mockPlayer: Player = mock()
        `when`(mockPlayer.uuid).thenReturn(playerUUID)

        val placementList = PlacementList()
        placementList.playerList.add(mockPlayer)

        //THEN
        val resultPlayer = placementList.getPlayer(playerUUID)

        //VERIFY
        Assertions.assertThat(resultPlayer).isEqualTo(mockPlayer)
    }

    @Test
    fun testUpdateNewPlayer() {

        val playerUUID = UUID.randomUUID()

        //WHEN
        val mockPlayer: Player = mock()
        `when`(mockPlayer.uuid).thenReturn(playerUUID)

        val placementList = PlacementList()

        //THEN
        placementList.updatePlayer(mockPlayer)

        //VERIFY
        Assertions.assertThat(placementList.playerList).containsExactlyElementsOf(arrayListOf(mockPlayer))
    }

    @Test
    fun testUpdatePlayer() {

        val playerUUID = UUID.randomUUID()

        //WHEN
        val player1 = Player(playerUUID)
        val player2 = Player(playerUUID)
        player2.increasePlacement(Block(Material.DIRT), Location(mock("World"), 0.0, 0.0, 0.0))

        val placementList = PlacementList()
        placementList.playerList.add(player1)

        //THEN
        placementList.updatePlayer(player2)

        //VERIFY
        Assertions.assertThat(placementList.playerList).containsExactlyElementsOf(arrayListOf(player2))
        Assertions.assertThat(placementList.playerList.first().placedBlocks.size).isEqualTo(1)
        Assertions.assertThat(placementList.playerList.first().placedLocations.size).isEqualTo(1)
    }
}