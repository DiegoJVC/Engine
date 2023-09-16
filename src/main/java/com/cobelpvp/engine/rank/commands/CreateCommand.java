package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class CreateCommand {

    @Command(names = {"rank create"}, permission = "engine.rank.create")
    public static void rankCreateCommand(CommandSender sender, @Param(name = "name") String name) {
        Rank rank = Rank.getRankByDisplayName(name);

        if (rank != null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        rank = new Rank(name);
        rank.save();

        sender.sendMessage(ColorText.translate(name + "&6:&e created"));
    }
}
