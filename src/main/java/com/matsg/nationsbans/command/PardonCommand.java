package com.matsg.nationsbans.command;

import com.matsg.nationsbans.Ban;
import com.matsg.nationsbans.sql.SQLManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

public class PardonCommand extends Command {

    public PardonCommand(Plugin plugin) {
        super(plugin, "pardon", "nationsbans.pardon", false);
    }

    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("§cSpecify a player name or UUID.").create());
            return;
        }

        Ban ban = args[0].matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
                ? SQLManager.getInstance().getBan(UUID.fromString(args[0])) : SQLManager.getInstance().getBan(args[0]);

        if (ban == null) {
            sender.sendMessage(new ComponentBuilder("§cThis player is currently not banned.").create());
            return;
        }

        SQLManager.getInstance().removeBan(ban);

        sender.sendMessage(new ComponentBuilder("You have lifted " + ban.getPlayerName() + "'s ban.").create());
    }
}