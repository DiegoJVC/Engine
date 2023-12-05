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
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnMuteCommand {

    @Command(names = {"unmute"}, permission = "engine.unmute")
    public static void unMuteCommand(Player sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard=true, defaultValue = "Pardoned") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, please try again...");
            return;
        }

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) == null) {
            sender.sendMessage(ChatColor.RED + "Could not unmute this player, because this is not muted.");
            return;
        }

        Profile senderProfile = Profile.getByUsername(sender.getName());

        assert senderProfile != null;
        String staffName = sender instanceof Player ? senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = profile.getActivePunishmentByType(PunishmentType.MUTE);
        punishment.setRemovedAt(System.currentTimeMillis());
        punishment.setRemovedReason(reason);
        punishment.setRemoved(true);

        if (sender instanceof Player) {
            punishment.setRemovedBy(sender.getUniqueId());
        }

        profile.save();

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
    }
}
