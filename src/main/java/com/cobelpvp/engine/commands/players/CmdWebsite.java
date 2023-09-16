package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.entity.Player;

public class CmdWebsite {

    @Command(names = {"website"})
    public static void websiteCommand(Player sender) {
        sender.sendMessage(ColorText.translate("&6Website: &e" + Settings.networkWebsite));
    }
}
