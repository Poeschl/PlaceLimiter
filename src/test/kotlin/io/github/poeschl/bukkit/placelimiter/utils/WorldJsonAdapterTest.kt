package io.github.poeschl.bukkit.placelimiter.utils

import com.nhaarman.mockito_kotlin.mock
import org.assertj.core.api.Assertions
import org.bukkit.World
import org.junit.Ignore
import org.junit.Test

//TODO: Utilize powermock to mock the static Bukkit calls - Waiting for mokito 2 support of powermock
class WorldJsonAdapterTest {

    @Ignore
    @Test
    fun testPlayerFromJson() {
        val dummyName = "Overworld"
        val shouldWorld: World = mock()

        //WHEN
        val testAdapter = WorldJsonAdapter()

        //THEN
        val fromJsonResult = testAdapter.worldFromJson(dummyName)

        //VERIFY
        Assertions.assertThat(fromJsonResult).isEqualTo(shouldWorld)
    }

    @Ignore
    @Test
    fun testHashMapToJson() {
        val dummyWorld: World = mock()
        val resultName = "Overworld"

        //WHEN
        val testAdapter = WorldJsonAdapter()

        //THEN
        val fromJsonResult = testAdapter.worldToJson(dummyWorld)

        //VERIFY
        Assertions.assertThat(fromJsonResult).isEqualTo(resultName)
    }

}