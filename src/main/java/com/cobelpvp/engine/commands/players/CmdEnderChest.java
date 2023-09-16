package com.cobelpvp.engine.commands.players;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.entity.Player;

public class CmdEnderChest {

    @Command(names = {"enderchest", "ec"}, permission = "engine.enderchest")
    public static void enderChestCommand(Player player) {
        player.openInventory(player.getEnderChest());
    }

    @Command(names = {"enderchestothers", "ecothers"}, permission = "engine.enderchestothers")
    public static void enderChestOthersCommand(Player player, @Param(name = "target") Player target) {
        player.openInventory(target.getEnderChest());
        player.sendMessage(ColorText.translate("&eOpening ender chest of " + target.getName() + "..."));
    }
}
