package com.cobelpvp.engine.commands.staff;

import java.util.HashSet;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.EntityUtils;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CmdSpawner {
    public CmdSpawner() {
    }

    @Command(
            names = {"spawner"},
            permission = "engine.spawner",
            description = "Change a spawner's type"
    )
    public static void spawner(Player sender, @Param(name = "mob") String mob) {
        EntityType type = EntityUtils.parse(mob);
        if (type != null && type.isAlive()) {
            Block block = sender.getTargetBlock((HashSet)null, 5);
            if (block != null && block.getState() instanceof CreatureSpawner) {
                CreatureSpawner spawner = (CreatureSpawner)block.getState();
                spawner.setSpawnedType(type);
                spawner.update();
                sender.sendMessage(ChatColor.GOLD + "This spawner now spawns " + ChatColor.WHITE + EntityUtils.getName(type) + ChatColor.GOLD + ".");
            } else {
                sender.sendMessage(ChatColor.RED + "You aren't looking at a mob spawner.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "No mob with the name " + mob + " found.");
        }

    }
}
