package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.EngineConstants;
import com.cobelpvp.engine.punishment.PunishmentType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AltsCommand {

    @Command(names = {"alts"}, permission = "moderator.alts")
    public static void altsCommand(CommandSender sender, @Param(name = "target") UUID target) {
        if (target == null) return;

        Profile profile = Profile.getByUuid(target);

        List<Profile> alts = Profile.getByIpAddress(profile.getAddress());

        if (alts.size() <= 1) {
            sender.sendMessage(ChatColor.RED + "This player doesn't have alts right now, please try again later.");
        } else {
            sender.sendMessage(EngineConstants.getCenter(ChatColor.LIGHT_PURPLE + "Alts for " + profile.getDisplayName()));
            sender.sendMessage(ChatColor.DARK_RED + " * " + getAltsMessage(alts));
            sender.sendMessage(ColorText.translate("&aNot-Banned&7/&dBanned&7/&5IP-Banned&7/&4Blacklisted"));
        }
    }

    public static String getAltsMessage(List<Profile> alts) {
        List<String> messages = new ArrayList<>(alts.size());
        for (Profile altProfile : alts) {
            StringBuilder sb = new StringBuilder();
            if (altProfile.getActivePunishmentByType(PunishmentType.BLACKLIST) != null) {
                sb.append(ChatColor.DARK_RED);
            } else if (altProfile.getActivePunishmentByType(PunishmentType.BAN_IP) != null) {
                sb.append(ChatColor.DARK_PURPLE);
            } else if (altProfile.getActivePunishmentByType(PunishmentType.BAN) != null) {
                sb.append(ChatColor.RED);
            } else if (altProfile.getPlayer() == null) {
                sb.append(ChatColor.RED);
            } else {
                sb.append(ChatColor.GREEN);
            }
            sb.append(altProfile.getDisplayName());
            messages.add(sb.toString());
        }
        return StringUtils.join(messages, ", ");
    }
}
