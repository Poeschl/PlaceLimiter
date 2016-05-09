package de.poeschl.bukkit.placelimiter.managers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import de.poeschl.bukkit.placelimiter.models.PlacementList;
import de.poeschl.bukkit.placelimiter.models.Player;
import de.poeschl.bukkit.placelimiter.utils.HashMapStringIntAdapter;
import de.poeschl.bukkit.placelimiter.utils.PlayerJsonAdapter;
import de.poeschl.bukkit.placelimiter.utils.WorldJsonAdapter;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class PlacementManager {

    private static final String PLACEMENTS_FILE_NAME = "placements.json";

    private File placementFile;
    private Logger logger;
    private PlacementList placements;
    private JsonAdapter<PlacementList> jsonAdapter;

    public PlacementManager(Plugin plugin) {
        logger = plugin.getLogger();
        placements = new PlacementList();
        placementFile = new File(plugin.getDataFolder(), PLACEMENTS_FILE_NAME);

        Moshi moshi = new Moshi.Builder().add(new PlayerJsonAdapter())
                .add(new HashMapStringIntAdapter())
                .add(new WorldJsonAdapter())
                .build();
        jsonAdapter = moshi.adapter(PlacementList.class);
    }

    public List<Player> getAllPlayers() {
        return placements.getPlayerList();
    }

    public Player getPlayer(UUID uuid) {
        return placements.getPlayer(uuid);
    }

    public void savePlayer(Player player) {
        placements.updatePlayer(player);
        save();
    }

    public void save() {
        try {
            BufferedSink fileSink = Okio.buffer(Okio.sink(placementFile));
            jsonAdapter.toJson(fileSink, placements);
            fileSink.flush();
        } catch (IOException ex) {
            logger.warning("Error on writing the placement file!");
        }
    }

    public void load() {
        if (placementFile.exists()) {
            try {
                BufferedSource fileSource = Okio.buffer(Okio.source(placementFile));
                placements = jsonAdapter.fromJson(fileSource);
            } catch (IOException ex) {
                logger.warning("Error on reading the placement file!");
            }
        }
    }
}
