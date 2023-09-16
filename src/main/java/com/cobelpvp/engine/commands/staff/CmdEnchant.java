package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdEnchant {

    @Command(names = {"enchant"}, permission = "engine.enchant")
    public static void enchantCommand(Player sender, @Param(name = "enchantment") Enchantment enchant, @Param(name = "level") String exp) {
        ItemStack stack = sender.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            sender.sendMessage(ChatColor.RED + "You dont have valid item in your hand.");
            return;
        }

        if (!isStringInteger(exp)) {
            sender.sendMessage(ChatColor.RED + exp + " must be a integer.");
            return;
        }
        int level = Integer.parseInt(exp);
        if (level < 0) {
            sender.sendMessage(ChatColor.RED + "Please enter a valid level for this.");
            return;
        }

        if (level == 0) {
            if (stack.containsEnchantment(enchant)) {
                stack.removeEnchantment(enchant);
                sender.sendMessage(ColorText.translate("&eRemoved enchantment named &d" + enchant + " &efrom your hand successfully!"));
            } else {
                sender.sendMessage(ChatColor.RED + "No enchant.");
            }
        } else {
            stack.addUnsafeEnchantment(enchant, level);
            sender.sendMessage(ColorText.translate("&eEnchanted your hand with &d" + enchant + " lvl " + level + " &esuccessfully!"));
        }
    }

    public static boolean isStringInteger(final String string) {
        try {
            Integer.parseInt(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
