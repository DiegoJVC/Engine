package com.cobelpvp.engine.commands.staff;

import java.lang.reflect.Field;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import net.minecraft.server.v1_7_R4.PlayerList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;

public class CmdSetSlots {
    private static Field maxPlayerField = null;

    public CmdSetSlots() {
    }

    @Command(
            names = {"setslots", "setmaxslots", "setservercap", "ssc"},
            permission = "engine.setslots",
            description = "Set the max slots"
    )
    public static void setslots(CommandSender sender, @Param(name = "slots") int slots) {
        if (slots < 0) {
            sender.sendMessage(ChatColor.RED + "The number of slots must be greater or equal to zero.");
        } else {
            set(slots);
            sender.sendMessage(ChatColor.GOLD + "Slots set to " + ChatColor.WHITE + slots + ChatColor.GOLD + ".");
        }

    }

    private static void set(int slots) {
        try {
            maxPlayerField.set(((CraftServer)Bukkit.getServer()).getHandle(), slots);
        } catch (IllegalAccessException var2) {
            var2.printStackTrace();
        }

        save();
    }

    private static void save() {
        Engine.getInstance().getConfig().set("slots", Bukkit.getMaxPlayers());
        Engine.getInstance().saveConfig();
    }

    public static void load() {
        if (Engine.getInstance().getConfig().contains("slots")) {
            set(Engine.getInstance().getConfig().getInt("slots"));
        } else {
            Engine.getInstance().getConfig().set("slots", Bukkit.getMaxPlayers());
        }

    }

    static {
        try {
            (maxPlayerField = PlayerList.class.getDeclaredField("maxPlayers")).setAccessible(true);
        } catch (NoSuchFieldException var1) {
            var1.printStackTrace();
        }

    }
}
