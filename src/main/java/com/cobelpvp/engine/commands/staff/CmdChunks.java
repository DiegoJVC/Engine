package com.cobelpvp.engine.commands.staff;

import java.util.Iterator;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class CmdChunks {

    @Command(names = {"chunks"},permission = "op")
    public static void chunks(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "Loaded chunks per world:");
        Iterator var1 = Bukkit.getWorlds().iterator();

        while(var1.hasNext()) {
            World world = (World)var1.next();
            sender.sendMessage(ChatColor.YELLOW + world.getName() + ": " + ChatColor.RED + world.getLoadedChunks().length);
        }

    }
}
