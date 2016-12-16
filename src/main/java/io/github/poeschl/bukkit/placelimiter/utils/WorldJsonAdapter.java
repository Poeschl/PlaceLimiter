package io.github.poeschl.bukkit.placelimiter.utils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldJsonAdapter {

    @FromJson
    World worldFromJson(String worldString) {
        World world = Bukkit.getServer().getWorld(worldString);
        return world;
    }

    @ToJson
    String worldToJson(World world) {
        return world.getName();
    }
}
