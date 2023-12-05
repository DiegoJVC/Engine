package com.cobelpvp.engine.profile.permissions;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.uuid.TeamsUUIDCache;
import com.cobelpvp.engine.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class RemovePermCommand {
        @Command(names = {"perms remove"}, permission = "engine.perms")
        public void execute(CommandSender sender, @Param(name = "target") UUID uuid, @Param(name = "permission" , wildcard = true) String permission) {
            Profile profile = Profile.getByUsername(TeamsUUIDCache.name(uuid));

            assert profile != null;
            profile.getPermissions().remove(permission);
            profile.updatePermissions();
            profile.save();
            sender.sendMessage(ChatColor.GOLD + "&cPermissions removed successfully.");
        }
}
