package com.cobelpvp.engine.punishment.commands;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.engine.punishment.menu.menus.PunishmentCheckMenu;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CheckCommand {

    @Command(names = {"check"}, permission = "engine.check")
    public static void checkCommand(CommandSender sender, @Param(name = "target") UUID uuid) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(ChatColor.RED + "Could not found this profile, because this doesn't exists.");
            return;
        }

        new PunishmentCheckMenu(profile).openMenu((Player) sender);
    }
}
