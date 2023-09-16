package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.util.Settings;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.command.CommandSender;

public class CmdDiscord {

    @Command(names = {"discord", "dc"})
    public static void discordCommand(CommandSender sender) {
        sender.sendMessage(ColorText.translate("&6Discord: &e" + Settings.discord));
    }
}
