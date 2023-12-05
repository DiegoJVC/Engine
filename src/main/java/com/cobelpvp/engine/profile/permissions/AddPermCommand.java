package com.cobelpvp.engine.profile.permissions;

import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.engine.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AddPermCommand {

    public class AddPerm {
        @Command(names = {"perms add"}, permission = "engine.perms")
        public void execute(CommandSender sender, @Param(name = "target") Profile target, @Param(name = "permission") String permission) {
            target.getPermissions().add(permission);
            target.updatePermissions();
            target.save();
            sender.sendMessage(ChatColor.GOLD + "&aPermissions added successfully.");
        }
    }
}
