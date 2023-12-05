package com.cobelpvp.engine.punishment.commands.remove;

import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnBanIPCommand {

    @Command(names = {"unbanip", "ub-ip", "unban-ip", "ubip"}, permission = "engine.unbanip")
    public static void execute(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard = true, defaultValue = "Pardoned") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

        if (profile.getActivePunishmentByType(PunishmentType.BAN_IP) == null) {
            sender.sendMessage(ChatColor.RED + "This player is not currently banned.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getColoredUsername() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BAN_IP);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason);
        punishment.setRemoved(true);

        if (sender instanceof Player) {
            punishment.setRemovedBy(((Player) sender).getUniqueId());
        }

        profile.save();

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
    }
}
