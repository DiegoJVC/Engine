package com.cobelpvp.engine.punishment.commands;

import com.cobelpvp.engine.checks.checks.ClearPunishmentsRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class WipePunishCommand {

    @Command(names = {"wipepunish"}, permission = "op")
    public static void wipePunishCommand(CommandSender sender, @Param(name = "player") UUID uuid) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, please try again...");
            return;
        }

        profile.getPunishments().clear();
        profile.save();

        Engine.getInstance().getRedis().sendPacket(new ClearPunishmentsRedisCheck(profile.getId()));

        sender.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Cleared!");
    }
}
