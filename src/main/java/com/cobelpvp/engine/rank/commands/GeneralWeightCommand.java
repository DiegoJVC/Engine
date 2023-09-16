package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class GeneralWeightCommand {

    @Command(names = {"rank generalweight"}, permission = "engine.rank.generalweight")
    public static void RankGeneralWeightCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "weight") String weight) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        try {
            Integer.parseInt(weight);
        } catch (Exception e) {
            sender.sendMessage(ColorText.translate("&cNumber is not valid."));
            return;
        }

        rank.setGeneralWeight(Integer.parseInt(weight));
        rank.save();
        rank.refresh();

        sender.sendMessage(ColorText.translate(displayName + "&6:&e changed general weight to " + Integer.parseInt(weight)));
    }
}
