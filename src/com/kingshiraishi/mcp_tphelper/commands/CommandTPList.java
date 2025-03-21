package com.kingshiraishi.mcp_tphelper.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
            sender.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("messages.player-only"));
            return true;
        }

        List<? extends Player> otherPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(player)).toList();

        if (otherPlayers.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("messages.teleport-failure--no-other-players"));
            return true;
        }

        String guiTitle = plugin.getConfig().getString("gui.title");
        int size = ((otherPlayers.size() + 8 + 9) / 9) * 9;

        Inventory gui = Bukkit.createInventory(null, size, guiTitle);

        ItemStack pearl = new ItemStack(Material.ENDER_PEARL);
        ItemMeta pearlMeta = pearl.getItemMeta();
        pearlMeta.setDisplayName(ChatColor.LIGHT_PURPLE + plugin.getConfig().getString("gui.description--tp-random"));
        pearl.setItemMeta(pearlMeta);

        gui.setItem(0, pearl); // row1, col0

        int startSlot = 9; // row2, col0
        int index = 0;
        for (Player target : otherPlayers) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(target);
            meta.setDisplayName("§a" + target.getName());
            head.setItemMeta(meta);

            if (startSlot + index >= size) break;
            gui.setItem(startSlot + index, head);
            index++;
        }

        player.openInventory(gui);

        return true;
    }
}
