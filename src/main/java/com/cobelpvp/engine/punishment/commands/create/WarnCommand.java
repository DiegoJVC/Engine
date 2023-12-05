package com.cobelpvp.engine.punishment.commands.create;

import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.PunishmentsAlertRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class WarnCommand {

    @Command(names = {"warn"}, permission = "engine.warn")
    public static void warnCommand(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "reason", wildcard = true, defaultValue = "Breaking Rules") String reason) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ColorText.translate("&cCannot found this profile."));
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

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(), reason, Integer.MAX_VALUE);

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Player player = profile.getPlayer();

        if (player != null) {
            player.sendMessage(ChatColor.YELLOW + "You have been warned by " + staffName + ChatColor.YELLOW + ".");
            player.sendMessage(ChatColor.YELLOW + "Reason for punishment: " + ChatColor.RED + reason);
        }

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
        Engine.getInstance().getRedis().sendPacket(new PunishmentsAlertRedisCheck((sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getDisplayName() : "SYSTEM"), profile.getDisplayName(), (sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getLastServer() : ""), profile.getLastServer(), reason, "", profile.getAlts().size(), punishment));
    }
}
