package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdCommands {

    @Command(names = "commands")
    public static void reclaimCommand(Player player) {

        if (player.hasPermission("engine.commands.default") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Default commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.RED + "NONE");
            return;
        }

        if (player.hasPermission("engine.commands.basic") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Basic commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.RED + "NONE");
            return;
        }

        if (player.hasPermission("engine.commands.archon") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Archon commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.RED + "NONE");
            return;
        }

        if (player.hasPermission("engine.commands.ancient") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Ancient commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.RED + "NONE");
            return;

        }

        if (player.hasPermission("engine.commands.legend") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Legend commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./follow");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            return;
        }

        if (player.hasPermission("engine.commands.inmortal") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Inmortal commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./rename");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./follow");
            player.sendMessage(ChatColor.DARK_GREEN + "./tournament start");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            return;
        }

        if (player.hasPermission("engine.commands.cobel") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Cobel commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./rename");
            player.sendMessage(ChatColor.DARK_GREEN + "./repair");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./follow");
            player.sendMessage(ChatColor.DARK_GREEN + "./tournament start");
            player.sendMessage(ChatColor.DARK_GREEN + "./tournament forcestart");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            return;
        }

        if (player.hasPermission("engine.commands.streamer") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Streamer commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./rename");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            return;
        }

        if (player.hasPermission("engine.commands.media") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Media commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./rename");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            return;
        }

        if (player.hasPermission("engine.commands.youtube") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD+ "------[" + ChatColor.AQUA +"Showing all the Youtube commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./rename");
            player.sendMessage(ChatColor.DARK_GREEN + "./repair");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./follow");
            player.sendMessage(ChatColor.DARK_GREEN + "./tournament start");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            return;
        }

        if (player.hasPermission("engine.commands.famous") && !player.isOp()) {
            player.sendMessage(ChatColor.GOLD + "------[" + ChatColor.AQUA +"Showing all the Famous commands" + ChatColor.GOLD+ "]------");
            player.sendMessage(ChatColor.BLUE + "*HCFactions");
            player.sendMessage(ChatColor.DARK_GREEN + "./reclaim");
            player.sendMessage(ChatColor.DARK_GREEN + "./bottle");
            player.sendMessage(ChatColor.DARK_GREEN + "./lives");
            player.sendMessage(ChatColor.DARK_GREEN + "./rename");
            player.sendMessage(ChatColor.DARK_GREEN + "./repair");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
            player.sendMessage(ChatColor.BLUE + "*Practice");
            player.sendMessage(ChatColor.DARK_GREEN + "./follow");
            player.sendMessage(ChatColor.DARK_GREEN + "./tournament start");
            player.sendMessage(ChatColor.DARK_GREEN + "./tournament forcestart");
            player.sendMessage(ChatColor.DARK_GREEN + "./vipannounce");
        }
    }
}
