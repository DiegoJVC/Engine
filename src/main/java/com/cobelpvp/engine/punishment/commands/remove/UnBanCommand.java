package com.cobelpvp.engine.punishment.commands.remove;

import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnBanCommand {

    @Command(names = {"unban", "ub"}, permission = "engine.unban")
    public static void execute(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard = true, defaultValue = "Pardoned") String reason) {
        OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(uuid);
        Profile profile = Profile.getByUuid(target.getUniqueId());

        if (profile.getActivePunishmentByType(PunishmentType.BAN) == null) {
            sender.sendMessage(ChatColor.RED + "This player is not currently banned.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getColoredUsername() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason);
        punishment.setRemoved(true);

        if (sender instanceof Player) {
            punishment.setRemovedBy(((Player) sender).getUniqueId());
        }

        profile.save();

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getId()));
    }
}
