package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdSetSpawn {

    @Command(names = {"setspawn"}, permission = "engine.setspawn")
    public static void setSpawnCommand(Player sender) {
        Location location = sender.getLocation();

        sender.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getYaw(), location.getPitch());

        sender.sendMessage(ColorText.translate("&6Spawn point has been updated."));
    }
}
