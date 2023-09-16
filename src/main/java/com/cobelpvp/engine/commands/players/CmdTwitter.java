package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.entity.Player;

public class CmdTwitter {

    @Command(names = {"twitter", "tw", "twt"})
    public static void twitterCommand(Player sender) {
        sender.sendMessage(ColorText.translate("&6Twitter: &e" + Settings.twitter));
    }
}
