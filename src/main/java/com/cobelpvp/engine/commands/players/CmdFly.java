package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.entity.Player;

public class CmdFly {

    @Command(names = {"fly", "flight"}, permission = "engine.fly")
    public static void flyCommand(Player sender) {
        boolean toggle = !sender.getAllowFlight();
        sender.setAllowFlight(toggle);
        sender.setFlying(toggle);
        sender.sendMessage(ColorText.translate("&6Your flight mode have been updated."));
    }
}
