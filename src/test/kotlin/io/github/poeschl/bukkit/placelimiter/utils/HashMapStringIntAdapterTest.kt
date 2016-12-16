package io.github.poeschl.bukkit.placelimiter.utils

import org.assertj.core.api.Assertions
import org.junit.Test

class HashMapStringIntAdapterTest {

    @Test
    fun testHashMapFromJson() {
        val dummyJson = listOf("STONE|0", "DIRT:2|2")
        val shouldResult = hashMapOf(Pair("STONE", 0), Pair("DIRT:2", 2))

        //WHEN
        val testAdapter = HashMapStringIntAdapter()

        //THEN
        val fromJsonResult = testAdapter.hashMapFromJson(dummyJson)

        //VERIFY
        Assertions.assertThat(fromJsonResult).isEqualTo(shouldResult)
    }

    @Test
    fun testHashMapToJson() {
        val dummyMap = hashMapOf(Pair("STONE", 0), Pair("DIRT:2", 2))
        val shouldJson = listOf("STONE|0", "DIRT:2|2")

        //WHEN
        val testAdapter = HashMapStringIntAdapter()

        //THEN
        val toJsonResult = testAdapter.hashMapToJson(dummyMap)

        //VERIFY
        Assertions.assertThat(toJsonResult).isEqualTo(shouldJson)
    }
}