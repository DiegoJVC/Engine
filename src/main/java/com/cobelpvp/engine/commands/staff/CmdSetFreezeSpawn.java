package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.util.LocationUtil;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CmdSetFreezeSpawn {

    public static Location freezeSpawn;

    @Command(names = {"setfreezespawn"}, permission = "engine.freezespawn")
    public static void freezeLocationCommand(Player player) {
        Location location = player.getLocation();
        freezeSpawn = location;

        if (freezeSpawn == null) {
            Engine.getInstance().getConfig().set("FreezeSpawn", null);
        } else {
            Engine.getInstance().getConfig().set("FreezeSpawn", LocationUtil.serialize(freezeSpawn));
        }

        Engine.getInstance().saveConfig();
        player.sendMessage(ColorText.translate("&6Freeze spawn has been set"));
    }
}