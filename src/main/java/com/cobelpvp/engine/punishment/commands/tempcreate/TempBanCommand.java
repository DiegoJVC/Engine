package com.cobelpvp.engine.punishment.commands.tempcreate;

import com.cobelpvp.engine.checks.checks.BanFailedRedisCheck;
import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.PunishmentsAlertRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.engine.util.Duration;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TempBanCommand {

    @Command(names = {"tempban", "tban", "tb"}, permission = "engine.tempban")
    public static void banCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "duration") String duration, @Param(name = "reason", wildcard =true, defaultValue = "Breaking Rules") String reason) {
        OfflinePlayer target = Bukkit.getServer().getOfflinePlayer(uuid);
        Profile profile = Profile.getByUuid(target.getUniqueId());
        Duration time = Duration.fromString(duration);

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, please try again...");
            return;
        }
        Profile senderProfile = Profile.getByUsername(sender.getName());

        assert senderProfile != null;

        if (sender instanceof Player) {
            if (profile.getActiveRank().isHighRank()) {
                Engine.getInstance().getRedis().sendPacket(new BanFailedRedisCheck(Bukkit.getServerName(), senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName(), profile.getColoredUsername()));
                sender.sendMessage(ChatColor.RED + "You cannot temp ban this player.");
                return;
            }
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(ChatColor.RED + "This player is already banned.");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getColoredUsername() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BAN, System.currentTimeMillis(), reason, time.getValue());

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
        Engine.getInstance().getRedis().sendPacket(new PunishmentsAlertRedisCheck((sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getDisplayName() : "SYSTEM"), profile.getDisplayName(), (sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getLastServer() : ""), profile.getLastServer(), reason, duration, profile.getAlts().size(), punishment));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Engine.getInstance());
        }
    }
}
