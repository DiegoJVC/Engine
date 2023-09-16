package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdTop {

    @Command(names = {"top", "teleporttohighposition"}, permission = "engine.top", async = true, description = "Teleport to highest location.")
    public static void top(Player player) {
        player.teleport(new Location(player.getWorld(), player.getLocation().getBlockX() + 0.5, player.getWorld().getHighestBlockYAt(player.getLocation()), player.getLocation().getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
        player.sendMessage(ChatColor.GOLD + ("Teleported to the highest position."));
    }
}
