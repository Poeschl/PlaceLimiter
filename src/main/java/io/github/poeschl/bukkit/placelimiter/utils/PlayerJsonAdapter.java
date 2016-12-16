package io.github.poeschl.bukkit.placelimiter.utils;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import io.github.poeschl.bukkit.placelimiter.models.Block;
import io.github.poeschl.bukkit.placelimiter.models.Player;
import io.github.poeschl.bukkit.placelimiter.models.PlayerJson;
import org.bukkit.Material;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public final class PlayerJsonAdapter {
    @FromJson
    Player playerFromJson(PlayerJson playerJson) {
        Player player = new Player(UUID.fromString(playerJson.uuid));

        Set<String> keys = playerJson.placedBlocks.keySet();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String currentKey = iterator.next();

            String name = currentKey.substring(0, currentKey.indexOf(":"));
            int data = Integer.parseInt(currentKey.substring(currentKey.indexOf(":") + 1, currentKey.length()));

            player.setPlacement(new Block(Material.getMaterial(name), (byte) data), playerJson.placedBlocks.get(currentKey));
        }
        player.setPlacedLocations(playerJson.placementLocations);
        return player;
    }

    @ToJson
    PlayerJson playerToJson(Player player) {
        PlayerJson playerJson = new PlayerJson();

        playerJson.uuid = player.getUUID().toString();

        Set<Block> keys = player.getPlacedBlocks().keySet();
        for (Iterator<Block> iterator = keys.iterator(); iterator.hasNext(); ) {
            Block currentBlock = iterator.next();

            playerJson.placedBlocks.put(currentBlock.getMaterial().name() + ":" + currentBlock.getData(), player.getPlacedBlocks().get(currentBlock));
        }
        playerJson.placementLocations = player.getPlacedLocations();
        return playerJson;
    }
}
