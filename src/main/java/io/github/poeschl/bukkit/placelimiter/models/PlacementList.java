package io.github.poeschl.bukkit.placelimiter.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlacementList {

    private List<Player> playerList;

    public PlacementList() {
        playerList = new ArrayList<>();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public Player getPlayer(UUID uuid) {
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            if (player.getUUID().equals(uuid)) {
                return player;
            }
        }
        return new Player(uuid);
    }

    public void updatePlayer(Player player){
        if(playerList.contains(player)){
            playerList.set(playerList.indexOf(player), player);
        } else {
            playerList.add(player);
        }
    }
}
