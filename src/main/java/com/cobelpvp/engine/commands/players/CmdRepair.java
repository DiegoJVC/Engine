package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.HashSet;

public class CmdRepair {

    @Command(names = {"repair", "fix"}, permission = "engine.repair")
    public static void repair(Player sender) {
        if (sender.hasPermission("engine.repair.all")) {
            sender.sendMessage(ChatColor.RED + "/repair <hand|all>");
        } else if (sender.hasPermission("engine.repair")) {
            sender.sendMessage(ChatColor.RED + "/repair <hand>");
        }
    }

    @Command(names = {"repair hand", "fix hand"}, permission = "engine.repair", description = "Repair the item you're currently holding")
    public static void repair_hand(Player sender) {
        ItemStack item = sender.getItemInHand();
        if (item == null || item.getType().equals(Material.AIR)) {
            sender.sendMessage(ChatColor.RED + "You must be holding an item.");
            return;
        }
        if (item.getDurability() == 0) {
            sender.sendMessage(ChatColor.RED + "That " + ChatColor.GOLD + ItemUtils.getName(item) + ChatColor.RED + " already has max durability.");
            return;
        }
        item.setDurability((short) 0);
        sender.sendMessage(ChatColor.GREEN + "Your " + ChatColor.GOLD + ItemUtils.getName(item) + ChatColor.GREEN + " has been repaired.");
    }

    @Command(names = {"repair all", "fix all"}, permission = "engine.repair.all", description = "Repair all items in your inventory")
    public static void repair_all(Player sender) {
        HashSet<ItemStack> toRepair = new HashSet<ItemStack>();
        PlayerInventory playerInventory = sender.getInventory();
        toRepair.addAll(Arrays.asList(playerInventory.getContents()));
        toRepair.addAll(Arrays.asList(playerInventory.getArmorContents()));
        for (ItemStack item : toRepair) {
            if (item == null || item.getType() == Material.AIR || !CmdRepair.isRepairble(item) || item.getDurability() == 0)
                continue;
            item.setDurability((short) 0);
        }
        sender.sendMessage(ChatColor.GREEN + "All " + ChatColor.GOLD + "items" + ChatColor.GREEN + " in your inventory has been repaired.");
    }

    private static boolean isRepairble(ItemStack item) {
        return item != null && (item.getType() == Material.WOOD_AXE || item.getType() == Material.WOOD_HOE || item.getType() == Material.WOOD_SWORD || item.getType() == Material.WOOD_SPADE || item.getType() == Material.WOOD_PICKAXE || item.getType() == Material.STONE_AXE || item.getType() == Material.STONE_HOE || item.getType() == Material.STONE_SWORD || item.getType() == Material.STONE_SPADE || item.getType() == Material.STONE_PICKAXE || item.getType() == Material.GOLD_AXE || item.getType() == Material.GOLD_HOE || item.getType() == Material.GOLD_SWORD || item.getType() == Material.GOLD_SPADE || item.getType() == Material.GOLD_PICKAXE || item.getType() == Material.IRON_AXE || item.getType() == Material.IRON_HOE || item.getType() == Material.IRON_SWORD || item.getType() == Material.IRON_SPADE || item.getType() == Material.IRON_PICKAXE || item.getType() == Material.DIAMOND_AXE || item.getType() == Material.DIAMOND_HOE || item.getType() == Material.DIAMOND_SWORD || item.getType() == Material.DIAMOND_SPADE || item.getType() == Material.DIAMOND_PICKAXE || item.getType() == Material.DIAMOND_BOOTS || item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.DIAMOND_HELMET || item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.IRON_BOOTS || item.getType() == Material.IRON_CHESTPLATE || item.getType() == Material.IRON_HELMET || item.getType() == Material.IRON_LEGGINGS || item.getType() == Material.GOLD_BOOTS || item.getType() == Material.GOLD_CHESTPLATE || item.getType() == Material.GOLD_HELMET || item.getType() == Material.GOLD_LEGGINGS || item.getType() == Material.DIAMOND_BOOTS || item.getType() == Material.DIAMOND_CHESTPLATE || item.getType() == Material.DIAMOND_HELMET || item.getType() == Material.DIAMOND_LEGGINGS || item.getType() == Material.CHAINMAIL_HELMET || item.getType() == Material.CHAINMAIL_CHESTPLATE || item.getType() == Material.CHAINMAIL_LEGGINGS || item.getType() == Material.CHAINMAIL_BOOTS || item.getType() == Material.LEATHER_HELMET || item.getType() == Material.LEATHER_CHESTPLATE || item.getType() == Material.LEATHER_LEGGINGS || item.getType() == Material.LEATHER_BOOTS || item.getType() == Material.FISHING_ROD || item.getType() == Material.CARROT_STICK || item.getType() == Material.SHEARS || item.getType() == Material.FLINT_AND_STEEL || item.getType() == Material.BOW);
    }
}
