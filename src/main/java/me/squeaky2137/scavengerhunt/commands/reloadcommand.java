package me.squeaky2137.scavengerhunt.commands;

import me.squeaky2137.scavengerhunt.Main;
import me.squeaky2137.scavengerhunt.configmanager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class reloadcommand implements CommandExecutor{
    private final Main main;

    public reloadcommand(Main main) {
        this.main = main;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.isOp() || player.hasPermission("hunt.reload")) {
                main.reloadConfig();
                FileConfiguration config = main.getConfig();
                configmanager.loadConfig(config);
                player.sendMessage(ChatColor.RED + "Reloaded config successfully");
            } else {
                player.sendMessage(ChatColor.RED + "Invalid Permissions");
            }
        } else {
            main.reloadConfig();
            FileConfiguration config = main.getConfig();
            configmanager.loadConfig(config);
            main.getLogger().info("Reloaded Config!");
        }
        return true;
    }
}
