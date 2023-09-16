package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdFeed {

    @Command(names = {"feed"}, permission = "engine.feed")
    public static void feedCommand(Player sender) {
        if (sender.getFoodLevel() >= 20) {
            sender.sendMessage(ChatColor.DARK_RED + "You already have maximum food level");
            return;
        }

        sender.setFoodLevel(20);
        sender.setFireTicks(0);

        sender.sendMessage(ColorText.translate("&6Food level have been regained."));
    }
}
