package me.squeaky2137.scavengerhunt;

import me.squeaky2137.scavengerhunt.commands.giveblock;
import me.squeaky2137.scavengerhunt.commands.reloadcommand;
import me.squeaky2137.scavengerhunt.events.headmanagement;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    FileConfiguration config = getConfig();

    public static Main getPlugin() {
        return plugin;
    }

    private static Main plugin;

    public static DataManager data;

    @Override
    public void onEnable() {
        plugin = this;
        configmanager.loadConfig(config);
        this.saveDefaultConfig();
        data = new DataManager(this);
        getLogger().info("Scavenger Hunt has been enabled!!");
        getServer().getPluginCommand("huntreload").setExecutor(new reloadcommand(this));
        getServer().getPluginCommand("gethead").setExecutor(new giveblock(this));
        getServer().getPluginManager().registerEvents(new headmanagement(), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
