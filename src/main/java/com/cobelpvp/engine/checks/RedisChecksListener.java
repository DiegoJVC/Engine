package com.cobelpvp.engine.checks;

import com.cobelpvp.engine.checks.checks.*;
import com.cobelpvp.engine.grant.GrantAppliedEvent;
import com.cobelpvp.engine.grant.GrantExpireEvent;
import com.cobelpvp.engine.grant.OGrant;
import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.bond.packet.handler.IncomingPacketHandler;
import com.cobelpvp.bond.packet.listener.PacketListener;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.Punishment;
import com.cobelpvp.engine.checks.checks.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class RedisChecksListener implements PacketListener {

    @IncomingPacketHandler
    public void onAddGrant(AddGrantRedisCheck packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        OGrant grant = packet.getGrant();

        if (player != null) {
            Profile profile = Profile.getProfileMap().get(player.getUniqueId());
            profile.getGrants().removeIf(other -> Objects.equals(other, grant));
            profile.getGrants().add(grant);

            new GrantAppliedEvent(player, grant);
        }
    }

    @IncomingPacketHandler
    public void onDeleteGrant(DeleteGrantRedisCheck packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        OGrant grant = packet.getGrant();

        if (player != null) {
            Profile profile = Profile.getProfileMap().get(player.getUniqueId());
            profile.getGrants().removeIf(other -> Objects.equals(other, grant));
            profile.getGrants().add(grant);

            new GrantExpireEvent(player, grant);
        }
    }

    @IncomingPacketHandler
    public void onBroadcastPunishment(BroadcastPunishmentRedisCheck packet) {
        Punishment punishment = packet.getPunishment();
        punishment.broadcast(packet.getStaff(), packet.getTarget());

        Player player = Bukkit.getPlayer(packet.getTargetUuid());

        if (player != null) {
            Profile profile = Profile.getProfileMap().get(player.getUniqueId());
            profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
            profile.getPunishments().add(punishment);

            if (punishment.getType().isBan() && !punishment.isRemoved() && !punishment.hasExpired()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(punishment.getKickMessage());
                    }
                }.runTask(Engine.getInstance());
            }

        }

    }

    @IncomingPacketHandler
    public void onPlayerTryBan(BanFailedRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.isOp()) {
                players.sendMessage(ColorText.translate("&8(&b" + packet.getServerName() + "&8) &r" + packet.getPlayerName() + "&b tried to ban &r" + packet.getTargetName()));
            }
        }
    }

    @IncomingPacketHandler
    public void onServerStatus(ServerStatusRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.isOp()) {
                players.sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &bis now " + packet.getStatus() + "&b."));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &bis now " + packet.getStatus() + "&b."));
    }

    @IncomingPacketHandler
    public void onPlayerQuitFreeze(QuitFreezeRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("qmodsuite.staffmode")) {
                players.sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &r" + packet.getPlayerName() + "&b left frozen."));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &r" + packet.getPlayerName() + "&b left frozen."));
    }

    @IncomingPacketHandler
    public void onVIPAnnouncement(VIPAnnouncementCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage(ColorText.translate("&8[&4Alert&8] &r" + packet.getPlayerName() + " &bis now playing &3" + packet.getServerName() + ""));
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&8[&4Alert&8] &r" + packet.getPlayerName() + " &bis now playing &3" + packet.getServerName() + ""));
    }

    @IncomingPacketHandler
    public void onPlayerJoinFreeze(JoinFreezeRedisCheck packet) {

        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("qmodsuite.staffmode")) {
                players.sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &r" + packet.getPlayerName() + "&b joined frozen."));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &r" + packet.getPlayerName() + "&b joined frozen."));
    }

    @IncomingPacketHandler
    public void onRankRefresh(RefreshRankRedisCheck packet) {
        Rank rank = Rank.getRankByID(packet.getUuid());

        if (rank == null) {
            rank = new Rank(packet.getUuid(), packet.getName());
        }
        rank.load();
    }


    @IncomingPacketHandler
    public void onClearGrants(ClearPunishmentsRedisCheck packet) {
        Player player = Bukkit.getPlayer(packet.getUuid());

        if (player != null) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            profile.getGrants().clear();
        }
    }

    @IncomingPacketHandler
    public void onClearPunishments(ClearPunishmentsRedisCheck packet) {
        Player player = Bukkit.getPlayer(packet.getUuid());

        if (player != null) {
            Profile profile = Profile.getByUuid(player.getUniqueId());
            profile.getPunishments().clear();
        }
    }
}
