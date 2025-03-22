package com.kingshiraishi.mcp_tphelper.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtil {

    private static JavaPlugin plugin;

    public static void init(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    private static final Map<String, ChatColor> COLOR_MAP = Map.ofEntries(
        Map.entry("black", ChatColor.BLACK),
        Map.entry("dark_blue", ChatColor.DARK_BLUE),
        Map.entry("dark_green", ChatColor.DARK_GREEN),
        Map.entry("dark_aqua", ChatColor.DARK_AQUA),
        Map.entry("dark_red", ChatColor.DARK_RED),
        Map.entry("dark_purple", ChatColor.DARK_PURPLE),
        Map.entry("gold", ChatColor.GOLD),
        Map.entry("gray", ChatColor.GRAY),
        Map.entry("dark_gray", ChatColor.DARK_GRAY),
        Map.entry("blue", ChatColor.BLUE),
        Map.entry("green", ChatColor.GREEN),
        Map.entry("aqua", ChatColor.AQUA),
        Map.entry("red", ChatColor.RED),
        Map.entry("light_purple", ChatColor.LIGHT_PURPLE),
        Map.entry("yellow", ChatColor.YELLOW),
        Map.entry("white", ChatColor.WHITE)
    );

    private static String applyColorPlaceholders(String msg) {
        Matcher matcher = Pattern.compile("\\{\\$(\\w+)}").matcher(msg);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String key = matcher.group(1).toLowerCase();
            ChatColor color = COLOR_MAP.getOrDefault(key, ChatColor.WHITE);
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(color.toString()));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static void sendDynamicMessage(Player player, String message) {
        if (message == null) {
            message = "[Empty message]";
        }

        message = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(message);
    }

    public static void sendStaticMessage(Player target, String key, Object... formatArgs) {
        String locale = target.getLocale(); // e.g. "en_us", "ja_jp"
        String lang = locale.startsWith("en") ? "en" : "ja"; // 今回言語設定がされていない場合のデフォルトは日本語にしました。

        ConfigurationSection section = plugin.getConfig().getConfigurationSection(key);
        String message = null;

        if (section != null) {
            message = section.getString(lang, section.getString("ja"));
        }

        if (message == null) {
            message = "[Missing message: " + key + "]";
        }

        if (formatArgs.length > 0) {
            message = String.format(message, formatArgs);
        }

        message = applyColorPlaceholders(message);
        sendDynamicMessage(target, message);
    }

    public static void sendStaticMessage(CommandSender sender, String key, Object... formatArgs) {
        String lang = "ja";

        if (sender instanceof Player player) {
            String locale = player.getLocale();
            lang = locale.startsWith("en") ? "en" : "ja";
        }

        ConfigurationSection section = plugin.getConfig().getConfigurationSection("messages." + key);
        String message = null;

        if (section != null) {
            message = section.getString(lang, section.getString("ja"));
        }

        if (message == null) {
            message = "[Missing message: " + key + "]";
        }

        if (formatArgs.length > 0) {
            message = String.format(message, formatArgs);
        }

        message = applyColorPlaceholders(message);
        sender.sendMessage(message);
    }


}
