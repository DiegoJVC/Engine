package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdMedia {

    @Command(names = {"media", "youtube", "famous"})
    public static void helpCommand(Player player) {
        player.sendMessage(ChatColor.GOLD + "----[" + ChatColor.LIGHT_PURPLE + "MediaTeam Requirements" + ChatColor.GOLD +"]----");
        player.sendMessage(ChatColor.RED + "[Media]: " + ChatColor.GREEN +"1 Video + 200 views" + ChatColor.RED + "(100+ Subs)");
        player.sendMessage(ChatColor.RED + "[Streamer]: " + ChatColor.GREEN +"20 Viewers (media)");
        player.sendMessage(ChatColor.RED + "[Youtube]: " + ChatColor.GREEN +"1 Video + 500 views" + ChatColor.RED + "(500+ Subs)");
        player.sendMessage(ChatColor.RED + "[Famous]: " + ChatColor.GREEN +"1 Video + 1k views" + ChatColor.RED + "(1.5k+ Subs)");
    }
}
