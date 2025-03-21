package com.kingshiraishi.mcp_tphelper;

import com.kingshiraishi.mcp_tphelper.commands.CommandTPList;
import com.kingshiraishi.mcp_tphelper.commands.GUITPListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class TPHelper extends JavaPlugin{

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.getCommand("tplist").setExecutor(new CommandTPList(this));

        getServer().getConsoleSender().sendMessage(ChatColor.BLUE + "[KingShiraishi]: TP Helper is enabled.");
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[KingShiraishi]: TP Helper is disabled.");
    }

}
