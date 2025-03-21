package com.kingshiraishi.mcp_tphelper.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;

public class GUITPListener implements Listener {
    private final JavaPlugin plugin;
    public GUITPListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String guiTitle = plugin.getConfig().getString("gui.title");
        if (!event.getView().getTitle().equals(guiTitle)) return;

        // set true to disable item stealing
        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();

        // if player clicks Enter Pearl then execute `/tp @r`
        if (clicked.getType() == Material.ENDER_PEARL) {
            List<? extends Player> candidates = Bukkit.getOnlinePlayers().stream()
                    .filter(p -> !p.equals(player)) // other players
                    .toList();

            if (candidates.isEmpty()) {
                player.sendMessage(ChatColor.YELLOW + plugin.getConfig().getString("messages.teleport-failure--no-other-players"));
            } else {
                Player target = candidates.get(new Random().nextInt(candidates.size()));
                target.teleport(player); // teleport the target player to the executor

                String toPlayerMsg = plugin.getConfig().getString("messages.teleport-success--to-you");
                player.sendMessage(ChatColor.GREEN + String.format(toPlayerMsg, target.getName()));

                String toTargetMsg = plugin.getConfig().getString("messages.teleport-success--you-re-tpd");
                target.sendMessage(ChatColor.DARK_GREEN + String.format(toTargetMsg, target.getName()));
            }

            player.closeInventory();
            return;
        }

        if (clicked == null || clicked.getType() != Material.PLAYER_HEAD) return;

        SkullMeta meta = (SkullMeta) clicked.getItemMeta();
        if (meta == null || meta.getOwningPlayer() == null) return;

        OfflinePlayer target = meta.getOwningPlayer();

        if (target.isOnline()) {
            player.teleport(target.getPlayer());

            // success
            String successMsg = plugin.getConfig().getString("messages.teleport-success");
            player.sendMessage(ChatColor.GREEN + String.format(successMsg, target.getName()));
        } else {
            // the target player is offline
            String offlineMsg = plugin.getConfig().getString("messages.teleport-failure--target-offline");
            player.sendMessage(ChatColor.YELLOW + String.format(offlineMsg, target.getName()));
        }

        player.closeInventory();
    }
}