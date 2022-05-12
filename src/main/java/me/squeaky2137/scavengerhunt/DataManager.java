package me.squeaky2137.scavengerhunt;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {
    private final Main main;
    private FileConfiguration dataConfig = null;
    private File configFile = null;

    public DataManager(Main main) {
        this.main = main;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if(this.configFile == null)
            this.configFile = new File(this.main.getDataFolder(), "data.yml");

        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.main.getResource("data.yml");
        if(defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();
        return this.dataConfig;
    }

    public void saveConfig() {
        if(this.dataConfig == null || this.configFile == null)
            return;
        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            main.getLogger().log(Level.SEVERE, "Couldnt save config to " + this.configFile, e);
        }
    }

    public void saveDefaultConfig() {
        configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("ScavengerHunt").getDataFolder(), "data.yml");

        if(!configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch(IOException e) {
                main.getLogger().log(Level.SEVERE, "Could not create data file", e);
            }
        }
    }
}
