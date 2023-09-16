package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.qchecks.checks.StaffReportRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.util.Cooldowns;
import com.cobelpvp.atheneum.util.TimeUtils;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ReportCommand {

    @Command(names = {"report"})
    public static void reportCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard = true) String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not load this profile, because this doesn't exists.");
            return;
        }

        if (Cooldowns.isOnCooldown("report", (Player) sender)) {
            sender.sendMessage(ChatColor.RED + "You cannot use this to quickly.");
            return;
        }

        profile.setReports(profile.getReports() + 1);

        Profile senderProfile = Profile.getByUsername(((Player) sender).getName());

        Engine.getInstance().getRedis().sendPacket(new StaffReportRedisCheck(senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName(), profile.getColoredUsername(), reason, Bukkit.getServerId(), Bukkit.getServerName(), profile.getReports()));

        sender.sendMessage(ChatColor.GREEN + "Your report have been sent to all staff available!");

        Cooldowns.addCooldown("report", (Player) sender, TimeUtils.parseTime("2m"));
    }
}
