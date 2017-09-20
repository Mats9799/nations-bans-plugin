package com.matsg.nationsbans.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class Command extends net.md_5.bungee.api.plugin.Command implements CommandBase {

    protected boolean playerOnly;
    protected Plugin plugin;
    protected String name, permissionNode;

    public Command(Plugin plugin, String name, String permissionNode, boolean playerOnly) {
        super(name);
        this.name = name;
        this.permissionNode = permissionNode;
        this.playerOnly = playerOnly;
        this.plugin = plugin;

        plugin.getProxy().getPluginManager().registerCommand(plugin, this);
    }

    public String getName() {
        return name;
    }

    public String getPermissionNode() {
        return permissionNode;
    }

    public boolean isPlayerOnly() {
        return playerOnly;
    }

    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer) && playerOnly) {
            sender.sendMessage(new ComponentBuilder("§cOnly players may execute this command.").create());
            return;
        }

        if (permissionNode != null && permissionNode.length() > 0 && !sender.hasPermission(permissionNode)) {
            sender.sendMessage(new ComponentBuilder("§cYou have no permisson to execute this command.").create());
            return;
        }

        onCommand(sender, args);
    }
}