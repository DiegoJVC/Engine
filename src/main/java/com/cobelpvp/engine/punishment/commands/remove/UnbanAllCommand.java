package com.cobelpvp.engine.punishment.commands.remove;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.checks.checks.ClearPunishmentsRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnbanAllCommand {
    @Command(names = {"unban-all"}, permission = "op")
    public static void RemovePunishCommand(CommandSender sender) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.DARK_RED + "This command can only be executed on console.");
            return;
        }
        for (Profile profile : Profile.getProfileMap().values()) {
            profile.getPunishments().clear();
            profile.save();
            Engine.getInstance().getRedis().sendPacket(new ClearPunishmentsRedisCheck(profile.getId()));
            System.out.printf("Removing all the punishments (Bans-Mutes-Kicks-Warnings)");
        }
    }
}
