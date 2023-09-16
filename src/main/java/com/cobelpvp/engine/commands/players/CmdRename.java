package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CmdRename {

    @Command(names = {"rename"}, permission = "engine.rename")
    public static void renameCommand(Player player, @Param(name = "name" , wildcard = true) String name) {
        ItemStack stack = player.getItemInHand();

        if (stack == null || stack.getType().equals(Material.AIR)) {
            player.sendMessage(ChatColor.RED + "You are not holding any item!");
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ColorText.translate(name));
        stack.setItemMeta(meta);
        player.sendMessage(ColorText.translate("&6Successfully renamed your item in hand to &r" + name + "&e."));
    }
}
