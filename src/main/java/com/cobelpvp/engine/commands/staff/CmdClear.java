package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.entity.Player;

public class CmdClear {

    @Command(names = {"clear", "cl"}, permission = "engine.clear")
    public static void clearCommand(Player sender, @Param(name = "target", defaultValue = "self") Player target) {
        target.getInventory().clear();
        target.getInventory().setArmorContents(null);

        if (target.equals(sender)) {
            sender.sendMessage(ColorText.translate("&6You cleared your inventory successful!"));
            return;
        }

        Profile profile = Profile.getByUsername(target.getName());

        sender.sendMessage(ColorText.translate("&6You cleared inventory of " + profile.getActiveGrant().getRank().getGameColor() + target.getName()));
    }
}
