package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class CmdTpworld {

    @Command(names = {"teleportworld", "tpworld"}, permission = "engine.teleportworld")
    public static void teleportWorldCommand(Player player, @Param(name = "name") String name) {
        World world = Bukkit.getWorld(name);

        if (world == null) {
            player.sendMessage(ChatColor.RED + "This world doesn't exists.");
            return;
        }

        Location location = new Location(world, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        player.teleport(location);
        player.sendMessage(ColorText.translate("&6Teleported to &c" + name + " &6in the locations &c" + player.getLocation().getX() + "&6, &c" + player.getLocation().getY() + "&6, &c" + player.getLocation().getZ()));
    }
}
