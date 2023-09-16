package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.engine.rank.Rank;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.EngineConstants;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InfoCommand {

    @Command(names = {"rank info"}, permission = "engine.rank.info")
    public static void rankInfoCommand(CommandSender sender, @Param(name = "displayName") String displayName) {
        Rank rank = Rank.getRankByDisplayName(displayName);

        if (rank == null) {
            sender.sendMessage(ColorText.translate("&cCannot found this rank."));
            return;
        }

        sender.sendMessage(EngineConstants.getCenter(ChatColor.AQUA + rank.getDisplayName() + " Information"));
        sender.sendMessage(ChatColor.GOLD + "Prefix: " + rank.getGamePrefix() + "Test");
        sender.sendMessage(ChatColor.GOLD + "Color: " + rank.getGameColor() + "Test");
        sender.sendMessage(ChatColor.GOLD + "General Weight: " + rank.getGeneralWeight());
        sender.sendMessage(ChatColor.GOLD + "Display Weight: " + rank.getDisplayWeight());
        if (rank.getPermissions().size() > 0) {
            sender.sendMessage(ChatColor.DARK_GREEN + "Permissions");
            sender.sendMessage(ChatColor.RED + StringUtils.join(rank.getPermissions(), ChatColor.GRAY + ","));
        }
        sender.sendMessage(ChatColor.GOLD + "Inherit: " + rank.getInherited());
    }
}
