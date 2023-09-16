package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import com.cobelpvp.atheneum.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VanishCommand {

    @Command(names = {"vanish", "v"}, permission = "moderator.vanish")
    public static void vanishCommand(Player sender) {
        ModeratorProfile suiteProfile = ModeratorProfile.getPlayerByName(sender.getName());
        assert suiteProfile != null;
        boolean toggle = !suiteProfile.isVanishEnabled();
        suiteProfile.setVanished(toggle);
        sender.sendMessage(ChatColor.YELLOW + "Vanish Mode: " + (toggle ? ChatColor.GREEN + "Enabled." : ChatColor.DARK_RED + "Disabled."));
    }
}
