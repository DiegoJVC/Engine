package com.cobelpvp.engine.commands.players;

import com.cobelpvp.engine.profile.Profile;
import com.cobelpvp.atheneum.command.Command;
import com.cobelpvp.atheneum.command.Param;
import com.cobelpvp.atheneum.util.PlayerUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CmdPing {

    @Command(names = {"ping"})
    public static void pingCmd(Player player, @Param(name = "target", defaultValue = "self") Player target) {
        if (player == target) {
            Profile profile = Profile.getByUsername(player.getName());
            assert profile != null;
            player.sendMessage(ChatColor.GOLD + "Ping: " + ChatColor.RED + PlayerUtils.getPing(player));
        } else {
            Profile profile = Profile.getByUsername(target.getName());
            assert profile != null;
            player.sendMessage(ChatColor.GOLD + target.getName() + "'s Ping: " + ChatColor.RED + PlayerUtils.getPing(target));
        }
    }
}
