package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ModeratorChatCommand {

    @Command(names = {"staffchat", "sc"}, permission = "moderator.staffchat")
    public static void staffChatCommand(Player sender) {
        ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(sender.getName());
        assert suiteProfile != null;
        boolean toggle = !suiteProfile.isStaffChatEnabled();
        suiteProfile.setStaffChatEnabled(toggle);
        sender.sendMessage(ChatColor.YELLOW + "Moderator Chat: " + (toggle ? ChatColor.GREEN + "Enabled." : ChatColor.DARK_RED + "Disabled."));
    }
}
