package de.poeschl.bukkit.placelimiter.models

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.bukkit.Location
import org.junit.Test
import java.util.*

class PlayerTest {

    val playerUUID = UUID.randomUUID()

    @Test
    fun testPlacementOfMaterial() {
        //WHEN
        val placedBlock: Block = mock()
        val neverPlacedBlock: Block = mock()
        val placedBlockCount = 42

        val testPlayer = Player(playerUUID)
        testPlayer.placedBlocks.put(placedBlock, placedBlockCount)

        //THEN
        val placedResult = testPlayer.getPlacementOfMaterial(placedBlock)
        val notPlacedResult = testPlayer.getPlacementOfMaterial(neverPlacedBlock)

        //VERIFY
        Assertions.assertThat(placedResult).isEqualTo(placedBlockCount)
        Assertions.assertThat(notPlacedResult).isEqualTo(0)
    }

    @Test
    fun testValidBreakLocation() {
        //WHEN
        val rightLocation: Location = mock()
        val wrongLocation: Location = mock()

        val testPlayer = Player(playerUUID)
        testPlayer.placedLocations.add(rightLocation)

        //THEN
        val rightResult = testPlayer.isBreakLocationValid(rightLocation)
        val wrongResult = testPlayer.isBreakLocationValid(wrongLocation)

        //VERIFY
        Assertions.assertThat(rightResult).isTrue()
        Assertions.assertThat(wrongResult).isFalse()
    }

    @Test
    fun testPlacementIncrease() {
        //WHEN
        val mockBlock: Block = mock()
        val mockLocation: Location = mock()

        val testPlayer = Player(playerUUID)

        //THEN
        testPlayer.increasePlacement(mockBlock, mockLocation)

        //VERIFY
        Assertions.assertThat(testPlayer.placedBlocks.keys.first()).isEqualTo(mockBlock)
        Assertions.assertThat(testPlayer.placedBlocks.values.first()).isEqualTo(1)
        Assertions.assertThat(testPlayer.placedLocations.first()).isEqualTo(mockLocation)
    }

    @Test
    fun testExistingPlacementIncrease() {
        //WHEN
        val mockBlock: Block = mock()
        val mockLocation: Location = mock()
        val newMockLocation: Location = mock()
        val placedBlockCount = 2

        val testPlayer = Player(playerUUID)
        testPlayer.placedBlocks.put(mockBlock, placedBlockCount)
        testPlayer.placedLocations.add(mockLocation)

        //THEN
        testPlayer.increasePlacement(mockBlock, newMockLocation)

        //VERIFY
        Assertions.assertThat(testPlayer.placedBlocks).isEqualTo(hashMapOf(Pair(mockBlock, placedBlockCount + 1)))
        Assertions.assertThat(testPlayer.placedLocations).isEqualTo(setOf(mockLocation, newMockLocation))
    }

    @Test
    fun testPlacementSetting() {
        //WHEN
        val mockBlock: Block = mock()
        val placedBlockCount = 42

        val testPlayer = Player(playerUUID)

        //THEN
        testPlayer.setPlacement(mockBlock, placedBlockCount)

        //VERIFY
        Assertions.assertThat(testPlayer.placedBlocks).isEqualTo(hashMapOf(Pair(mockBlock, placedBlockCount)))
    }

    @Test
    fun testPlacementDecrease() {
        //WHEN
        val mockBlock: Block = mock()
        val mockLocation: Location = mock()
        val placedBlockCount = 1

        val testPlayer = Player(playerUUID)
        testPlayer.placedBlocks.put(mockBlock, placedBlockCount)
        testPlayer.placedLocations.add(mockLocation)

        //THEN
        testPlayer.decreasePlacement(mockBlock, mockLocation)

        //VERIFY
        Assertions.assertThat(testPlayer.placedBlocks).isEqualTo(hashMapOf(Pair(mockBlock, placedBlockCount - 1)))
        Assertions.assertThat(testPlayer.placedLocations).doesNotContain(mockLocation)
    }

    @Test
    fun testPlacementDecreaseOnZero() {
        //WHEN
        val mockBlock: Block = mock()
        val mockLocation: Location = mock()

        val testPlayer = Player(playerUUID)
        testPlayer.placedBlocks.put(mockBlock, 0)

        //THEN
        testPlayer.decreasePlacement(mockBlock, mockLocation)

        //VERIFY
        Assertions.assertThat(testPlayer.placedBlocks).isEqualTo(hashMapOf(Pair(mockBlock, 0)))
    }

    @Test
    fun testPlacementDecreaseOnEmpty() {
        //WHEN
        val mockBlock: Block = mock()
        val mockLocation: Location = mock()

        val testPlayer = Player(playerUUID)

        //THEN
        testPlayer.decreasePlacement(mockBlock, mockLocation)

        //VERIFY
        Assertions.assertThat(testPlayer.placedBlocks).isEqualTo(hashMapOf(Pair(mockBlock, 0)))
    }

    @Test
    fun testEquals() {
        //WHEN
        val testPlayer = Player(playerUUID)
        val playerWithSameUuid = Player(playerUUID)
        val otherPlayer = Player(UUID.randomUUID())

        //THEN
        val correctEquals = testPlayer == playerWithSameUuid
        val failedEquals = testPlayer == otherPlayer

        //VERIFY
        Assertions.assertThat(correctEquals).isTrue()
        Assertions.assertThat(testPlayer.hashCode()).isEqualTo(playerWithSameUuid.hashCode())
        Assertions.assertThat(failedEquals).isFalse()

    }
}