package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.command.CommandSender;

public class CmdTeamSpeak {

    @Command(names = {"teamspeak", "ts"})
    public static void teamspeakCommand(CommandSender sender) {
        sender.sendMessage(ColorText.translate("&6TeamSpeak: &e" + Settings.teamSpeak));
    }
}
