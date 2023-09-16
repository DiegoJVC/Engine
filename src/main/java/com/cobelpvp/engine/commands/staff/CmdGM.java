package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class CmdGM {

    @Command(names = {"gamemode", "gm"}, permission = "engine.gamemode")
    public static void gameModeCommand(Player sender, @Param(name = "gamemode") GameMode gameMode) {
        sender.setGameMode(gameMode);
        sender.updateInventory();
        sender.sendMessage(ColorText.translate("&6Set your game mode to &c" + gameMode + "."));
    }
}
