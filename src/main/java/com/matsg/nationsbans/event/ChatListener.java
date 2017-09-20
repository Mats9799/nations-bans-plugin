package com.matsg.nationsbans.event;

import com.matsg.nationsbans.command.BanCommand;
import com.matsg.nationsbans.command.Command;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ChatListener extends AbstractListener {

    private Command[] commands;

    public ChatListener(Plugin plugin) {
        super(plugin);
        this.commands = new Command[] { new BanCommand(plugin) };
    }

    @EventHandler
    public void onCommandSend(ChatEvent event) {
        if (!event.isCommand()) return;

        for (Command command : commands) {
            if (event.getMessage().startsWith("/" + command.getName())) {
                command.execute((CommandSender) event.getSender(), event.getMessage().split(" ").length > 1
                        ? event.getMessage().replace("/" + command.getName() + " ", "").split(" ") : new String[0]);
                event.setCancelled(true);
                break;
            }
        }
    }
}