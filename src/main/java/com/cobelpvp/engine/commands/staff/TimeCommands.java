package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TimeCommands {

    @Command(names = {"sun", "day"}, permission = "engine.time", async = true, description = "Set time of the server to day")
    public static void sun(Player player) {
        player.sendMessage(ChatColor.GOLD + "Sun has begun!");
        player.getLocation().getWorld().setTime(1000);
        player.getLocation().getWorld().setStorm(false);
        player.getLocation().getWorld().setThundering(false);
    }

    @Command(names = {"night"}, permission = "engine.time", async = true, description = "Set time of the server to night")
    public static void night(Player player) {
        player.sendMessage(ChatColor.GOLD + "Moon was turn up!");
        player.getLocation().getWorld().setTime(12000);
    }

    @Command(names = "weather clear", permission = "engine.time", async = true, description = "Set weather to off")
    public static void weatherclear(Player player) {
        player.sendMessage(ChatColor.GOLD + "Weather was cleared!");
        player.getLocation().getWorld().setStorm(false);
        player.getLocation().getWorld().setThundering(false);
    }

    @Command(names = "weather thunder", permission = "engine.time", async = true, description = "Set weather thunders to off")
    public static void weatherthunder(Player player) {
        if (!player.getLocation().getWorld().isThundering()) {
            player.sendMessage(ChatColor.GOLD + "Thunders are flashing!");
            player.getLocation().getWorld().setThundering(true);
        } else {
            player.sendMessage(ChatColor.GOLD + "Thunders was cleared!");
            player.getLocation().getWorld().setThundering(false);
        }

    }

    @Command(names = "weather storm", permission = "engine.time", async = true, description = "Set weather storm to off")
    public static void weatherstorm(Player player) {
        if (!player.getLocation().getWorld().hasStorm()) {
            player.getLocation().getWorld().setStorm(true);
            player.sendMessage(ChatColor.GOLD + "Storm is coming on!");
        } else {
            player.sendMessage(ChatColor.GOLD + "Storm weather was cleared!");
            player.getLocation().getWorld().setThundering(false);
        }
    }

}
