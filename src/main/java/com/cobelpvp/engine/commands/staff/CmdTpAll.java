package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdTpAll {

    @Command(names = {"teleportall", "tpall", "tp*"}, permission = "engine.tpall")
    public static void teleportAllCommand(Player player) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.teleport(player);
        }

        player.sendMessage(ColorText.translate("&6Teleported all players to your location!"));
    }
}
