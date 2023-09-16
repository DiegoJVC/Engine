package com.cobelpvp.engine.moderator.qchecks;

import com.cobelpvp.engine.moderator.qchecks.checks.*;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.bond.packet.handler.IncomingPacketHandler;
import com.cobelpvp.bond.packet.listener.PacketListener;
import com.cobelpvp.engine.moderator.qchecks.checks.*;
import com.cobelpvp.engine.punishment.PunishmentType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RedisQChecksListener implements PacketListener {

    @IncomingPacketHandler
    public void onPunishmentsAlerts(PunishmentsAlertRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("punishments.admin")) {
                String punishContext = packet.getPunishment().getContext();

                if (packet.getPunishment().getType() == PunishmentType.BAN || packet.getPunishment().getType() == PunishmentType.BAN_IP || packet.getPunishment().getType() == PunishmentType.BLACKLIST || packet.getPunishment().getType() == PunishmentType.KICK) {
                    players.sendMessage(ChatColor.RED + "Kicked " + packet.getTargetName());
                }

                Player staffPlayer = Bukkit.getPlayer(packet.getStaffName());
                players.sendMessage(ChatColor.DARK_RED + " * " + ChatColor.RED + packet.getStaffName() + (staffPlayer != null ? " (" + packet.getStaffServerName() + ") " : "") + punishContext + " " + packet.getTargetName() + " (" + packet.getTargetServerName() + ")");
                players.sendMessage(ChatColor.DARK_RED + " * " + ChatColor.RED + "Reason: " + packet.getReason());

                if (!(packet.getPunishment().isPermanent() || packet.getPunishment().getType() == PunishmentType.KICK)) {
                    players.sendMessage(ChatColor.DARK_RED + " * " + ChatColor.RED + "Time: " + packet.getDuration());
                }

                if (packet.getPunishment().getType() == PunishmentType.BAN_IP || packet.getPunishment().getType() == PunishmentType.BLACKLIST) {
                    players.sendMessage(ChatColor.DARK_RED + " * " + ChatColor.RED + "Banned 1 player and " + packet.getIps() + " IPs");
                }
            }
        }
    }

    @IncomingPacketHandler
    public void onStaffJoin(StaffJoinRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("moderator.staffjoin")) {
                players.sendMessage(ColorText.translate("&9[Staff] &r" + packet.getPlayerName() + "&b joined &7" + packet.getServerName()));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&9[Staff] &r" + packet.getPlayerName() + "&b joined &7" + packet.getServerName()));
    }

    @IncomingPacketHandler
    public void onStaffChatMute(ChatMutedAlertRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.isOp()) {
                players.sendMessage(ColorText.translate("&8(&9" + packet.getServerName() + "&8) &r" + packet.getStaff() + "&b " + packet.getStatus() + " chat."));
            }
        }
    }

    @IncomingPacketHandler
    public void onStaffLeave(StaffLeaveRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("moderator.staffleave")) {
                players.sendMessage(ColorText.translate("&9[Staff] &r" + packet.getPlayerName() + "&b left &7" + packet.getServerName() + "&b."));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&9[Staff] &r" + packet.getPlayerName() + "&b left &7" + packet.getServerName() + "&b."));
    }

    @IncomingPacketHandler
    public void onStaffChat(StaffChatRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("moderator.staffchat")) {
                players.sendMessage(ColorText.translate("&9[Staff] &7[" + packet.getServerName() + "] &r" + packet.getPlayerName() + "&b: " + packet.getChatMessage()));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&9[Staff] &7[" + packet.getServerName() + "] &r" + packet.getPlayerName() + "&b: " + packet.getChatMessage()));
    }

    @IncomingPacketHandler
    public void onStaffReport(StaffReportRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("moderator.staffreport")) {
                players.sendMessage(ColorText.translate("&9[Report] &7[" + packet.getServerName() + "] &r" + packet.getAccused() + " &7(" + packet.getReports() + ") reported by " + packet.getSentBy()));
                players.sendMessage(ColorText.translate("     &9Reason: &b" + packet.getReason()));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&9[Report] &7[" + packet.getServerName() + "] &r" + packet.getAccused() + " &7(" + packet.getReports() + ") reported by " + packet.getSentBy()));
        Bukkit.getConsoleSender().sendMessage(ColorText.translate("     &9Reason: &b" + packet.getReason()));
    }

    @IncomingPacketHandler
    public void onStaffRequest(StaffRequestRedisCheck packet) {
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (players.hasPermission("qmodsuite.staffrequest")) {
                players.sendMessage(ColorText.translate("&9[Request] &7[" + packet.getServerName() + "] " + packet.getSentBy() + "&r has requested assistance"));
                players.sendMessage(ColorText.translate("     &9Reason: &b" + packet.getReason()));
            }
        }

        Bukkit.getConsoleSender().sendMessage(ColorText.translate("&9[Request] &7[" + packet.getServerName() + "] " + packet.getSentBy() + "&r has requested assistance"));
        Bukkit.getConsoleSender().sendMessage(ColorText.translate("     &9Reason: &b" + packet.getReason()));
    }
}
