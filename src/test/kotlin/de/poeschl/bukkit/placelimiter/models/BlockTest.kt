package de.poeschl.bukkit.placelimiter.models

import org.assertj.core.api.Assertions
import org.bukkit.Material
import org.junit.Test

class BlockTest {

    @Test
    fun testEquals() {
        //WHEN
        val blockA = Block(Material.DIRT)
        val blockB = Block(Material.DIRT)

        //THEN
        val equals = blockA == blockB

        //Verify
        Assertions.assertThat(equals).isTrue()
        Assertions.assertThat(blockA.hashCode()).isEqualTo(blockB.hashCode())
    }

    @Test
    fun testEqualsWithDataId() {
        //WHEN
        val blockA = Block(Material.DIRT, 3)
        val blockB = Block(Material.DIRT, 3)

        //THEN
        val equals = blockA == blockB

        //Verify
        Assertions.assertThat(equals).isTrue()
        Assertions.assertThat(blockA.hashCode()).isEqualTo(blockB.hashCode())
    }

    @Test
    fun testEqualsMixed() {
        //WHEN
        val blockA = Block(Material.DIRT, 0)
        val blockB = Block(Material.DIRT, 42)
        val blockC = Block(Material.DIRT)

        //THEN
        val equalsNonData = blockA == blockC
        val equalsNonData2 = blockB == blockC

        //Verify
        Assertions.assertThat(equalsNonData).isTrue()
        Assertions.assertThat(blockA.hashCode()).isEqualTo(blockC.hashCode())
        Assertions.assertThat(equalsNonData2).isTrue()
        Assertions.assertThat(blockB.hashCode()).isEqualTo(blockC.hashCode())
    }

    @Test
    fun testNotEquals() {
        //WHEN
        val blockA = Block(Material.DIRT)
        val blockB = Block(Material.STONE)

        //THEN
        val equals = blockA == blockB

        //Verify
        Assertions.assertThat(equals).isFalse()
        Assertions.assertThat(blockA.hashCode()).isNotEqualTo(blockB.hashCode())
    }

    @Test
    fun testNotEqualsWithDataId() {
        //WHEN
        val blockA = Block(Material.DIRT, 1)
        val blockB = Block(Material.DIRT, 3)

        //THEN
        val equals = blockA == blockB

        //Verify
        Assertions.assertThat(equals).isFalse()
        Assertions.assertThat(blockA.hashCode()).isEqualTo(blockB.hashCode())
    }

    @Test
    fun testToString() {
        //WHEN
        val blockA = Block(Material.DIRT)
        val blockB = Block(Material.DIRT, 0)
        val blockC = Block(Material.DIRT, 42)

        //THEN
        val resultWithNoData = blockA.toString()
        val resultWithDataZero = blockB.toString()
        val resultWithData = blockC.toString()

        //VERIFY
        Assertions.assertThat(resultWithNoData).isEqualTo("DIRT")
        Assertions.assertThat(resultWithDataZero).isEqualTo("DIRT:0")
        Assertions.assertThat(resultWithData).isEqualTo("DIRT:42")
    }
}