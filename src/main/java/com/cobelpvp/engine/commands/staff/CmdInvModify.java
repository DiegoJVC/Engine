package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.entity.Player;

public class CmdInvModify {

    @Command(names = {"inventorymodify", "invmodify"}, permission = "engine.invmodify")
    public static void invModifyCommand(Player player, @Param(name = "target") Player target) {
        player.openInventory(target.getInventory());
    }
}
