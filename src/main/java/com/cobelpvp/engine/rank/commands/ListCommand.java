package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.engine.EngineConstants;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import java.util.ArrayList;
import java.util.List;

public class ListCommand {

    @Command(names = {"rank list"}, permission = "engine.rank.list")
    public static void RankListCommand(CommandSender sender) {
        List<Rank> ranks = new ArrayList<>(Rank.getRanksMap().values());
        ranks.sort((o1, o2) -> o2.getDisplayWeight() - o1.getDisplayWeight());

        sender.sendMessage(EngineConstants.getCenter(ChatColor.AQUA.toString() + ranks.size() + " Ranks"));
        for (Rank rank : ranks) {
            sender.sendMessage(ColorText.translate(rank.getFormattedName() + " &3> &d" + rank.getDisplayWeight() + " weight"));
        }
    }
}
