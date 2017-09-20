package com.matsg.nationsbans.command;

import com.matsg.nationsbans.Ban;
import com.matsg.nationsbans.sql.SQLManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public class KickCommand extends Command {

    public KickCommand(Plugin plugin) {
        super(plugin, "kick", "nationsbans.kick", false);
    }

    public void onCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("§cSpecify a player name or UUID.").create());
            return;
        }

        ProxiedPlayer player = args[0].matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")
                ? plugin.getProxy().getPlayer(UUID.fromString(args[0])) : plugin.getProxy().getPlayer(args[0]);

        if (player == null) {
            sender.sendMessage(new ComponentBuilder("§cPlayer not found.").create());
            return;
        }

        if (sender instanceof ProxiedPlayer && (ProxiedPlayer) sender == player) {
            sender.sendMessage(new ComponentBuilder("§cYou cannot kick yourself.").create());
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(new ComponentBuilder("§cClarify the reason of the kick.").create());
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 1; i < args.length; i ++) {
            builder.append(args[i] + " ");
        }

        String reason = builder.toString();
        Timestamp pardonDate = new Timestamp(Calendar.getInstance().getTime().getTime());

        SQLManager.getInstance().registerBan(new Ban(player.getUniqueId(), player.getName(), sender.getName(), reason, pardonDate));

        player.disconnect(new ComponentBuilder("You have been kicked!\n\nReason: " + reason).create());
        sender.sendMessage(new ComponentBuilder("Player " + player.getName() + " has been kicked.").create());
    }
}