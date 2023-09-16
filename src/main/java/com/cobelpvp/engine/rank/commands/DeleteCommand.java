package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class DeleteCommand {

    @Command(names = {"rank delete"}, permission = "engine.rank.delete")
    public static void RankDeleteCommand(CommandSender sender, @Param(name = "displayName") String displayName) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        rank.deleteRank();

        sender.sendMessage(ColorText.translate(displayName + "&6: &edeleted"));
    }
}
