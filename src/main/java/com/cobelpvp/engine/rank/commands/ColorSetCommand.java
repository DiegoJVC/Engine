package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class ColorSetCommand {

    @Command(names = {"rank color"}, permission = "engine.rank.color")
    public static void rankColorCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "color") String color) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        if (!color.startsWith("&")) {
            sender.sendMessage(ColorText.translate("&cPlease enter color codes '&'."));
            return;
        }

        rank.setGameColor(color);
        rank.save();

        sender.sendMessage(ColorText.translate(displayName + "&6:&e changed color to " + color));
    }
}
