package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.util.Settings;
import org.bukkit.entity.Player;

public class CmdStore {

    @Command(names = {"store"})
    public static void storeCommand(Player sender) {
        sender.sendMessage(ColorText.translate("&6Store: &e" + Settings.networkStore));
    }
}
