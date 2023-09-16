package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdTppos {

    @Command(names = {"teleportposition", "teleportpos", "tppos"}, permission = "engine.teleportposition")
    public static void teleportPositionCommand(Player sender, @Param(name = "x") int x, @Param(name = "y") int y, @Param(name = "z") int z) {
        Location location = new Location(sender.getWorld(), x, y, z);
        sender.teleport(location);
        sender.sendMessage(ColorText.translate("&6Teleported to &c" + x + "&6x, &c" + y + "&6y, &c" + z + "&6z in &c" + sender.getWorld().getName() + " &6world!"));
    }
}
