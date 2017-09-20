package com.matsg.nationsbans.config;

import net.md_5.bungee.api.plugin.Plugin;

public class SQLConfig extends Yaml {

    public SQLConfig(Plugin plugin) {
        super(plugin, "sql.yml", false);
    }

    public String getDatabaseIP() {
        return getString("sql.ip");
    }

    public String getDatabaseName() {
        return getString("sql.database");
    }

    public String getPassword() {
        return getString("sql.password");
    }

    public String getUsername() {
        return getString("sql.username");
    }
}