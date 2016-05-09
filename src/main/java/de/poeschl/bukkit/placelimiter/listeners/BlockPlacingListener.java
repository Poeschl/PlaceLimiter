package de.poeschl.bukkit.placelimiter.listeners;

import de.poeschl.bukkit.placelimiter.managers.PermissionManager;
import de.poeschl.bukkit.placelimiter.managers.PlacementManager;
import de.poeschl.bukkit.placelimiter.managers.SettingManager;
import de.poeschl.bukkit.placelimiter.models.Block;
import de.poeschl.bukkit.placelimiter.models.Player;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockPlacingListener implements Listener {

    private JavaPlugin plugin;
    private SettingManager settingManager;
    private PlacementManager placementManager;

    public BlockPlacingListener(JavaPlugin plugin, SettingManager settingManager, PlacementManager placementManager) {
        this.plugin = plugin;
        this.settingManager = settingManager;
        this.placementManager = placementManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block currentBlock = new Block(event.getBlock().getType(), event.getBlock().getData());

        if (settingManager.isMaterialLimited(currentBlock) && !event.getPlayer().hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)) {
            Player currentPlayer = placementManager.getPlayer(event.getPlayer().getUniqueId());
            int alreadyPlaced = currentPlayer.getPlacementOfMaterial(currentBlock);
            int limit = settingManager.getMaterialLimit(currentBlock);

            if (alreadyPlaced < limit) {
                currentPlayer.increasePlacement(currentBlock, event.getBlock().getLocation());
                placementManager.savePlayer(currentPlayer);
            } else {
                event.setCancelled(true);
                String message = String.format(settingManager.getLimitPlaceReachedMessage(), currentBlock.getMaterial().name() + ":" + currentBlock.getData());
                event.getPlayer().sendMessage(message);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block currentBlock = new Block(event.getBlock().getType(), event.getBlock().getData());
        Location blockLocation = event.getBlock().getLocation();

        if (settingManager.isMaterialLimited(currentBlock)) {

            if (!event.getPlayer().hasPermission(PermissionManager.PERMISSION_KEY_LIMIT_OVERRIDE)) {
                Player currentPlayer = placementManager.getPlayer(event.getPlayer().getUniqueId());

                if (currentPlayer.isBreakLocationValid(blockLocation)) {
                    currentPlayer.decreasePlacement(currentBlock, blockLocation);
                    placementManager.savePlayer(currentPlayer);
                } else {
                    event.setCancelled(true);
                    String message = String.format(settingManager.getNotFromPlayerPlacedMessage(), currentBlock.getMaterial().name() + ":" + currentBlock.getData());
                    event.getPlayer().sendMessage(message);
                }
            } else {
                for (Player player : placementManager.getAllPlayers()) {
                    if (player.isBreakLocationValid(blockLocation)) {
                        player.decreasePlacement(currentBlock, blockLocation);
                        break;
                    }
                }
            }

        }
    }
}