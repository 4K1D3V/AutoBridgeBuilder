package pro.akii.pl.autoBridgeBuilder.utils;

import org.bukkit.entity.Player;

public class PermissionUtils {
    public static boolean hasPermission(Player player, String permission) {
        return player.hasPermission(permission) || player.isOp();
    }
}
