package com.matsg.nationsbans.command;

import net.md_5.bungee.api.CommandSender;

public interface CommandBase {

    void onCommand(CommandSender sender, String[] args);

    String getName();

    String getPermissionNode();

    boolean isPlayerOnly();
}