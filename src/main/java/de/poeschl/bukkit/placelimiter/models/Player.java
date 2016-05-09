package de.poeschl.bukkit.placelimiter.models;

import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.google.common.primitives.Ints.max;

public class Player {
    private UUID uuid;
    private HashMap<Block, Integer> placedBlocks;
    private Set<Location> placedLocations;

    public Player(UUID uuid) {
        this.uuid = uuid;
        this.placedBlocks = new HashMap<>();
        this.placedLocations = new HashSet<>();
    }

    public UUID getUUID() {
        return uuid;
    }

    public HashMap<Block, Integer> getPlacedBlocks() {
        return placedBlocks;
    }

    public void setPlacedLocations(Set<Location> placedLocations) {
        this.placedLocations = placedLocations;
    }

    public Set<Location> getPlacedLocations() {
        return placedLocations;
    }

    public int getPlacementOfMaterial(Block block) {
        return placedBlocks.containsKey(block) ? placedBlocks.get(block) : 0;
    }

    public boolean isBreakLocationValid(Location location) {
        return placedLocations.contains(location);
    }

    public void increasePlacement(Block block, Location location) {
        Integer placed = placedBlocks.get(block);

        placedBlocks.put(block, placed != null ? placed + 1 : 1);
        placedLocations.add(location);
    }

    public void setPlacement(Block block, int placements) {
        placedBlocks.put(block, placements);
    }

    public void decreasePlacement(Block block, Location location) {
        Integer placed = placedBlocks.get(block);

        placedBlocks.put(block, placed != null ? max(placed - 1, 0) : 0);
        placedLocations.remove(location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return uuid != null ? uuid.equals(player.uuid) : player.uuid == null;

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
