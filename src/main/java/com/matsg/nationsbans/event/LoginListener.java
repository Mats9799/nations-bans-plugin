package com.matsg.nationsbans.event;

import com.matsg.nationsbans.Ban;
import com.matsg.nationsbans.sql.SQLManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.text.SimpleDateFormat;

public class LoginListener extends AbstractListener {

    public LoginListener(Plugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerLogin(LoginEvent event) {
        Ban ban = SQLManager.getInstance().getBan(event.getConnection().getUniqueId());

        if (ban == null) {
            return;
        }

        event.setCancelled(true);
        event.setCancelReason(new ComponentBuilder("You are banned!\n\nBanned until: " +
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(ban.getPardonDate())).create());
    }
}