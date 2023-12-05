package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.entity.Player;

public class CraftCommand {

    @Command(names = {"craft"}, permission = "engine.craft", description = "Opens a crafting table")
    public static void rename(Player sender) {
        sender.openWorkbench(sender.getLocation(), true);
    }
}

