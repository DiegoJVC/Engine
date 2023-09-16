package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CmdMore {

    @Command(names = {"more", "stack"}, permission = "engine.more")
    public static void moreCommand(Player sender, @Param(name = "amount") int amount) {
        if (amount <= -1) {
            sender.sendMessage(ChatColor.RED + "Please enter valid amount for this.");
            return;
        }

        ItemStack stack = sender.getItemInHand();
        stack.setAmount(amount);
        sender.updateInventory();
        sender.sendMessage(ColorText.translate("&6Stacked your item in hand to " + amount));
    }
}
