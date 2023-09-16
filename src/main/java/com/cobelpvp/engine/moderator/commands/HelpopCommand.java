package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.qchecks.checks.StaffRequestRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.util.Cooldowns;
import com.cobelpvp.atheneum.util.TimeUtils;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HelpopCommand {

    @Command(names = {"request","helpop"})
    public static void requestCommand(CommandSender sender, @Param(name = "reason", wildcard = true) String reason) {
        if (Cooldowns.isOnCooldown("helpop", (Player) sender)) {
            sender.sendMessage(ChatColor.RED + "You cannot use this to quickly.");
            return;
        }

        Profile senderProfile = Profile.getByUsername(((Player) sender).getName());

        assert senderProfile != null;
        Engine.getInstance().getRedis().sendPacket(new StaffRequestRedisCheck(senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName(), reason, Bukkit.getServerId(), Bukkit.getServerName()));

        sender.sendMessage(ChatColor.GREEN + "Your request have been sent to all staff available!");

        Cooldowns.addCooldown("helpop", (Player) sender, TimeUtils.parseTime("2m"));
    }
}
