package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.profile.Profile;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BungeeTeleportCommand {

    @Command(names = {"bungeeteleport","bungeetpto","bungeetp"}, permission = "engine.bungeetp", async = true)
    public static void bungeetp(Player sender, @Param(name = "target") UUID uuid) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        if (profile == null) {
            sender.sendMessage(ChatColor.RED + "No player with the name " + ChatColor.YELLOW + uuid + ChatColor.RED + "found.");
            return;
        }
        Player target = Engine.getInstance().getServer().getPlayer(uuid);
        if(target != null) {
            sender.teleport(target);
        }
        teleport(sender.getPlayer(), uuid, Bukkit.getPlayer(uuid).getServer());
    }

    private static void teleport(Player player, UUID target, Server server) {
        Engine.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Engine.getInstance(), () -> {
            if(!player.getServer().getServerName().equals(Bukkit.getPlayer(target).getServer().getServerName())) {
                player.sendMessage(ChatColor.GOLD + "Sending you to " + ChatColor.WHITE + Bukkit.getPlayer(target).getServer().getServerName() + ChatColor.GOLD + ".");
            }
            sendToServer(player,server.getServerId());
        }, 20L);
    }

    private static void sendToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        try {
            player.sendPluginMessage(Engine.getInstance(),"BungeeCord", out.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
