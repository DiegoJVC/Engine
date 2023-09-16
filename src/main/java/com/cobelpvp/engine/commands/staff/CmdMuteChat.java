package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.util.ColorText;
import com.cobelpvp.engine.Engine;
import com.cobelpvp.engine.moderator.qchecks.checks.ChatMutedAlertRedisCheck;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdMuteChat {

    @Command(names = {"mutechat", "mc"}, permission = "engine.mutechat")
    public static void muteChatCommand(CommandSender sender) {
        Engine.getInstance().getChat().togglePublicChatMute();

        Profile profile = Profile.getByUsername(sender.getName());

        if (profile == null || !profile.isLoaded()) return;

        String senderName = (sender instanceof Player ? profile.getActiveGrant().getRank().getGameColor() + sender.getName() : ChatColor.DARK_RED + "Console");

        String context = Engine.getInstance().getChat().isPublicChatMuted() ? "muted" : "unmuted";

        Bukkit.broadcastMessage(ColorText.translate("&aChat have been &d" + context + " &aby &r" + senderName + "&a."));

        Engine.getInstance().getRedis().sendPacket(new ChatMutedAlertRedisCheck(senderName, context, Bukkit.getServerName()));
    }
}
