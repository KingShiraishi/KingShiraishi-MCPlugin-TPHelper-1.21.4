package com.kingshiraishi.mcp_tphelper.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CommandTPList implements CommandExecutor {
    private final JavaPlugin plugin;
    public CommandTPList(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getConfig().getString("messages.player-only"));
            return true;
        }

        String guiTitle = plugin.getConfig().getString("gui.title");
//        TODO: Get other players count
//        int size = ((otherPlayers + 8) / 9) * 9;
        int size = 27;

        Inventory gui = Bukkit.createInventory(null, size, guiTitle);
        player.openInventory(gui);


        return true;
    }
}
