package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class AddPermCommand {

    @Command(names = {"rank addperm", "rank addpermission"}, permission = "engine.rank.addperm")
    public static void RankAddPermissionCommand(CommandSender sender, @Param(name = "displayName") String displayName, @Param(name = "permission") String permission) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        if (rank.getPermissions().contains(permission.toLowerCase().trim())) {
            sender.sendMessage(ColorText.translate("&cAlready have this permission."));
            return;
        }

        for (Rank inherit : rank.getInherited()) {
            if (inherit.hasPermission(permission)) {
                sender.sendMessage(ColorText.translate("&cInherited rank already have this permission."));
                return;
            }
        }

        rank.getPermissions().add(permission);
        rank.save();
        rank.refresh();

        sender.sendMessage(ColorText.translate(displayName + "&6:&e added permission " + permission));
    }
}
