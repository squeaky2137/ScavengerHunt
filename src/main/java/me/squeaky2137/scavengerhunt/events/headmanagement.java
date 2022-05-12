package me.squeaky2137.scavengerhunt.events;


import me.squeaky2137.scavengerhunt.Main;
import me.squeaky2137.scavengerhunt.configmanager;
import org.bukkit.*;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class headmanagement implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().hasItemMeta()) {
            ItemStack item = e.getItemInHand();
            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            if (data.has(new NamespacedKey(Main.getPlugin(), "hunt"), PersistentDataType.STRING)) {
                Location location = e.getBlockPlaced().getLocation();
                if(Main.data.getConfig().contains("TotalHeads")) {
                    Integer amount = Main.data.getConfig().getInt("TotalHeads") + 1;
                    Main.data.getConfig().set("TotalHeads", amount);
                } else {
                    Main.data.getConfig().set("TotalHeads", 1);
                }

                Main.data.getConfig().set("Heads." + location.hashCode(), 0);
                Main.data.saveConfig();
                e.getPlayer().sendMessage(ChatColor.GREEN + "Placed Head");
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Location blockloc = e.getBlock().getLocation();
        if(Main.data.getConfig().contains("Heads." + blockloc.hashCode())) {
            if(!e.getPlayer().hasPermission("scavengerhunt.breakhead")) {
                e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to break easter eggs!");
                e.setCancelled(true);
            } else {
                Main.data.getConfig().set("Heads." + blockloc.hashCode(), null);
                Integer amount = Main.data.getConfig().getInt("TotalHeads") - 1;
                Main.data.getConfig().set("TotalHeads", amount);
                Main.data.saveConfig();
                e.getPlayer().sendMessage(ChatColor.RED + "Removed Head");
            }
        }
    }

    @EventHandler
    public static void onBlockClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(e.getClickedBlock().getType() == Material.PLAYER_HEAD) {
                Location blockloc = e.getClickedBlock().getLocation();
                if(Main.data.getConfig().contains("Heads." + blockloc.hashCode())) {
                    if(Main.data.getConfig().contains("Players." + player.getUniqueId() + ".headlocations")) {
                        List<Integer> HeadLoc = Main.data.getConfig().getIntegerList("Players." + player.getUniqueId() + ".headlocations");
                        if(HeadLoc.contains(blockloc.hashCode())) {
                            player.sendMessage(ChatColor.RED + "Already collected this egg!");
                            return;
                        } else {
                            HeadLoc.add(blockloc.hashCode());
                            Main.data.getConfig().set("Players." + player.getUniqueId() + ".headlocations", HeadLoc);
                            if(Main.data.getConfig().contains("Players." + player.getUniqueId() + ".HeadsFound")) {
                                int numfound = Main.data.getConfig().getInt("Players." + player.getUniqueId() + ".HeadsFound") + 1;
                                Main.data.getConfig().set("Players." + player.getUniqueId() + ".HeadsFound", numfound);
                            } else {
                                Main.data.getConfig().set("Players." + player.getUniqueId() + ".HeadsFound", 1);

                            }
                            Main.data.saveConfig();
                        }
                    } else {
                        List<Integer> headloc = new ArrayList<>();
                        headloc.add(blockloc.hashCode());


                        Main.data.getConfig().set("Players." + player.getUniqueId() + ".headlocations", headloc);
                        if(Main.data.getConfig().contains("Players." + player.getUniqueId() + ".HeadsFound")) {
                            int numfound = Main.data.getConfig().getInt("Players." + player.getUniqueId() + ".HeadsFound") + 1;
                            Main.data.getConfig().set("Players." + player.getUniqueId() + ".HeadsFound", numfound);
                        } else {
                            Main.data.getConfig().set("Players." + player.getUniqueId() + ".HeadsFound", 1);
                        }
                        Main.data.saveConfig();
                    }

                    player.sendMessage(ChatColor.AQUA + "You have found " + Main.data.getConfig().getInt("Players." + player.getUniqueId() + ".HeadsFound") +"/"+Main.data.getConfig().getInt("TotalHeads") + " Hidden Eggs!");

                    if(Main.data.getConfig().getInt("Players." + player.getUniqueId() + ".HeadsFound") >= Main.data.getConfig().getInt("TotalHeads")) {
                        try {
                            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                            for(String command : configmanager.commands) {
                                Bukkit.dispatchCommand(console, command.replace("%player%", player.getName()));
                            }



                        } catch(Exception err) {
                            Main.getPlugin().getLogger().log(Level.SEVERE,"Invalid item for prize in scavengerhunt config", err);
                        }
                        player.sendMessage(ChatColor.GOLD + "You have found all of the hidden Eggs!");
                    }

                }
            }
        }
    }
}
