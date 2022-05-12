package me.squeaky2137.scavengerhunt;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class configmanager {
    public static void loadConfig(FileConfiguration config) {
        commands = config.getStringList("commands");
    }
    public static List<String> commands;
}
