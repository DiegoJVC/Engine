package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class PrefixCommand {

    @Command(names = {"rank prefix"}, permission = "engine.rank.prefix")
    public static void RankPrefixCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "gamePrefix", wildcard = true) String gamePrefix) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        rank.setGamePrefix(gamePrefix);
        rank.save();

        sender.sendMessage(ColorText.translate(rank.getFormattedName() + "&6:&e changed prefix to &r" + gamePrefix));
    }
}
