package com.cobelpvp.engine.punishment.commands.tempcreate;

import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.PunishmentsAlertRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.engine.util.Duration;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TempMuteCommand {

    @Command(names = {"tempmute"}, permission = "engine.tempmute")
    public static void muteCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "duration") String duration, @Param(name = "reason", wildcard =true, defaultValue = "Breaking Rules") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        Duration time = Duration.fromString(duration);
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, because this doesn't exists.");
            return;
        }

        if (sender instanceof Player) {
            if (profile.getActiveRank().isHighRank()) {
                sender.sendMessage(ChatColor.RED + "You cannot mute this player.");
                return;
            }
        }

        if (profile.getActivePunishmentByType(PunishmentType.MUTE) != null) {
            sender.sendMessage(ChatColor.RED + "This player is already muted.");
            return;
        }

        Profile senderProfile = Profile.getByUsername(sender.getName());

        assert senderProfile != null;
        String staffName = sender instanceof Player ? senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.MUTE, System.currentTimeMillis(), reason, time.getValue());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Player player = profile.getPlayer();

        if (player != null) {
            String senderName = sender instanceof Player ? senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName() : ChatColor.GREEN + "SYSTEM";
            player.sendMessage(ColorText.translate("&cYou have been " + punishment.getContext() + " by " + senderName + "."));
            player.sendMessage(ColorText.translate("&cThe reason for this punishment is " + punishment.getAddedReason()));

            if (!punishment.isPermanent()) {
                player.sendMessage(ChatColor.RED + "This mute will expire in " + punishment.getTimeRemaining() + ".");
            }
        }

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
        Engine.getInstance().getRedis().sendPacket(new PunishmentsAlertRedisCheck((sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getDisplayName() : "SYSTEM"), profile.getDisplayName(), (sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getLastServer() : ""), profile.getLastServer(), reason, duration, profile.getAlts().size(), punishment));
    }
}
