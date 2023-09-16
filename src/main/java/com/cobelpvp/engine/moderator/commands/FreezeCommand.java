package com.cobelpvp.engine.moderator.commands;

import com.cobelpvp.engine.moderator.profile.ModeratorProfile;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.ColorText;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FreezeCommand {

    @Command(names = {"freeze"}, permission = "moderator.freeze")
    public static void freezeCommand(CommandSender sender, @Param(name = "target") Player player) {
        ModeratorProfile modSuiteProfile = ModeratorProfile.getPlayerByName(player.getName());
        Profile profile = Profile.getByUsername(player.getName());

        if (modSuiteProfile == null || !modSuiteProfile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Cannot found this profile.");
            return;
        }

        if (sender.equals(player)) {
            sender.sendMessage(ChatColor.RED + "You cannot freeze yourself.");
            return;
        }

        Profile senderProfile = Profile.getByUsername(((Player) sender).getName());

        String staffName = (sender instanceof Player ? senderProfile.getActiveGrant().getRank().getGameColor() + senderProfile.getDisplayName() : ChatColor.DARK_RED + "Consoole");

        boolean toggle = !modSuiteProfile.isFreeze();
        modSuiteProfile.setFreeze(toggle);
        sender.sendMessage(ColorText.translate((toggle ? "&eYou froze: " + profile.getColoredUsername() : "&eYou un-froze: " + profile.getColoredUsername())));

        player.sendMessage(ColorText.translate((toggle ? "&eFrozed by: " + staffName : "&eUn-frozed by: " + staffName)));
    }
}
