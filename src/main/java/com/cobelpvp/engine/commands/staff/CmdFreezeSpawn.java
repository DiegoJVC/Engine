package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdFreezeSpawn {

    @Command(names = {"freezespawn"}, permission = "engine.freezespawn")
    public static void freezeSpawnCommand(Player player) {
        Location location = CmdSetFreezeSpawn.freezeSpawn;

        if (CmdSetFreezeSpawn.freezeSpawn == null) {
            player.sendMessage(ChatColor.DARK_RED + "Freeze spawn is not set!");
            return;
        }

        player.teleport(location);
        player.sendMessage(ChatColor.GOLD + "Teleported to freeze spawn!");
    }
}
