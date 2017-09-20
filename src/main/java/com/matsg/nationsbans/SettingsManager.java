package com.matsg.nationsbans;

import com.matsg.nationsbans.config.SQLConfig;
import com.matsg.nationsbans.config.Yaml;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {

    private static SettingsManager instance;
    private Plugin plugin;
    private List<Yaml> yamls;

    private SettingsManager(Plugin plugin) {
        this.plugin = plugin;
        this.yamls = new ArrayList<Yaml>();

        setup();
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager(NationsBans.getPlugin());
        }
        return instance;
    }

    public SQLConfig getSQLConfig() {
        try {
            return (SQLConfig) getYaml("sql.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Yaml getYaml(String resource) throws FileNotFoundException {
        for (Yaml yaml : yamls) {
            if (yaml.getResourceName().equalsIgnoreCase(resource)) {
                return yaml;
            }
        }
        throw new FileNotFoundException("No such config file named " + resource);
    }

    public List<Yaml> getYamls() {
        return yamls;
    }

    public void loadConfig(String resource) {
        try {
            if (getYaml(resource) == null) {
                return;
            }

            getYaml(resource).reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadConfigs() {
        yamls.clear();
        setup();
    }

    public void saveConfig(String resource) {
        try {
            if (getYaml(resource) == null) {
                return;
            }

            Yaml yaml = getYaml(resource);

            if (!yaml.canSave()) {
                return;
            }

            yaml.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfigs() {
        for (Yaml yaml : yamls) {
            if (yaml.canSave()) {
                yaml.save();
            }
        }
    }

    private void setup() {
        yamls.add(new SQLConfig(plugin));
    }
}