package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.entity.Player;

public class CmdTp {

    @Command(names = {"teleport", "tp"}, permission = "engine.teleport")
    public static void teleportCommand(Player sender, @Param(name = "target") Player target) {
        sender.teleport(target.getLocation());
        Profile targetProfile = Profile.getByUsername(target.getName());

        String targetName = targetProfile.getActiveRank().getGameColor() + target.getName();
        sender.sendMessage(ColorText.translate("&6Teleported to " + targetName));
    }
}
