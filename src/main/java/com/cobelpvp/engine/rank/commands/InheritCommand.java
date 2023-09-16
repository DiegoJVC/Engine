package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class InheritCommand {

    @Command(names = {"rank inherit"}, permission = "engine.rank.inherit")
    public static void RankInheritCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "inherit") String inherit) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        Rank inheritRank = Rank.getRankByDisplayName(inherit);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        if (inheritRank == null) {
            sender.sendMessage(ColorText.translate("&cThis inherit rank doesn't exists."));
            return;
        }

        if (!rank.rankCanInherit(inheritRank)) {
            sender.sendMessage(ColorText.translate("&c" + rank.getDisplayName() + " can inherit with " + inheritRank.getDisplayName()));
            return;
        }

        rank.getInherited().add(inheritRank);
        rank.save();
        rank.refresh();

        sender.sendMessage(ColorText.translate(rank.getFormattedName() + "&6:&e inherited " + inheritRank.getFormattedName() + "&e with this"));
    }
}
