package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class HighRankCommand {

    @Command(names = {"rank highrank"}, permission = "op", hidden = true)
    public static void RankHighRankCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "boolean") boolean status) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        rank.setHighRank(status);
        rank.save();

        sender.sendMessage(ColorText.translate(rank.getFormattedName() + "&6:&e high rank toggle to " + status));
    }
}
