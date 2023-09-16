package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class RemovePermCommand {

    @Command(names = {"rank removeperm", "rank removepermission"}, permission = "engine.rank.removeperm")
    public static void RankRemovePermCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "permission") String permission) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        if (!rank.getPermissions().contains(permission.toLowerCase().trim())) {
            sender.sendMessage(ColorText.translate("&cThis rank doesn't have '" + permission + "' permission."));
            return;
        }

        rank.getPermissions().remove(permission);
        rank.save();
        rank.refresh();

        sender.sendMessage(ColorText.translate(rank.getFormattedName() + "&6:&e removed permission " + permission));
    }
}
