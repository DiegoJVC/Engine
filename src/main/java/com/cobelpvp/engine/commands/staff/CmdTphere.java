package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.entity.Player;

public class CmdTphere {

    @Command(names = {"teleporthere", "tphere"}, permission = "engine.teleporthere")
    public static void teleportHereCommand(Player sender, @Param(name = "target") Player target) {
        target.teleport(sender.getLocation());

        Profile targetProfile = Profile.getByUsername(target.getName());

        Profile senderProfile = Profile.getByUsername(sender.getName());

        assert senderProfile != null;
        sender.sendMessage(ColorText.translate("&eTeleported " + targetProfile.getActiveGrant().getRank().getGameColor() + target.getName() + " &eto your location!"));
    }
}
