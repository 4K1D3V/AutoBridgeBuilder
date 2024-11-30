package pro.akii.pl.autoBridgeBuilder.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import pro.akii.pl.autoBridgeBuilder.AutoBridgeBuilder;

public class ConfigUtils {

    public static Material getBridgeBlock(Player player) {
        String blockName = AutoBridgeBuilder.getInstance().getConfig().getString("bridge-block");
        try {
            return Material.valueOf(blockName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Material.PACKED_ICE;
        }
    }

    public static void setBridgeBlock(Player player, Material material) {
        AutoBridgeBuilder.getInstance().getConfig().set("bridge-block", material.name());
        AutoBridgeBuilder.getInstance().saveConfig();
    }
}
