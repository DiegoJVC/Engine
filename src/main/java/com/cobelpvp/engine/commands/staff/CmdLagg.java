package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.*;

import java.util.List;

public class CmdLagg {

    @Command(names = {"lagg"}, permission = "engine.lagg", hidden = true)
    public static void laggCommand(Player sender) {
        for (World world : Bukkit.getWorlds()) {
            List<Entity> entitiesList = world.getEntities();
            for (Entity entity : entitiesList) {
                if (entity instanceof Item || entity instanceof Animals || entity instanceof Monster) {
                    entity.remove();
                }
            }
        }

        sender.sendMessage(ChatColor.GOLD + "All the entities have been removed!");
    }
}
