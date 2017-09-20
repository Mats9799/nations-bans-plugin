package com.matsg.nationsbans;

import com.matsg.nationsbans.command.BanCommand;
import com.matsg.nationsbans.command.KickCommand;
import com.matsg.nationsbans.command.PardonCommand;
import com.matsg.nationsbans.event.ChatListener;
import com.matsg.nationsbans.event.LoginListener;
import net.md_5.bungee.api.plugin.Plugin;

public class NationsBans extends Plugin {

    private static Plugin plugin;

    public void onEnable() {
        plugin = this;

        SettingsManager.getInstance().loadConfigs();

        new BanCommand(plugin);
        new KickCommand(plugin);
        new PardonCommand(plugin);

        new ChatListener(plugin);
        new LoginListener(plugin);
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}