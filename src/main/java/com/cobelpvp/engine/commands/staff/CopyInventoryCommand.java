package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CopyInventoryCommand {

    @Command(names = {"cpinv", "cpfrom", "copyinv"}, permission = "engine.copyinv", description = "Apply a player's inventory to yours")
    public static void cpinv(Player sender, @Param(name = "target") Player player) {
        sender.getInventory().setContents(player.getInventory().getContents());
        sender.getInventory().setArmorContents(player.getInventory().getArmorContents());
        sender.setHealth(player.getHealth());
        sender.setFoodLevel(player.getFoodLevel());
        player.getActivePotionEffects().forEach(((Player) sender)::addPotionEffect);
        sender.sendMessage(player.getDisplayName() + ChatColor.GOLD + "'s inventory has been applied to you.");
    }

    @Command(names = {"cpto", "invto", "copyinvto", "cpinvto"}, permission = "engine.invto", description = "Apply your inventory to another player")
    public static void cpto(Player sender, @Param(name = "target") Player player) {
        player.getInventory().setContents(sender.getInventory().getContents());
        player.getInventory().setArmorContents(sender.getInventory().getArmorContents());
        player.setHealth(sender.getHealth());
        player.setFoodLevel(sender.getFoodLevel());
        sender.getActivePotionEffects().forEach(((Player) player)::addPotionEffect);
        sender.sendMessage((ChatColor.GOLD + "Your inventory has been applied to " + ChatColor.WHITE + player.getDisplayName() + ChatColor.GOLD + "."));
    }
}

