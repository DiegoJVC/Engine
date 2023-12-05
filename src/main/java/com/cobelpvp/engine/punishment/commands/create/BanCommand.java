package com.cobelpvp.engine.punishment.commands.create;

import com.cobelpvp.engine.checks.checks.BanFailedRedisCheck;
import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.PunishmentsAlertRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class BanCommand {

    @Command(names = {"ban", "b"}, permission = "engine.ban")
    public static void banCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard = true, defaultValue = "Breaking Rules") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ColorText.translate("&cCannot found this profile."));
            return;
        }

        if (Bukkit.getPlayer(uuid) != null) {
            if (Bukkit.getPlayer(uuid).equals(sender)) {
                sender.sendMessage(ColorText.translate("&cYou cannot ban yourself"));
                return;
            }
        }

        if (sender instanceof Player) {
            if (profile.getActiveRank().isHighRank()) {

                Profile senderProfile = Profile.getByUsername(sender.getName());

                Engine.getInstance().getRedis().sendPacket(new BanFailedRedisCheck(Bukkit.getServerName(), senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName(), profile.getColoredUsername()));
                sender.sendMessage(ColorText.translate("&cYou cannot ban this player."));
                return;
            }
        }

        if (profile.getActivePunishmentByType(PunishmentType.BAN) != null) {
            sender.sendMessage(ColorText.translate("&cThis player is already banned."));
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getColoredUsername() : ChatColor.GREEN + "SYSTEM";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BAN, System.currentTimeMillis(), reason, Integer.MAX_VALUE);

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
        Engine.getInstance().getRedis().sendPacket(new PunishmentsAlertRedisCheck((sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getDisplayName() : "SYSTEM"), profile.getDisplayName(), (sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getLastServer() : ""), profile.getLastServer(), reason, "", profile.getAlts().size(), punishment));

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
