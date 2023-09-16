package com.cobelpvp.engine.commands.staff;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.profile.menu.menus.PersonalProfileMenu;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CmdProfile {

    @Command(names = {"profile", "playerinfo"}, permission = "engine.playerinfo")
    public static void profileCommand(CommandSender sender, @Param(name = "target") UUID uuid) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not load this profile, because this doesn't exists.");
            return;
        }

        new PersonalProfileMenu(profile).openMenu((Player) sender);
    }
}
