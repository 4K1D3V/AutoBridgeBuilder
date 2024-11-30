package pro.akii.pl.autoBridgeBuilder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pro.akii.pl.autoBridgeBuilder.commands.AutoBridgeCommand;
import pro.akii.pl.autoBridgeBuilder.listeners.PlayerMoveListener;
import pro.akii.pl.autoBridgeBuilder.utils.ConfigUtils;
import pro.akii.pl.autoBridgeBuilder.utils.PermissionUtils;

public class AutoBridgeBuilder extends JavaPlugin {
    private static AutoBridgeBuilder instance;

    public static AutoBridgeBuilder getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.getCommand("autobridge").setExecutor(new AutoBridgeCommand(this));
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        logPluginInfo();
    }

    public void logPluginInfo() {
        getLogger().info("AutoBridgeBuilder plugin is enabled.");
        getLogger().info("Bridge Block: " + ConfigUtils.getBridgeBlock(null));
    }

    public boolean isOPOrHasPermission(Player player) {
        return player.isOp() || PermissionUtils.hasPermission(player, "autobridge.admin");
    }
}
