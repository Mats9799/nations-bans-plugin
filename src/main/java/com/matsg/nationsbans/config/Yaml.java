package com.matsg.nationsbans.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;
import java.util.List;

public abstract class Yaml {

    protected Plugin plugin;
    protected File file;
    protected Configuration config;
    protected String resource;
    protected boolean save;

    public Yaml(Plugin plugin, String filepath, String resource, boolean save) {
        this.file = getNewFile(filepath, resource);
        this.plugin = plugin;
        this.resource = resource;
        this.save = save;

        createNewYaml(filepath, resource, save);

        reload();
    }

    public Yaml(Plugin plugin, String resource, boolean save) {
        this(plugin, plugin.getDataFolder().getPath(), resource, save);
    }

    public boolean canSave() {
        return save;
    }

    public String getResourceName() {
        return resource;
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public Configuration getConfigurationSection(String path) {
        return config.getSection(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public String getFilePath() {
        return file.getPath();
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public Object getObject(String path) {
        return config.get(path);
    }

    public InputStream getResource() {
        return plugin.getResourceAsStream(resource);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void reload() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeKey(String path) {
        config.set(path, null);
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    //Copies existing data in the resource as well as comments
    private void copyResource(InputStream resource, File file) {
        try {
            OutputStream out = new FileOutputStream(file);

            int length;
            byte[] buf = new byte[1024];

            while ((length = resource.read(buf)) > 0) {
                out.write(buf, 0, length);
            }

            out.close();
            resource.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Creates a new yaml from the resources in the given directory
    private void createNewYaml(String filepath, String resource, boolean save) {
        File file = getNewFile(filepath, resource);

        if (file == null) {
            return;
        }

        prepareFile(file, resource);
    }

    private File getNewFile(String filepath, String filename) {
        if (filename.length() == 0 || filename == null) {
            return null;
        }
        if (!filename.endsWith(".yml")) {
            filename.replace(filename.substring(filename.length() - 4, filename.length()), ".yml");
        }

        return new File(filepath, filename);
    }

    //Creates the yaml file in the directory if it does not exist yet
    private void prepareFile(File file, String resource) {
        if (file.exists()) {
            return;
        }

        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            if (resource.length() > 0 && resource != null) {
                copyResource(plugin.getResourceAsStream(resource), file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}