package me.squeaky2137.scavengerhunt.commands;

import me.squeaky2137.scavengerhunt.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class giveblock implements CommandExecutor {
    private final Main main;

    public giveblock(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("hunt.gethead")) {
                if(player.getInventory().getItemInMainHand().getType() == Material.PLAYER_HEAD) {
                     ItemStack head = player.getInventory().getItemInMainHand();
                     SkullMeta headmeta = (SkullMeta) head.getItemMeta();
                    PersistentDataContainer dataContainer = headmeta.getPersistentDataContainer();
                    dataContainer.set(new NamespacedKey(Main.getPlugin(), "hunt"), PersistentDataType.STRING, "true");
                    head.setItemMeta(headmeta);
                    return true;
                }
                player.sendMessage(ChatColor.RED + "You must be holding a player head!");
            } else {
                player.sendMessage(ChatColor.RED +"Invalid perms");
            }
        } else {
            main.getLogger().info("Only Players can run this command");
        }
        return true;
    }


}
