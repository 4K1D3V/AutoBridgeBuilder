package pro.akii.pl.autoBridgeBuilder.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pro.akii.pl.autoBridgeBuilder.AutoBridgeBuilder;
import pro.akii.pl.autoBridgeBuilder.utils.ConfigUtils;

import java.util.ArrayList;
import java.util.List;

public class GUIManager {

    public static void openBlockSelectionMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + AutoBridgeBuilder.getInstance().getConfig().getString("gui.gui_title"));
        List<Material> allowedBlocks = getAllowedBlocksFromConfig(player);
        for (int i = 0; i < allowedBlocks.size(); i++) {
            ItemStack blockItem = new ItemStack(allowedBlocks.get(i), 1);
            ItemMeta meta = blockItem.getItemMeta();
            meta.setDisplayName(allowedBlocks.get(i).toString());
            blockItem.setItemMeta(meta);
            menu.setItem(i, blockItem);
        }
        if (AutoBridgeBuilder.getInstance().isOPOrHasPermission(player)) {
            addOPOnlyBlocks(menu);
        }

        player.openInventory(menu);
    }

    public static List<Material> getAllowedBlocksFromConfig(Player player) {
        List<Material> blocks = new ArrayList<>();
        List<String> blockNames = AutoBridgeBuilder.getInstance().getConfig().getStringList("allowed-blocks");

        for (String blockName : blockNames) {
            try {
                blocks.add(Material.valueOf(blockName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning("Invalid block in config: " + blockName);
            }
        }

        return blocks;
    }

    private static void addOPOnlyBlocks(Inventory menu) {
        List<Material> opBlocks = getOpBlocksFromConfig();
        for (int i = 0; i < opBlocks.size(); i++) {
            ItemStack blockItem = new ItemStack(opBlocks.get(i), 1);
            ItemMeta meta = blockItem.getItemMeta();
            meta.setDisplayName(opBlocks.get(i).toString() + " (OP Only)");
            blockItem.setItemMeta(meta);
            menu.addItem(blockItem);
        }
    }

    public static List<Material> getOpBlocksFromConfig() {
        List<Material> blocks = new ArrayList<>();
        List<String> blockNames = AutoBridgeBuilder.getInstance().getConfig().getStringList("op-blocks");

        for (String blockName : blockNames) {
            try {
                blocks.add(Material.valueOf(blockName.toUpperCase()));
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().warning(ChatColor.RED + "Invalid OP block in config: " + blockName);
            }
        }

        return blocks;
    }

    public static void handleBlockSelection(Player player, ItemStack selectedItem) {
        Material selectedBlock = selectedItem.getType();
        ConfigUtils.setBridgeBlock(player, selectedBlock);
        player.sendMessage(ChatColor.GREEN + "You have selected " + selectedBlock.toString() + " as your bridge block.");
    }
}
