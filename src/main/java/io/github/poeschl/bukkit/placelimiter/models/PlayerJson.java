package io.github.poeschl.bukkit.placelimiter.models;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PlayerJson {
    public String uuid;
    public HashMap<String, Integer> placedBlocks;
    public Set<Location> placementLocations;

    public PlayerJson() {
        placedBlocks = new HashMap<>();
        placementLocations = new HashSet<>();
    }
}
