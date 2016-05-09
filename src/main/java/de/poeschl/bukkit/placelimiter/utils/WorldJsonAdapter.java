package de.poeschl.bukkit.placelimiter.utils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldJsonAdapter {

    @FromJson
    World playerFromJson(String worldString) {
        World world = Bukkit.getServer().getWorld(worldString);
        return world;
    }

    @ToJson
    String playerToJson(World world) {
        return world.getName();
    }
}
