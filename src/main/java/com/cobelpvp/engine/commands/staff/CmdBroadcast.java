package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdBroadcast {

    @Command(names = {"broadcast", "bc"}, permission = "engine.broadcast")
    public static void broadcastCommand(CommandSender player, @Param(name = "message", wildcard = true) String message) {
        String staffName = player instanceof Player ? Profile.getProfileMap().get(((Player) player).getUniqueId()).getColoredUsername() : ChatColor.GREEN.toString() + ChatColor.STRIKETHROUGH + "SYSTEM";
        Bukkit.broadcastMessage(ColorText.translate("&d&o[&r" + staffName + "&d&o] " + message));
    }
}
