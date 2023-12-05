package com.cobelpvp.engine.profile.permissions;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.entity.Player;

public class PermsCommand {
    @Command(names = {"perms"}, permission = "engine.perms")
    public static void execute(Player sender) {
        sender.sendMessage(ColorText.translate("&7/perms add (name) (permission)"));
        sender.sendMessage(ColorText.translate("&7/perms remove (name) (permission)"));
    }
}
