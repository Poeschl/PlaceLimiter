package io.github.poeschl.bukkit.placelimiter.listeners;

import io.github.poeschl.bukkit.placelimiter.managers.PermissionManager;
import io.github.poeschl.bukkit.placelimiter.managers.PlacementManager;
import io.github.poeschl.bukkit.placelimiter.managers.SettingManager;
import io.github.poeschl.bukkit.placelimiter.models.Block;
import io.github.poeschl.bukkit.placelimiter.models.Player;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Logger;

public class BlockPlacingListener implements Listener {

    private Logger logger;
    private SettingManager settingManager;
    private PlacementManager placementManager;

    public BlockPlacingListener(Logger logger, SettingManager settingManager, PlacementManager placementManager) {
        this.logger = logger;
        this.settingManager = settingManager;
        this.placementManager = placementManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        @SuppressWarnings("deprecation")
        Block currentBlock = new Block(event.getBlockPlaced().getType(), event.getBlockPlaced().getData());

        if (settingManager.isMaterialLimited(currentBlock)) {
            Block pureRestrictedBlock = settingManager.getRestrictedBlockOf(currentBlock);

            if (!event.getPlayer().hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)) {
                Player currentPlayer = placementManager.getPlayer(event.getPlayer().getUniqueId());
                int alreadyPlaced = currentPlayer.getPlacementOfMaterial(currentBlock);
                int limit = settingManager.getMaterialLimit(currentBlock);

                if (alreadyPlaced < limit) {
                    currentPlayer.increasePlacement(pureRestrictedBlock, event.getBlockPlaced().getLocation());
                    logger.info(event.getPlayer().getName() + " placed down " + pureRestrictedBlock.toString());
                    placementManager.savePlayer(currentPlayer);
                } else {
                    event.setCancelled(true);
                    String message = String.format(settingManager.getLimitPlaceReachedMessage(), pureRestrictedBlock.toString());
                    event.getPlayer().sendMessage(message);
                }
            } else {
                logger.info(event.getPlayer().getName() + " placed down " + pureRestrictedBlock.toString() + " with Override");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        @SuppressWarnings("deprecation")
        Block currentBlock = new Block(event.getBlock().getType(), event.getBlock().getData());
        Location blockLocation = event.getBlock().getLocation();

        if (settingManager.isMaterialLimited(currentBlock)) {
            Block pureRestrictedBlock = settingManager.getRestrictedBlockOf(currentBlock);

            if (!event.getPlayer().hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)) {
                Player currentPlayer = placementManager.getPlayer(event.getPlayer().getUniqueId());

                if (currentPlayer.isBreakLocationValid(blockLocation)) {
                    currentPlayer.decreasePlacement(pureRestrictedBlock, blockLocation);
                    logger.info(event.getPlayer().getName() + " removed " + pureRestrictedBlock.toString());
                    placementManager.savePlayer(currentPlayer);
                } else {
                    boolean knownLocation = false;

                    for (Player player : placementManager.getAllPlayers()) {
                        if (player.isBreakLocationValid(blockLocation)) {
                            knownLocation = true;
                            break;
                        }
                    }

                    if (knownLocation) {
                        event.setCancelled(true);
                        String message = String.format(settingManager.getNotFromPlayerPlacedMessage(), pureRestrictedBlock.toString());
                        event.getPlayer().sendMessage(message);
                    } else {
                        logger.info(event.getPlayer().getName() + " removed " + pureRestrictedBlock.toString() + "on unknown location.");
                    }
                }
            } else {
                for (Player player : placementManager.getAllPlayers()) {
                    if (player.isBreakLocationValid(blockLocation)) {
                        player.decreasePlacement(pureRestrictedBlock, blockLocation);
                        placementManager.savePlayer(player);
                        break;
                    }
                }
                logger.info(event.getPlayer().getName() + " removed " + pureRestrictedBlock.toString() + " with Override");
            }

        }
    }
}
