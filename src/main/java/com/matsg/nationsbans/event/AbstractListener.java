package com.matsg.nationsbans.event;

import net.md_5.bungee.api.plugin.Event;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class AbstractListener implements Listener {

    protected Plugin plugin;

    public AbstractListener(Plugin plugin) {
        this.plugin = plugin;

        enable(this);
    }

    protected void callEvent(Event event) {
        plugin.getProxy().getPluginManager().callEvent(event);
    }

    protected void disable(Listener listener) {
        plugin.getProxy().getPluginManager().unregisterListener(listener);
    }

    protected void enable(Listener listener) {
        plugin.getProxy().getPluginManager().registerListener(plugin, listener);
    }
}