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

        List<? extends Player> otherPlayers = Bukkit.getOnlinePlayers().stream()
                .filter(p -> !p.equals(player)).toList();

        if (otherPlayers.isEmpty()) {
            player.sendMessage(plugin.getConfig().getString("messages.no-other-players"));
            return true;
        }

        String guiTitle = plugin.getConfig().getString("gui.title");
        int size = ((otherPlayers.size() + 8) / 9) * 9;

        Inventory gui = Bukkit.createInventory(null, size, guiTitle);

        for (Player target : otherPlayers) {
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();
            meta.setOwningPlayer(target);
            meta.setDisplayName("§a" + target.getName());
            head.setItemMeta(meta);
            gui.addItem(head);
        }

        player.openInventory(gui);

        return true;
    }
}
