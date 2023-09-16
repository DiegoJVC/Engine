package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.checks.checks.VIPAnnouncementCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.util.Cooldowns;
import com.cobelpvp.atheneum.util.TimeUtils;
import com.cobelpvp.engine.Engine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdVipAnnounce {

    @Command(names = {"announcement", "announce", "vipannouncement", "vipannounce"}, permission = "engine.vipannounce")
    public static void vipAnnouncementCommand(Player player) {
        Profile profile = Profile.getByUsername(player.getName());
        assert profile != null;
        assert profile.isLoaded();
        String playerName = profile.getActiveRank().getGameColor() + player.getName();

        if (Bukkit.getServerName().contains("Hub")) {
            player.sendMessage(ChatColor.RED + "You cant perform this command in the hub.");
            return;
        }

        if (Cooldowns.isOnCooldown("vipannouncement", player)) {
            player.sendMessage(ChatColor.RED + "Please wait...");
            return;
        }

        Engine.getInstance().getRedis().sendPacket(new VIPAnnouncementCheck(playerName, Bukkit.getServerName()));
        Cooldowns.addCooldown("vipannouncement", player, TimeUtils.parseTime("1m"));
    }
}
