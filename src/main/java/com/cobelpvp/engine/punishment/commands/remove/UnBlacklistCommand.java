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

public class UnBlacklistCommand {

    @Command(names = {"unblacklist"}, permission = "op")
    public static void unblacklistCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard =true, defaultValue = "Pardoned") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, please try again...");
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.BLACKLIST) == null) {
            sender.sendMessage(ChatColor.RED + "This player is not blacklisted.");
            return;
        }

        Profile senderProfile = Profile.getByUsername(sender.getName());

        assert senderProfile != null;

        String staffName = sender instanceof Player ? senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.BLACKLIST);
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
