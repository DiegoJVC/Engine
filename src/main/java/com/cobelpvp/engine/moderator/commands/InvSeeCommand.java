package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.menu.InvseeMenu;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.entity.Player;

public class InvSeeCommand {

    @Command(names = {"invsee"}, permission = "moderator.invsee")
    public static void invSeeCommand(Player sender, @Param(name = "target") Player target) {
        new InvseeMenu(target).openMenu(sender);
        sender.sendMessage(ColorText.translate("&eOpening inventory of &9" + target.getName() + "&e!"));
    }
}
