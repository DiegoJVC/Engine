package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ModeratorModeCommand {

    @Command(names = {"staffmode", "staff", "h", "mod", "hacker", "silent"}, permission = "moderator.staffmode")
    public static void staffModeCommand(Player sender) {
        ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(sender.getName());
        assert suiteProfile != null;
        boolean toggle = !suiteProfile.isStaffModeEnabled();
        suiteProfile.setStaffModeEnabled(toggle);
        sender.sendMessage(ChatColor.YELLOW + "Moderator Mode: " + (toggle ? ChatColor.GREEN + "Enabled." : ChatColor.DARK_RED + "Disabled."));
    }
}
