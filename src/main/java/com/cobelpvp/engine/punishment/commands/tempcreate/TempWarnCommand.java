package com.cobelpvp.engine.punishment.commands.tempcreate;

import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.PunishmentsAlertRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.engine.util.Duration;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class TempWarnCommand {

    @Command(names = {"tempwarn", "twarn"}, permission = "engine.tempwarn")
    public static void warnCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "duration") String duration, @Param(name = "reason", wildcard = true, defaultValue = "Breaking Rules") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(uuid);
        Duration time = Duration.fromString(duration);
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, please try again...");
            return;
        }

        if (sender instanceof Player) {
            if (profile.getActiveRank().isHighRank()) {
                sender.sendMessage(ChatColor.RED + "You cannot warn this player.");
                return;
            }
        }

        Profile senderProfile = Profile.getByUsername(sender.getName());

        assert senderProfile != null;
        String staffName = (sender instanceof Player ? senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName() : ChatColor.GREEN + "SYSTEM");

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(), reason, time.getValue());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Player player = profile.getPlayer();

        if (player != null) {
            player.sendMessage(ChatColor.YELLOW + "You have been warned by " + staffName + ChatColor.YELLOW + ".");
            player.sendMessage(ChatColor.YELLOW + "Time for punish: " + ChatColor.RED + time.getValue());
            player.sendMessage(ChatColor.YELLOW + "Reason for punishment: " + ChatColor.RED + reason);
        }

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
        Engine.getInstance().getRedis().sendPacket(new PunishmentsAlertRedisCheck((sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getDisplayName() : "SYSTEM"), profile.getDisplayName(), (sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getLastServer() : ""), profile.getLastServer(), reason, duration, profile.getAlts().size(), punishment));
    }
}
