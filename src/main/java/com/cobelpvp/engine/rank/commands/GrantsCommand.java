package com.cobelpvp.engine.rank.commands;

import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.grant.menu.GrantsListMenu;
import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.UUID;

public class GrantsCommand {

    @Command(names = {"grants"}, permission = "op")
    public static void grantsCommand(CommandSender sender, @Param(name = "player") UUID uuid) {
        Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));
        new GrantsListMenu(profile).openMenu((Player) sender);
    }
}
