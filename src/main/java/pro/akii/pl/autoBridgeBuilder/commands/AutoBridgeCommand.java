package pro.akii.pl.autoBridgeBuilder.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pro.akii.pl.autoBridgeBuilder.AutoBridgeBuilder;
import pro.akii.pl.autoBridgeBuilder.gui.GUIManager;
import pro.akii.pl.autoBridgeBuilder.utils.ConfigUtils;

public class AutoBridgeCommand implements CommandExecutor {

    private final AutoBridgeBuilder plugin;

    public AutoBridgeCommand(AutoBridgeBuilder plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(plugin.getConfig().getString("messages.no_perms.only_player"));
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            GUIManager.openBlockSelectionMenu(player);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (!plugin.isOPOrHasPermission(player)) {
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no_perms.no_reload"));
                return false;
            }
            plugin.reloadConfig();
            player.sendMessage(ChatColor.GREEN + "Configuration has been reloaded.");
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("block")) {
            if (!plugin.isOPOrHasPermission(player)) {
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no_perms.no_set_block"));
                return false;
            }
            Material material = Material.getMaterial(args[1].toUpperCase());
            if (material != null) {
                ConfigUtils.setBridgeBlock(player, material);
                player.sendMessage(ChatColor.GREEN + plugin.getConfig().getString("messages.perms.set_block") + material.toString());
            } else {
                player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.null.invalid_block"));
            }
            return true;
        }
        return false;
    }
}
