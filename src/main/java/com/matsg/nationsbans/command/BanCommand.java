package com.matsg.nationsbans.command;

import com.matsg.nationsbans.Ban;
import com.matsg.nationsbans.sql.SQLManager;
import com.matsg.nationsbans.util.DateUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class BanCommand extends Command {

    public BanCommand(Plugin plugin) {
        super(plugin, "ban", "nationsbans.ban", false);
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
            sender.sendMessage(new ComponentBuilder("§cYou cannot ban yourself.").create());
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(new ComponentBuilder("§cSpecify the length of the ban.").create());
            return;
        }

        Timestamp pardonDate = DateUtils.getBanTimestamp(args[1]);

        if (pardonDate == null) {
            sender.sendMessage(new ComponentBuilder("§cError while parsing the ban length input.").create());
            return;
        }

        if (args.length == 2) {
            sender.sendMessage(new ComponentBuilder("§cClarify the reason of the ban.").create());
            return;
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 2; i < args.length; i ++) {
            builder.append(args[i] + " ");
        }

        String reason = builder.toString();

        SQLManager.getInstance().registerBan(new Ban(player.getUniqueId(), player.getName(), sender.getName(), reason, pardonDate));

        player.disconnect(new ComponentBuilder("You have been banned!\n\nReason: " + reason
                + "\nBanned until: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(pardonDate)).create());

        sender.sendMessage(new ComponentBuilder("Player " + player.getName() + " has been banned until " +
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(pardonDate) + ".").create());
    }
}