package com.cobelpvp.engine.punishment.commands.create;

import com.cobelpvp.engine.checks.checks.BroadcastPunishmentRedisCheck;
import com.cobelpvp.engine.moderator.qchecks.checks.PunishmentsAlertRedisCheck;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.punishment.PunishmentType;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class KickCommand {

    @Command(names = {"kick"}, permission = "engine.kick")
    public static void kickCommand(CommandSender sender, @Param(name = "player") Player target, @Param(name = "reason", wildcard = true, defaultValue = "Breaking Rules") String reason) {
        Profile profile = Profile.getByUsername(target.getName());

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ColorText.translate("&cCannot found this profile."));
            return;
        }

        if (sender instanceof Player) {
            if (profile.getActiveRank().isHighRank()) {
                sender.sendMessage(ChatColor.RED + "You cannot kick this player.");
                return;
            }
        }

        Profile senderProfile = Profile.getByUsername(sender.getName());

        String staffName;
        if (sender instanceof Player) {
            assert senderProfile != null;
            staffName = (senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName());
        } else {
            staffName = ChatColor.GREEN + "SYSTEM";
        }

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.KICK, System.currentTimeMillis(), reason, -1);

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Engine.getInstance().getRedis().sendPacket(new BroadcastPunishmentRedisCheck(punishment, staffName, profile.getColoredUsername(), profile.getUuid()));
        Engine.getInstance().getRedis().sendPacket(new PunishmentsAlertRedisCheck((sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getDisplayName() : "SYSTEM"), profile.getDisplayName(), (sender instanceof Player ? Profile.getProfileMap().get(((Player) sender).getUniqueId()).getLastServer() : ""), profile.getLastServer(), reason, "", profile.getAlts().size(), punishment));

        new BukkitRunnable() {
            @Override
            public void run() {
                target.kickPlayer(punishment.getKickMessage());
            }
        }.runTask(Engine.getInstance());
    }
}
