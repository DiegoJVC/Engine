package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.entity.Player;

public class CmdHelp {

    @Command(names = {"help", "?"})
    public static void helpCommand(Player player) {
        player.sendMessage(ColorText.translate("&6Help: &e" + Settings.teamSpeak + " | " + Settings.discord));
    }
}
