package pro.akii.pl.autoBridgeBuilder.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pro.akii.pl.autoBridgeBuilder.AutoBridgeBuilder;
import pro.akii.pl.autoBridgeBuilder.utils.ConfigUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerMoveListener implements Listener {

    private static final long BUILD_DELAY_MS = 500;
    private Map<UUID, Long> lastBuildTimes = new HashMap<>();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("autobridge.use") && (shouldPlaceBlock(player) || AutoBridgeBuilder.getInstance().isOPOrHasPermission(player))) {
            long lastBuildTime = getLastBuildTime(player);
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastBuildTime >= BUILD_DELAY_MS) {
                placeBridgeBlock(player);
                updateLastBuildTime(player, currentTime);
            }
        }
    }

    private void placeBridgeBlock(Player player) {
        Location location = player.getLocation().clone();
        location.setY(location.getY() - 1);
        Block block = location.getBlock();

        if (block.getType() == Material.AIR) {
            block.setType(ConfigUtils.getBridgeBlock(player));
            playBridgeBuildingEffects(player);
        }
    }

    private void playBridgeBuildingEffects(Player player) {
        boolean effectsEnabled = AutoBridgeBuilder.getInstance().getConfig().getBoolean("bridge-effects.enable-particles");
        boolean soundEnabled = AutoBridgeBuilder.getInstance().getConfig().getBoolean("bridge-effects.enable-sound");

        if (effectsEnabled) {
            player.getWorld().spawnParticle(Particle.valueOf(AutoBridgeBuilder.getInstance().getConfig().getString("bridge-effects.particle-type")), player.getLocation(), 10);
        }

        if (soundEnabled) {
            player.playSound(player.getLocation(), Sound.valueOf(AutoBridgeBuilder.getInstance().getConfig().getString("bridge-effects.sound-type")), 1.0F, 1.0F);
        }
    }

    private long getLastBuildTime(Player player) {
        return lastBuildTimes.getOrDefault(player.getUniqueId(), 0L);
    }

    private void updateLastBuildTime(Player player, long currentTime) {
        lastBuildTimes.put(player.getUniqueId(), currentTime);
    }

    private boolean shouldPlaceBlock(Player player) {
        return player.isOnGround() && !player.isInsideVehicle();
    }
}
